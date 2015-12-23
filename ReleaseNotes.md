# Release Notes

## Release 0.3.4 (NOT RELEASED YET)


### API Changes

  [Added disableJob, enabledJob][pull-123]
  The following methods have been added to support enabling
  and disabling jobs.

```java
void disableJob(String jobName);
void disableJob(String jobName, boolean crumbFlag);
void enableJob(String jobName);
void enableJob(String jobName, boolean crumbFlag);
```

## Release 0.3.3

  [Fixed #111 A missing dependency to jaxen caused problems][issue-111]
  The problem was that the dependency has missed from the compile scope
  which caused problems for users to use. The dependency was there via
  transitive dependency of a test scoped artifact.

  [Do not propagate IOException when GET for /CrumbIssuer fails][issue-116]

### API Changes

  [Added Cloudbees Folder support][issue-108]
  The following methods have been added to support `FolderJob` type.

```java
void createFolder(String jobName, Boolean crumbFlag);
Optional<FolderJob> getFolderJob(Job job);
Map<String, Job> getJobs(FolderJob folder);
Map<String, Job> getJobs(FolderJob folder, String view);
Map<String, View> getViews(FolderJob folder);
View getView(FolderJob folder, String name);
JobWithDetails getJob(FolderJob folder, String jobName);
MavenJobWithDetails getMavenJob(FolderJob folder, String jobName);
void createJob(FolderJob folder, String jobName, String jobXml);
void createJob(FolderJob folder, String jobName, String jobXml, Boolean crumbFlag);
```

## Release 0.3.2

### API Changes:

  [JobWithDetails has been enhanced with information about the
  first build][issue-91].

```java
Build getFirstBuild()
```

  [The `JenkinsServer` API has been enhanced to get information about the Queue](issue-104)

```java
QueueItem getQueueItem(QueueReference ref) throws IOException 
```

```java
Build getBuild(QueueItem q)  throws IOException 
```

 The `JenkinsHttpClient` API has been changed from the following:

```java
JenkinsHttpClient(URI uri, DefaultHttpClient defaultHttpClient);
```
 into

```java
JenkinsHttpClient(URI uri, CloseableHttpClient client);
```

  Furthermore the `JenkinsHttpClient` API has been enhanced with the following
  method which allows to create an unauthenticated Jenkins HTTP client.

```java
JenkinsHttpClient(URI uri, HttpClientBuilder builder);
```

  The `Build` class `Stop` method needed to be changed internally based on an
  inconsistencies in Jenkins versions. (This might be change in future). There
  are versions 1.565 which supports the stop method as a post call, version
  1.609.1 support it as a get call and 1.609.2 as a post call.


  The `Job` class has been enhanced to trigger parameterized builds.

```java
QueueReference build(Map<String, String> params, boolean crumbFlag) throws IOException;
```


## Release 0.3.1

### API Changes:


 Until to Release 0.3.0 the `getLoadStatistics()` method returned a simple `Map` which
 needed to be changed to represent the information which is returned.

 So the old one looked like this:

```java
Map getLoadStatistics();
```

 The new one looks like this:

```java
LoadStatistics getLoadStatistics() throws IOException
```

 You can see how it works by using the [JenkinsLoadStatisticsExample.java][2].

 The API for `getExecutors()` has been changed from a simple [`Map` into something
 more meaningful][issue-67].

 The old one looked like this:

```java
List<Map> getExecutors(); 
```

  where as the new API looks like this:


```java
List<Executor> getExecutors();
```

  This will result in a list of [Executor][4] which contain supplemental informations
  about the appropriate executor.


## Issues

### [Issue #53][issue-53]

  This release contains new functionality like support to get the list of existing views.
  This can be accomplished by using the following:

```java
Map<String, View> views = jenkins.getViews();
```

### [Issue #67][issue-67]

  The change for `getExecutors()` which is documented in API changes.

### [Issue #82][issue-82]

  The information about the `ChangeSet` and the `culprit` has been improved.

```java
JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:8080/jenkins"), "admin", "password");
MavenJobWithDetails mavenJob = js.getMavenJob("javaee");
BuildWithDetails details = mavenJob.getLastSuccessfulBuild().details();
```

  So now you can extract the causes which is related to the trigger which triggered the build.

```java
List<BuildCause> causes = details.getCauses();
```

  With the help of `getChangeSet()` you can extract the changeset information of your build:

```java
BuildChangeSet changeSet = details.getChangeSet();
```

  By using the `changeSet.getKind()` you would expect to get the kind of version control
  which has been used. If you use Git or Subversion this is true but unfortunately it 
  is not true for all version control systems (for example TFS). 

  The information about files which have been changed an other information can be extracted
  by using the following:

```java
List<BuildChangeSetItem> items = changeSet.getItems();
```

  If you like to get a better overview [check the example file in the project][1].


### [Issue #89][issue-89]

  Extract TestReport from Jenkins

```java
JenkinsServer js = new JenkinsServer(URI.create("http://jenkins-server/"));
MavenJobWithDetails mavenJob = js.getMavenJob("MavenJob");
BuildWithDetails details = mavenJob.getLastSuccessfulBuild().details();
TestReport testReport = mavenJob.getLastSuccessfulBuild().getTestReport(); 
```

  You can extract the information about the tests which have run which contains
  information about the number of tests, failed tests and skipped tests etc.
  [For a more detailed example take a look into the project][3].





[1]: https://github.com/RisingOak/jenkins-client/blob/master/src/test/java/com/offbytwo/jenkins/integration/JenkinsChangeSetExample.java
[2]: https://github.com/RisingOak/jenkins-client/blob/master/src/test/java/com/offbytwo/jenkins/integration/JenkinsLoadStatisticsExample.java
[3]: https://github.com/RisingOak/jenkins-client/blob/master/src/test/java/com/offbytwo/jenkins/integration/BuildJobTestReports.java
[4]: https://github.com/RisingOak/jenkins-client/blob/master/src/main/java/com/offbytwo/jenkins/model/Executor.java
[issue-53]: https://github.com/RisingOak/jenkins-client/issues/53
[issue-67]: https://github.com/RisingOak/jenkins-client/issues/67
[issue-82]: https://github.com/RisingOak/jenkins-client/issues/82
[issue-89]: https://github.com/RisingOak/jenkins-client/issues/89
[issue-91]: https://github.com/RisingOak/jenkins-client/issues/91
[issue-104]: https://github.com/RisingOak/jenkins-client/issues/104
[issue-111]: https://github.com/RisingOak/jenkins-client/issues/111
[issue-116]: https://github.com/RisingOak/jenkins-client/issues/116
[issue-108]: https://github.com/RisingOak/jenkins-client/issues/108
[pull-123]: https://github.com/RisingOak/jenkins-client/pull/123

