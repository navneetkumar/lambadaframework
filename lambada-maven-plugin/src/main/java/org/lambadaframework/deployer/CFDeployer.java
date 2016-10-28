package org.lambadaframework.deployer;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;
import static spark.Spark.threadPool;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.lambadaframework.AbstractMojoPlugin;

@Mojo(name = "setup", requiresDirectInvocation = true,
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
            getLog().info(LOG_SEPERATOR);
            getLog().info("Starting setup of lambada");
            
        } catch (Exception e) {
            throw new MojoExecutionException("Exception at CF deployment", e);
        }
    }

}