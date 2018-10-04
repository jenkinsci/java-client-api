package com.offbytwo.jenkins.it.testcontainer;

import java.io.IOException;
import java.net.URI;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame.OutputType;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.View;

public class FirstContainerTest {

//	public static GenericContainer<?> jenkins = new GenericContainer("jenkins/jenkins:2.142-slim")
//			.withExposedPorts(8080);
//	@ClassRule
//	public static GenericContainer<?> jenkins = new GenericContainer("jenkins:1.651.3")
//			.withExposedPorts(8080);
//	withCopyFileToContainer(MountableFile.forClasspathResource("/app.jar"), "/")
	
	@ClassRule
	public static GenericContainer<?> jenkins = new GenericContainer(
			new ImageFromDockerfile()
    			.withFileFromClasspath("plugins.txt", "/plugins.txt")
    			.withFileFromClasspath( "config.xml", "/config.xml" )
				.withDockerfileFromBuilder(builder -> 
				{
					builder
						.from("jenkins:1.651.3")
						.copy("plugins.txt", "/usr/share/jenkins/ref/plugins.txt")
						.copy("config.xml", "/usr/share/jenkins/ref/config.xml")
						.run("/usr/local/bin/plugins.sh", "/usr/share/jenkins/ref/plugins.txt");
	
				}));

	
	@Test
	public void testName() throws InterruptedException, IOException {
		System.out.println("Hello world.");
	
		String containerIpAddress = jenkins.getContainerIpAddress();
		Integer mappedPort = jenkins.getMappedPort(8080);
		System.out.println("IP:" + containerIpAddress + " mappedPort:" + mappedPort);
		
		JenkinsServer jenkinsServer = new JenkinsServer(URI.create("http://" + containerIpAddress + ":" + mappedPort + "/"));
		
		ToStringConsumer stdoutStringConsumer = new ToStringConsumer();
		ToStringConsumer stderrStringConsumer = new ToStringConsumer();
		jenkins.followOutput(stdoutStringConsumer, OutputType.STDOUT);
		jenkins.followOutput(stderrStringConsumer, OutputType.STDERR);

		String utf8String = stdoutStringConsumer.toUtf8String();
		String utf8StringErr = stderrStringConsumer.toUtf8String();

		System.out.println(utf8String);
		System.out.println(utf8StringErr);

		while (!jenkinsServer.isRunning()) {
			Thread.sleep(500);
		};
		
		View job = jenkinsServer.getView( "Test-View" );
		
		System.out.println( "Job:" + job.getDescription() + " " + job.getName());
	}
}
