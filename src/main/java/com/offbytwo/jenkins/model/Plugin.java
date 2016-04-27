package com.offbytwo.jenkins.model;

import java.util.List;

/**
 * @author Karl Heinz Marbaise
 */
public class Plugin extends BaseModel
{
    private boolean active;
    private String backupVersion;
    private boolean bundled;
    private boolean downgradable;
    private boolean enabled;
    private boolean hasUpdate;
    private String longName;
    private boolean pinned;
    private String shortName;
    private String supportsDynamicLoad; // YesNoMayBe
    private String url;
    private String version;

    private List<PluginDependency> dependencies;

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }

    public String getBackupVersion()
    {
        return backupVersion;
    }

    public void setBackupVersion( String backupVersion )
    {
        this.backupVersion = backupVersion;
    }

    public boolean isBundled()
    {
        return bundled;
    }

    public void setBundled( boolean bundled )
    {
        this.bundled = bundled;
    }

    public boolean isDowngradable()
    {
        return downgradable;
    }

    public void setDowngradable( boolean downgradable )
    {
        this.downgradable = downgradable;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

    public boolean isHasUpdate()
    {
        return hasUpdate;
    }

    public void setHasUpdate( boolean hasUpdate )
    {
        this.hasUpdate = hasUpdate;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public boolean isPinned()
    {
        return pinned;
    }

    public void setPinned( boolean pinned )
    {
        this.pinned = pinned;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getSupportsDynamicLoad()
    {
        return supportsDynamicLoad;
    }

    public void setSupportsDynamicLoad( String supportsDynamicLoad )
    {
        this.supportsDynamicLoad = supportsDynamicLoad;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public List<PluginDependency> getDependencies()
    {
        return dependencies;
    }

    public void setDependencies( List<PluginDependency> dependencies )
    {
        this.dependencies = dependencies;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( active ? 1231 : 1237 );
        result = prime * result + ( ( backupVersion == null ) ? 0 : backupVersion.hashCode() );
        result = prime * result + ( bundled ? 1231 : 1237 );
        result = prime * result + ( ( dependencies == null ) ? 0 : dependencies.hashCode() );
        result = prime * result + ( downgradable ? 1231 : 1237 );
        result = prime * result + ( enabled ? 1231 : 1237 );
        result = prime * result + ( hasUpdate ? 1231 : 1237 );
        result = prime * result + ( ( longName == null ) ? 0 : longName.hashCode() );
        result = prime * result + ( pinned ? 1231 : 1237 );
        result = prime * result + ( ( shortName == null ) ? 0 : shortName.hashCode() );
        result = prime * result + ( ( supportsDynamicLoad == null ) ? 0 : supportsDynamicLoad.hashCode() );
        result = prime * result + ( ( url == null ) ? 0 : url.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Plugin other = (Plugin) obj;
        if ( active != other.active )
            return false;
        if ( backupVersion == null )
        {
            if ( other.backupVersion != null )
                return false;
        }
        else if ( !backupVersion.equals( other.backupVersion ) )
            return false;
        if ( bundled != other.bundled )
            return false;
        if ( dependencies == null )
        {
            if ( other.dependencies != null )
                return false;
        }
        else if ( !dependencies.equals( other.dependencies ) )
            return false;
        if ( downgradable != other.downgradable )
            return false;
        if ( enabled != other.enabled )
            return false;
        if ( hasUpdate != other.hasUpdate )
            return false;
        if ( longName == null )
        {
            if ( other.longName != null )
                return false;
        }
        else if ( !longName.equals( other.longName ) )
            return false;
        if ( pinned != other.pinned )
            return false;
        if ( shortName == null )
        {
            if ( other.shortName != null )
                return false;
        }
        else if ( !shortName.equals( other.shortName ) )
            return false;
        if ( supportsDynamicLoad == null )
        {
            if ( other.supportsDynamicLoad != null )
                return false;
        }
        else if ( !supportsDynamicLoad.equals( other.supportsDynamicLoad ) )
            return false;
        if ( url == null )
        {
            if ( other.url != null )
                return false;
        }
        else if ( !url.equals( other.url ) )
            return false;
        if ( version == null )
        {
            if ( other.version != null )
                return false;
        }
        else if ( !version.equals( other.version ) )
            return false;
        return true;
    }
    
    
    
}
