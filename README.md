# A Jenkins API Client for Java

[![MIT Licence](https://img.shields.io/github/license/jenkinsci/java-client-api.svg?label=License)](http://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/com.offbytwo.jenkins/jenkins-client.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.offbytwo.jenkins%22%20a%3A%22jenkins-client%22)
[![Build Status](https://travis-ci.org/jenkinsci/java-client-api.svg?branch=master)](https://travis-ci.org/jenkinsci/java-client-api)
[![Javadocs](https://javadoc.io/badge/com.offbytwo.jenkins/jenkins-client.svg?color=blue)](https://javadoc.io/doc/com.offbytwo.jenkins/jenkins-client)

## Important Note

The Jenkins API Client For Java has now moved under the umbrella of the Jenkins GitHub Organization.

## What is the "Jenkins API Client for Java"?

This library is just a piece of java code which uses the REST API of jenkins.
This means you can trigger builds, extract informations about jobs or builds
etc. The information you can extract will be represented in java objects which
you can reuse for other purposes or integrate this library into other parts for
a higher level of integration.
 
## Getting Started

If you like to use this library you need to add the library as a dependency
to your project. This can be done by using a Maven dependency like the following: 

```xml
<dependency>
  <groupId>com.offbytwo.jenkins</groupId>
  <artifactId>jenkins-client</artifactId>
  <version>0.3.8</version>
</dependency>
```

This can also being done by defining a Gradle dependency like this:

```
compile 'com.offbytwo.jenkins:jenkins-client:0.3.8'
```

Starting with a future release 0.4.0 the groupId/artifactId will change (NOT YET DONE!)

```xml
<dependency>
  NOT YET FINALIZED NOR RELEASED !!!
  <groupId>org.jenkins-ci.lib</groupId>
  <artifactId>java-client-api</artifactId>
  <version>0.4.0</version>
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

## Running Tests
To run only unit tests without invoking the integration tests use the following command:

```
mvn clean install -DskipITs
```

## Running Integration Tests
To run integration tests simply start

```
mvn -Prun-its clean verify
```

There is also a module which contains [integration tests][integration-tests] 
which are running with a special version of Jenkins
within a Docker container to check several aspects of the API which can't be
covered by the usual integration tests.

## Release Notes

You can find details about the different releases in the [Release Notes](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md).

 * [Release 0.3.9 NOT RELEASED YET](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-039).
 * [Release 0.3.8](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-038).
 * [Release 0.3.7](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-037).
 * [Release 0.3.6](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-036).
 * [Release 0.3.5](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-035).
 * [Release 0.3.4](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-034).
 * [Release 0.3.3](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-033).
 * [Release 0.3.2](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-032).
 * [Release 0.3.1](https://github.com/jenkinsci/java-client-api/blob/master/ReleaseNotes.md#release-031).

## Contribution

### Creating Issues

If you find a problem please create an 
[issue in the ticket system with the component `java-client-api`](https://issues.jenkins-ci.org/projects/JENKINS/)
and describe what is going wrong or what you expect to happen.
If you have a full working example or a log file this is also helpful.
You should of course describe only a single issue in a single ticket and not 
mixing up several different things into a single issue.

### Creating a Pull Request

Before you create a pull request it is necessary to create an issue in
the [ticket system before with the component `java-client-api`](https://issues.jenkins-ci.org/browse/JENKINS)
and describe what the problem is or what kind of feature you would like
to add. Afterwards you can create an appropriate pull request.

It is required if you want to get a Pull request to be integrated into please
squash your commits into a single commit which references the issue in the
commit message which looks like this:

```
Fixed #Issue
 o Description.
```

This makes it simpler to merge it and this will also close the
appropriate issue automatically in one go. This make the life as 
maintainer a little bit easier.

A pull request has to fulfill only a single ticket and should never
create/add/fix several issues in one, cause otherwise the history is hard to
read and to understand and makes the maintenance of the issues and pull request
hard or to be honest impossible.

Furthermore it is necessary to create appropriate entries into the `ReleaseNotes.md`
file as well.


## Help & Questions

You can ask questions in the [mailing list](https://groups.google.com/d/forum/java-client-api)
 which is also intended as discussion forum for development.

## Generated Site

http://jenkinsci.github.io/java-client-api/

## License

Copyright (C) 2013, Cosmin Stejerean, Karl Heinz Marbaise, and contributors.

Distributed under the MIT license: http://opensource.org/licenses/MIT

[integration-tests]: https://github.com/jenkinsci/java-client-api/tree/master/jenkins-client-it-docker
