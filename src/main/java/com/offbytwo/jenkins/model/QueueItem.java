package com.offbytwo.jenkins.model;

public class QueueItem extends BaseModel
{
  // actions

  boolean blocked;

  boolean buildable;

  Long id;

  Long inQueueSince;

  String params;

  boolean stuck;

  // task

  String url;

  String why;

  boolean cancelled;

  Executable executable;

  public boolean isBlocked()
  {
    return blocked;
  }

  public void setBlocked(boolean blocked)
  {
    this.blocked = blocked;
  }

  public boolean isBuildable()
  {
    return buildable;
  }

  public void setBuildable(boolean buildable)
  {
    this.buildable = buildable;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getInQueueSince()
  {
    return inQueueSince;
  }

  public void setInQueueSince(Long inQueueSince)
  {
    this.inQueueSince = inQueueSince;
  }

  public String getParams()
  {
    return params;
  }

  public void setParams(String params)
  {
    this.params = params;
  }

  public boolean isStuck()
  {
    return stuck;
  }

  public void setStuck(boolean stuck)
  {
    this.stuck = stuck;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getWhy()
  {
    return why;
  }

  public void setWhy(String why)
  {
    this.why = why;
  }

  public boolean isCancelled()
  {
    return cancelled;
  }

  public void setCancelled(boolean cancelled)
  {
    this.cancelled = cancelled;
  }

  public Executable getExecutable()
  {
    return executable;
  }

  public void setExecutable(Executable executable)
  {
    this.executable = executable;
  }

  
}
