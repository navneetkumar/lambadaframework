package org.lambadaframework;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.lambadaframework.aws.Cloudformation;
import org.lambadaframework.aws.Cloudformation.CloudFormationOutput;
import org.lambadaframework.deployer.Deployment;

import java.util.List;
import java.util.Properties;


public abstract class AbstractMojoPlugin extends AbstractMojo {


    @Parameter(required = true)
    public String packageName;

    @Parameter(required = true)
    public String stageToDeploy;

    /**
     * Specifies the region where the application will be deployed.
     * Should be a valid AWS Region with Lambda and API Gateway support
     */
    @Parameter(required = true)
    public String regionToDeploy;

    /**
     * Specifies the maximum execution time allowed for Lambda function (seconds)
     */
    @Parameter(defaultValue = "3")
    public String lambdaMaximumExecutionTime = "3";

    /**
     * Specifies the maximum memory size allowed for Lambda function (MB)
     */
    @Parameter(defaultValue = "128")
    public String lambdaMemorySize = "128";

    /**
     * Lambda execution role policy ARN
     * <p>
     * This policy is attached to the default Lambda execution profile.
     * <p>
     * With this policy, Lambda function can access to the resources allowed in the policy.
     * <p>
     * In case of this property is not set, a default policy with sufficient permissions
     * to execute Lambda function in a VPC is assigned to the Lambda function.
     */
    @Parameter
    public List<CharSequence> lambdaExecutionRolePolicies;

    @Parameter
    public List<String> lambdaSecurityGroups;

    @Parameter
    public List<String> lambdaSubnetIds;

    protected static final String LOG_SEPERATOR = new String(new char[72]).replace("\0", "-");

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    public MavenProject mavenProject;

    protected void printLogo() {
        getLog().info(LOG_SEPERATOR);
        getLog().info("__     ");
        getLog().info("\\ \\    ");
        getLog().info(" \\ \\   ");
        getLog().info("  > \\  ");
        getLog().info(" / ^ \\ ");
        getLog().info("/_/ \\_\\");
    }

    public Deployment getDeployment() {

        Properties cloudFormationParameters = new Properties();
        cloudFormationParameters.setProperty(Deployment.LAMBDA_MAXIMUM_EXECUTION_TIME_KEY, lambdaMaximumExecutionTime);
        cloudFormationParameters.setProperty(Deployment.LAMBDA_MEMORY_SIZE_KEY, lambdaMemorySize);

        if (lambdaExecutionRolePolicies != null) {
            cloudFormationParameters.setProperty(Deployment.LAMBDA_EXECUTION_ROLE_POLICY_KEY, String.join(",", lambdaExecutionRolePolicies));
        }


        if (lambdaSecurityGroups != null && lambdaSubnetIds != null) {
            cloudFormationParameters.setProperty(Deployment.LAMBDA_VPC_SECURITY_GROUPS_KEY, String.join(",", lambdaSecurityGroups));
            cloudFormationParameters.setProperty(Deployment.LAMBDA_VPC_SUBNETS_KEY, String.join(",", lambdaSubnetIds));
        } else if (lambdaSecurityGroups != null || lambdaSubnetIds != null) {
            throw new RuntimeException("lambdaSecurityGroups and lambdaSubnetIds should be set together.");
        }

        Deployment deployment = new Deployment(
                mavenProject,
                packageName,
                cloudFormationParameters,
                regionToDeploy,
                stageToDeploy);

        deployment.setLog(getLog());


        return deployment;
    }


    /**
     * Applies Cloudformation template.
     * <p>
     * Built-in Cloudformation template creates Lambda function and the necessary IAM Roles.
     * <p>
     * If CF template does not exist it creates a new one.
     *
     * @param deployment Deployment
     * @return CloudFormationOutput
     * @throws Exception
     */
    protected Cloudformation.CloudFormationOutput applyCloudFormation(Deployment deployment) throws Exception {
        Cloudformation cloudformation = new Cloudformation(deployment);
        cloudformation.setLog(getLog());
        return cloudformation.createOrUpdateStack();
    }
    
    protected CloudFormationOutput executeCloudFormation() throws Exception {
          getLog().info(LOG_SEPERATOR);          
          getLog().info("CLOUDFORMATION");
          getLog().info(LOG_SEPERATOR);
          Cloudformation.CloudFormationOutput cloudFormationOutput = applyCloudFormation(getDeployment());
          getLog().info("Deployed IAM Role: " + cloudFormationOutput.getLambdaExecutionRole());
          getLog().info("Deployed Lambda Function ARN: " + cloudFormationOutput.getLambdaFunctionArn());
          getLog().info("Deployed API gateway-id is : " + cloudFormationOutput.getApiGatewayId());
          getLog().info(LOG_SEPERATOR);
          return cloudFormationOutput;
          
    }
}
