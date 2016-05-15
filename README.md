# A Jenkins API Client for Java

[![MIT Licence](https://img.shields.io/github/license/RisingOak/jenkins-client.svg?label=License)](http://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/com.offbytwo.jenkins/jenkins-client.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.offbytwo.jenkins%22%20a%3A%22jenkins-client%22)
[![Build Status](https://travis-ci.org/RisingOak/jenkins-client.svg?branch=master)](https://travis-ci.org/RisingOak/jenkins-client)

## Getting Started

To get started add the following dependency to your project

```xml
<dependency>
  <groupId>com.offbytwo.jenkins</groupId>
  <artifactId>jenkins-client</artifactId>
  <version>0.3.5</version>
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

There is also a separate project which contains [integration
tests][integration-tests] which are running with a special version of Jenkins
within a Docker container to check several aspects of the API which can't be
covered by the usual integration tests.

## Release Notes

You can find details about the different releases in the [Release Notes](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md).

 * [Release 0.4.0](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-040).
 * [Release 0.3.6](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-036).
 * [Release 0.3.5](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-035).
 * [Release 0.3.4](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-034).
 * [Release 0.3.3](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-033).
 * [Release 0.3.2](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-032).
 * [Release 0.3.1](https://github.com/RisingOak/jenkins-client/blob/master/ReleaseNotes.md#release-031).

## Contribution

### Creating Issues

If you find a problem please create an 
[issue in the ticket system](https://github.com/RisingOak/jenkins-client/issues)
and describe what is going wrong or what you expect to happen.
If you have a full working example or a log file this is also helpful.
You should of course describe only a single issue in a single ticket and not 
mixing up several different things into a single issue.

### Creating a Pull Request

Before you create a pull request it is necessary to create an issue in
the [ticket system before](https://github.com/RisingOak/jenkins-client/issues)
and describe what the problem is or what kind of feature you would like
to add. Afterwards you can create an appropriate pull request.

It is required if you want to get a Pull request to be integrated into please
squash your commits into a single commit which references the issue in the
commit message.

A pull request has to fulfill only a single ticket and should never
create/add/fix several issues in one, cause otherwise the history is hard to
read and to understand and makes the maintenance of the issues and pull request
hard.

Furthermore it is neccesary to create appropriate entries into the `ReleaseNotes.md`
file as well.


## Generated Site

http://risingoak.github.io/jenkins-client/

## License

Copyright (C) 2013, Rising Oak LLC.

Distributed under the MIT license: http://opensource.org/licenses/MIT

[integration-tests]: https://github.com/RisingOak/jenkins-client-docker
