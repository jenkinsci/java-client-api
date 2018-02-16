package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Plugin;
import com.offbytwo.jenkins.model.PluginManager;

@Test( groups = { Groups.NO_EXECUTOR_GROUP } )
public class NoExecutorStartedPluginManagerIT
    extends AbstractJenkinsIntegrationCase
{

    private PluginManager pluginManager;

    @BeforeMethod
    public void beforeMethod()
        throws IOException
    {
        pluginManager = jenkinsServer.getPluginManager();
    }

    @Test
    public void getPluginsShouldReturn9ForJenkins20()
    {
        // TODO: Check why there is such a difference in the number of Plugins?
        if ( jenkinsServer.getVersion().isLessThan( "2.0" ) )
        {
            throw new SkipException( "Not Version 2.0 (" + jenkinsServer.getVersion() + ")" );
        }
        assertThat( pluginManager.getPlugins() ).hasSize( 21 );
    }

    @Test
    public void getPluginsShouldReturn27ForJenkins1651()
    {
        JenkinsVersion jv = jenkinsServer.getVersion();
        if ( jv.isLessThan( "1.651" ) || jv.isGreaterThan( "1.651.3" ) )
        {
            throw new SkipException( "Not Version 1.651 (" + jv.toString() + ")" );
        }
        assertThat( pluginManager.getPlugins() ).hasSize( 21 );
    }

    private Plugin createPlugin( String shortName, String version )
    {
        Plugin result = new Plugin();
        result.setShortName( shortName );
        result.setVersion( version );
        return result;
    }

    @Test
    public void getPluginsShouldReturnTheListOfInstalledPluginsForJenkins20()
    {

        if ( jenkinsServer.getVersion().isLessThan( "2.0" ) )
        {
            throw new SkipException( "Not Version 2.0 (" + jenkinsServer.getVersion() + ")" );
        }

        // TODO: The list of plugins is contained in the plugin.txt
        // which should be read and used as base of comparison.
        // instead of maintaining at two locations.
        //@formatter:off
        Plugin[] expectedPlugins = {
            createPlugin("workflow-api", "2.25"), 
            createPlugin("workflow-job", "2.17"), 
            createPlugin("workflow-support", "2.18"), 
            createPlugin("workflow-step-api", "2.14"), 
            createPlugin("workflow-durable-task", "2.18"), 
            createPlugin("workflow-durable-task-step", "2.19"), 
            createPlugin("token-macro", "2.3"), 
            createPlugin("translation", "1.10"),
            createPlugin("testng-plugin", "1.14"), 
            createPlugin("matrix-project", "1.12"),
            createPlugin("job-dsl", "1.68"), 
            createPlugin("windows-slaves", "1.0"),
            createPlugin("antisamy-markup-formatter", "1.1"), 
            createPlugin("junit", "1.24"),
            createPlugin("maven-plugin", "2.7.1"), 
            createPlugin("external-monitor-job", "1.4"),
            createPlugin("jacoco", "2.2.1"), 
            createPlugin("pam-auth", "1.1"), 
            createPlugin("ldap", "1.11"),
            createPlugin("script-security", "1.41"), 
            createPlugin("mailer", "1.11"), 
            createPlugin("cvs", "2.11"),
            createPlugin("ant", "1.2"), 
            createPlugin("config-file-provider", "2.17"),
            createPlugin("matrix-auth", "1.1"),
            createPlugin("javadoc", "1.1"), 
            createPlugin("timestamper", "1.8.9"),
            createPlugin("credentials", "2.1.16"), 
            createPlugin("throttle-concurrents", "2.0.1"),
            createPlugin("subversion", "1.54"), 
            createPlugin("ssh-slaves", "1.9"),
            createPlugin("ssh-credentials", "1.13"), 
            createPlugin("cloudbees-folder", "6.3"), 
            createPlugin("durable-task", "1.18"), 
            createPlugin("scm-api", "2.2.6"), 
            createPlugin("structs", "1.14"), 

        };
        //@formatter:on
        List<Plugin> plugins = pluginManager.getPlugins();

        for ( Plugin plugin : plugins )
        {
            boolean found = false;
            for ( int i = 0; i < expectedPlugins.length; i++ )
            {
                if ( plugin.getShortName().equals( expectedPlugins[i].getShortName() )
                    && plugin.getVersion().equals( expectedPlugins[i].getVersion() ) )
                {
                    found = true;
                }
            }
            assertThat( found ).as( "Plugin shortName:%s version:%s couldn't be found.", plugin.getShortName(),
                                    plugin.getVersion() ).isTrue();
        }

    }

    @Test
    public void getPluginsShouldReturnTheListOfInstalledPluginsFor1651()
    {
        JenkinsVersion jv = jenkinsServer.getVersion();
        if ( jv.isLessThan( "1.651" ) || jv.isGreaterThan( "1.651.3" ) )
        {
            throw new SkipException( "Not Version 1.651 (" + jv + ")" );
        }

        // TODO: Check why there is such a difference in the number of Plugins?
        // TODO: The list of plugins is contained in the plugin.txt
        // which should be read and used as base of comparison.
        // instead of maintaining at two locations.
        //@formatter:off
        Plugin[] expectedPlugins = { 
            createPlugin("workflow-support", "2.18"), 
            createPlugin("token-macro", "1.12.1"), 
            createPlugin("translation", "1.10"),
            createPlugin("testng-plugin", "1.10"), 
            createPlugin("matrix-project", "1.4.1"),
            createPlugin("job-dsl", "1.41"), 
            createPlugin("windows-slaves", "1.0"),
            createPlugin("antisamy-markup-formatter", "1.1"), 
            createPlugin("junit", "1.10"),
            createPlugin("maven-plugin", "2.7.1"), 
            createPlugin("external-monitor-job", "1.4"),
            createPlugin("jacoco", "1.0.19"), 
            createPlugin("pam-auth", "1.1"), 
            createPlugin("ldap", "1.11"),
            createPlugin("script-security", "1.13"), 
            createPlugin("mailer", "1.11"), 
            createPlugin("cvs", "2.11"),
            createPlugin("ant", "1.2"), 
            createPlugin("config-file-provider", "2.10.0"),
            createPlugin("ssh-credentials", "1.10"), 
            createPlugin("matrix-auth", "1.1"),
            createPlugin("javadoc", "1.1"), 
            createPlugin("timestamper", "1.7.2"),
            createPlugin("credentials", "1.24"), 
            createPlugin("throttle-concurrents", "1.9.0"),
            createPlugin("subversion", "1.54"), 
            createPlugin("ssh-slaves", "1.9"),
            createPlugin("cloudbees-folder", "5.12"), 
        };
        //@formatter:on
        List<Plugin> plugins = pluginManager.getPlugins();

        for ( Plugin plugin : plugins )
        {
            boolean found = false;
            for ( int i = 0; i < expectedPlugins.length; i++ )
            {
                if ( plugin.getShortName().equals( expectedPlugins[i].getShortName() )
                    && plugin.getVersion().equals( expectedPlugins[i].getVersion() ) )
                {
                    found = true;
                }
            }
            assertThat( found ).as( "Plugin shortName:%s version:%s couldn't be found.", plugin.getShortName(),
                                             plugin.getVersion() ).isTrue();
        }
    }

}
