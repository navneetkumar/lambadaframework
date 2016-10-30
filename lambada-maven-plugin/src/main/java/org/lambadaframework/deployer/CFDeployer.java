package org.lambadaframework.deployer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.lambadaframework.AbstractMojoPlugin;
import org.lambadaframework.aws.ApiGateway;
import org.lambadaframework.aws.Cloudformation;
import org.lambadaframework.aws.LambdaFunction;
import org.lambadaframework.aws.StaticApiGateway;

@Mojo(name = "cf", requiresDirectInvocation = true,
requiresProject = true,
defaultPhase = LifecyclePhase.NONE,
requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
@Execute(phase = LifecyclePhase.NONE)
public class CFDeployer extends AbstractMojoPlugin {
	
	@Override
    public void execute() throws MojoExecutionException {
        try {
            printLogo();
            
            Deployment deployment = getDeployment();
            
            // Set up Cloudformation
            Cloudformation.CloudFormationOutput cloudFormationOutput = executeCloudFormation();
            
            // Set up VPC of Lambda, create new version
            getLog().info("LAMBDA");
            LambdaFunction lambdaFunction = new LambdaFunction(cloudFormationOutput.getLambdaFunctionArn(), deployment);
            lambdaFunction.setLog(getLog());
            String functionAlias = lambdaFunction.deployLatestVersion();
            getLog().info(LOG_SEPERATOR);
            
            // Set up APIGateway, create new version
            getLog().info("API GATEWAY");
            StaticApiGateway apiGateway = new StaticApiGateway(deployment, functionAlias, cloudFormationOutput.getApiGatewayId());
            apiGateway.setLog(getLog());
            apiGateway.deployEndpoints();
            getLog().info(LOG_SEPERATOR);
            
        } catch (Exception e) {
            throw new MojoExecutionException("Exception at CF deployment", e);
        }
    }

}