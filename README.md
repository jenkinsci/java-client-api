# jenkins-client

A Java client for the Jenkins API

## Getting Started

To get started add the following dependency to your project

```xml
<dependency>
  <groupId>com.offbytwo.jenkins</groupId>
  <artifactId>jenkins-client</artifactId>
  <version>0.1.4</version>
</dependency>
```

## Usage

The `com.offbytwo.jenkins.JenkinsServer` class provides the main entry
point into the API. You can create a reference to the Jenkins server
given its location and (optionally) a username and password/token.

```java
JenkinsServer jenkins = new JenkinsServer(new URI("http://localhost:8080/jenkins"), "admin", "password")
```

At the top level you can access all of the currently defined
jobs. This returns a map of job names (in lower case) to jobs.

```java
Map<String, Job> jobs = jenkins.getJobs()
```

The Job class provides only summary information (name and url). You can retrieve details as follows

```java
JobWithDetails job = jobs.get("My Job").details()
```

The `JobWithDetails` class provides you with access to the list of
builds (and related information such as the first, last, successful,
etc) and upstream and downstream projects.

## Running Integration Tests

Integration tests require a running jenkins instance populated with some data.

To set up passing integration tests:

1. start local instance of jenkins with no security on port 8080
2. IMPORTANT: install the "Git Plugin" on this instance
3. Create a build named "trunk"
4. trigger at least 5 builds, the most recent of which should succeed
5. create a build named "pr"
6. make that build paramaterized
7. Add a parameter called REVISION whose default value is "foobar"
8. Trigger at least one successful build using that parameter
9. Now run JenkinsServerIntegration.java tests

## License

Copyright (C) 2013, Rising Oak LLC.

Distributed under the MIT license: http://opensource.org/licenses/MIT
