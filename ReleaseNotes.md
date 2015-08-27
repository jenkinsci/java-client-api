# Release Notes

## Release 0.3.1

API Changes:

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


Issue #53

  This release contains new functionality like support to get the list of existing views.
  This can be accomplished by using the following:

```java
Map<String, View> views = jenkins.getViews();
```

Issue #67

  Added ComputerSet and Executor informations.


Issue #82

  ChangeSet and culprits


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


Issue #89

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
