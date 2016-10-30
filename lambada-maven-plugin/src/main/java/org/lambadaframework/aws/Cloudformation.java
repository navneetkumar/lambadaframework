package org.lambadaframework.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.*;
import com.amazonaws.util.IOUtils;

import org.lambadaframework.deployer.Deployment;

import java.io.IOException;
import java.util.List;

public class Cloudformation extends AWSTools {

    
    private final static String LAMBDA_EXECUTION_IAM_RESOURCE_NAME = "LambdaExecutionRoleArn";
    private final static String LAMBDA_EXECUTION_NAME = "LambdaFunctionArn";
    private final static String API_GATEWAY_ROOT = "APIGatewayRoot";


    private AmazonCloudFormationClient cloudformationClient;

    protected Deployment deployment;

    public Cloudformation(Deployment deployment) {
        this.deployment = deployment;
    }

    protected AmazonCloudFormationClient getCloudFormationClient() {
        if (null != cloudformationClient) {
            return cloudformationClient;
        }

        return cloudformationClient = new AmazonCloudFormationClient(getAWSCredentialsProvideChain()).withRegion(Region.getRegion(Regions.fromName(deployment.getRegion())));
    }

    public String getCloudformationTemplate() {
        return getTemplateFromFile();
    }
    
    public String getTemplateFromFile() {
    	String template = "";
    	try {
    		template = IOUtils.toString(
			Thread.currentThread().getContextClassLoader().getResourceAsStream("cloudformation.json"));
			log.info("Loaded the CF template from file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return template;
    }


    public static class CloudFormationOutput {

        protected String lambdaExecutionRole;

        protected String lambdaFunctionArn;
        
        protected String apiGatewayId;

        public String getApiGatewayId() {
			return apiGatewayId;
		}

		public void setApiGatewayId(String apiGatewayId) {
			this.apiGatewayId = apiGatewayId;
		}

		public String getLambdaExecutionRole() {
            return lambdaExecutionRole;
        }

        public CloudFormationOutput setLambdaExecutionRole(String lambdaExecutionRole) {
            this.lambdaExecutionRole = lambdaExecutionRole;
            return this;
        }

        public String getLambdaFunctionArn() {
            return lambdaFunctionArn;
        }

        public CloudFormationOutput setLambdaFunctionArn(String lambdaFunctionArn) {
            this.lambdaFunctionArn = lambdaFunctionArn;
            return this;
        }
    }

    public String waitForCompletion() throws Exception {

        DescribeStacksRequest wait = new DescribeStacksRequest();
        wait.setStackName(deployment.getCloudFormationStackName());
        Boolean completed = false;
        String stackStatus = "Unknown";
        String stackReason = "";

        log.info("Waiting");

        int iteration = 0;
        while (!completed) {
            List<Stack> stacks = getCloudFormationClient().describeStacks(wait).getStacks();
            if (stacks.isEmpty()) {
                completed = true;
                stackStatus = "NO_SUCH_STACK";
                stackReason = "Stack has been deleted";
            } else {
                for (Stack stack : stacks) {

                    if (stack.getStackStatus().contains("FAILED")
                            || stack.getStackStatus().equals(StackStatus.UPDATE_ROLLBACK_COMPLETE.toString())
                            || stack.getStackStatus().equals(StackStatus.ROLLBACK_COMPLETE.toString())
                            ) {
                        throw new Exception("Cloudformation failed. Please check AWS Console for details");
                    }


                    if (stack.getStackStatus().equals(StackStatus.UPDATE_COMPLETE.toString())
                            || stack.getStackStatus().equals(StackStatus.CREATE_COMPLETE.toString())
                            ) {
                        completed = true;
                        stackStatus = stack.getStackStatus();
                        stackReason = stack.getStackStatusReason();
                    }
                }
            }

            // Show we are waiting
            log.info("Please wait (" + ++iteration + ")...");

            // Not done yet so sleep for 2 seconds.
            if (!completed) Thread.sleep(1000);
        }

        // Show we are done
        log.info("Cloudformation update completed.");

        return stackStatus + (stackReason != null ? " (" + stackReason + ")" : "");
    }

    public CloudFormationOutput getStackOutputs(AmazonCloudFormation stackbuilder,
                                                String stackName) {
        DescribeStacksRequest wait = new DescribeStacksRequest();
        wait.setStackName(stackName);
        List<Stack> stacks = getCloudFormationClient().describeStacks(wait).getStacks();

        CloudFormationOutput cloudFormationOutput = new CloudFormationOutput();

        for (Stack stack : stacks) {
            if (stack.getStackName().equals(stackName)) {
                stack.getOutputs().forEach(output -> {
                    if (output.getOutputKey().equals(LAMBDA_EXECUTION_IAM_RESOURCE_NAME)) {
                        cloudFormationOutput.setLambdaExecutionRole(output.getOutputValue());
                    }

                    if (output.getOutputKey().equals(LAMBDA_EXECUTION_NAME)) {
                        cloudFormationOutput.setLambdaFunctionArn(output.getOutputValue());
                    }
                    
                    if (output.getOutputKey().equals(API_GATEWAY_ROOT)) {
                        cloudFormationOutput.setApiGatewayId(output.getOutputValue());
                    }
                });
                return cloudFormationOutput;
            }
        }
        throw new RuntimeException("Unknown Cloudformation error. Try deploying.");

    }


    public CloudFormationOutput createOrUpdateStack() throws Exception {
        log.info("Creating or updating Cloudformation stack" + getStackName());
        log.info("CF Parammeters = 	" + deployment.getCloudFormationParameters());
        try {
            createStack(deployment, getCloudformationTemplate());
        } catch (AlreadyExistsException alreadyExistsException) {
            log.info(getStackName() + "Stack already exists. Trying to update.");
            try {
                updateStack(deployment, getCloudformationTemplate());
            } catch (AmazonServiceException noUpdateNeededException) {
                log.info("No updates needed for Cloudformation stack" + getStackName() + ".Resuming deployment.");
            }
        }

        return getStackOutputs(getCloudFormationClient(), deployment.getCloudFormationStackName());
    }

    protected void createStack(Deployment deployment,
                               String templateBody) throws Exception {

        String templateName = deployment.getCloudFormationStackName();
        CreateStackRequest createRequest = new CreateStackRequest();
        createRequest.setStackName(templateName);
        createRequest.setTemplateBody(templateBody);
        createRequest.setParameters(deployment.getCloudFormationParameters());
        createRequest.withCapabilities(Capability.CAPABILITY_IAM);
        getCloudFormationClient().createStack(createRequest);
        log.info("Stack creation completed, the stack " + templateName + " completed with " + waitForCompletion());
    }

    protected void updateStack(Deployment deployment,
                               String templateBody) throws Exception {
        String templateName = deployment.getCloudFormationStackName();
        UpdateStackRequest updateStackRequest = new UpdateStackRequest();
        updateStackRequest.setStackName(templateName);
        updateStackRequest.setTemplateBody(templateBody);
        updateStackRequest.setParameters(deployment.getCloudFormationParameters());
        updateStackRequest.withCapabilities(Capability.CAPABILITY_IAM);
        getCloudFormationClient().updateStack(updateStackRequest);
        log.info("Stack update completed, the stack " + templateName + " completed with " + waitForCompletion());
    }
    
    private String getStackName() {
    	return  " [" + deployment.getCloudFormationStackName() + "] " ;
    }

}

