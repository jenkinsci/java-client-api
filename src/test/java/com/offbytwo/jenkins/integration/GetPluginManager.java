package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Plugin;
import com.offbytwo.jenkins.model.PluginDependency;
import com.offbytwo.jenkins.model.PluginManager;

/**
 * @author Karl Heinz Marbaise
 */
public class GetPluginManager
{
    @Test
    public void shouldAddStringParamToAnExistingJob()
        throws IOException
    {
        JenkinsServer js = new JenkinsServer( URI.create( "http://localhost:10090/" ) );

        PluginManager pluginManager = js.getPluginManager();

        pluginManager.getPlugins();
        for ( Plugin plugin : pluginManager.getPlugins() )
        {
            System.out.println( "Plugin: " + plugin.getShortName() );
            System.out.println( "      longName: " + plugin.getLongName() );
            System.out.println( "           url: " + plugin.getUrl() );
            System.out.println( "   dynamicLoad: " + plugin.getSupportsDynamicLoad() );
            System.out.println( " backupVersion: " + plugin.getBackupVersion() );
            System.out.println( "       version: " + plugin.getVersion() );
            System.out.println( "        pinned: " + plugin.isPinned() );
            System.out.println( "        acitve: " + plugin.isActive() );
            System.out.println( "       bundled: " + plugin.isBundled() );
            System.out.println( "  downgradable: " + plugin.isDowngradable() );
            System.out.println( "        enable: " + plugin.isEnabled() );
            System.out.println( "     hasUpdate: " + plugin.isHasUpdate() );
            System.out.println( "              : " );
            for ( PluginDependency dep : plugin.getDependencies() )
            {
                System.out.println( "              ----------------- " );
                System.out.println( "                     name: " + dep.getShortName() );
                System.out.println( "                    short: " + dep.getShortName() );
                System.out.println( "                  version: " + dep.getVersion() );
                System.out.println( "                 optional: " + dep.isOptional() );
            }
        }
    }

}
