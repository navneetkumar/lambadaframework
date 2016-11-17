package org.lambadaframework.aws;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.model.*;
import org.lambadaframework.deployer.Deployment;
import org.lambadaframework.jaxrs.model.Resource;
import org.lambadaframework.jaxrs.model.ResourceMethod;
import org.lambadaframework.jaxrs.JAXRSParser;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;


public class StaticApiGateway extends AWSTools {

    protected Deployment deployment;
    protected String functionAlias;
    protected AmazonApiGateway apiGatewayClient;
    protected RestApi amazonApi;
    protected String apiGatewayId;

    public StaticApiGateway(Deployment deployment, String functionAlias, String apiGatewayId) {
        this.deployment = deployment;
        this.functionAlias = functionAlias;
        this.apiGatewayId = apiGatewayId;
    }



    protected AmazonApiGateway getApiGatewayClient() {
        if (apiGatewayClient != null) {
            return apiGatewayClient;
        }

        return apiGatewayClient = new AmazonApiGatewayClient(getAWSCredentialsProvideChain()).withRegion(Region.getRegion(Regions.fromName(deployment.getRegion())));
    }

    /**
     * This method update API gateway endPoint with stage variables
     *
     * @throws IOException General exception while deploying
     */
    public void deployEndpoints()
            throws IOException {
        if (log != null)
            log.info("API Gateway deployment is being initialized.");
        createDeployment();
    }

    private void createDeployment() {

        if (log != null) {
            log.info("Creating new deployment");
        }
        log.info("Finding api with id = " + apiGatewayId);
        String stageName = "release";
        Map<String, String> stageVariables = new HashMap<String, String>();
        stageVariables.put("functionAlias", functionAlias);
        log.info("Deploying api to lambda alias = " + functionAlias);    
		CreateDeploymentResult deploymentResult = getApiGatewayClient().createDeployment(new CreateDeploymentRequest()
                .withRestApiId(apiGatewayId)
                .withDescription(deployment.getProjectName() + " v" + deployment.getVersion())
                .withStageDescription(deployment.getStage())
                .withStageName(stageName)
                .withVariables(stageVariables)
        );

        if (log != null) {
            log.info("Created new deployment: " + deploymentResult.getId());
        }


        String apiUrl = "https://" +
        		apiGatewayId +
                ".execute-api." +
                deployment.getRegion() +
                ".amazonaws.com/" +
                stageName;


        if (log != null) {
            log.info("Your API is online at: " + apiUrl);
        }

    } 
}
