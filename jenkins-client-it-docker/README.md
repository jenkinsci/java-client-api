Docker Configuration for Jenkins API Client
===========================================

This project contains the integration tests for the 
[Jenkins API client for Java][1].

This project defines an Jenkins environment by using
a Docker image definition which is used within these
integration tests.

The `plugin.txt` contains the list of plugins which will be installed
during the build of the image.
Those plugins are needed for the integration tests.

How to run the Docker ITs on Windows (w/o Hyper-V)
----
  * [install Docker Toolbox][2] to get VirtualBox and Docker QuickStart 
    Terminal onto your host system
  * open Docker QuickStart Terminal and execute 
    * `cd jenkins-client-it-docker`
    * `docker build --no-cache -t jenkins-with-plugins .`
  * edit the configuration of the VirtualBox machine and add a new shared 
    folder in the VirtualBox instance with the name "jobs" that points to 
    "java-client-api\jenkins-client-it-docker\jobs"
  * open Docker QuickStart Terminal and execute 
    * `docker run --name jenkins-for-testing -v /jobs:/var/jenkins_home/jobs -d -p 8080:8080 -p 50000:50000  --env JENKINS_OPTS=--httpPort=8080 jenkins-with-plugins`
    * `mvn -Prun-its,run-docker-its clean verify --batch-mode -DjenkinsUrl=http://192.168.99.100:8080/`


TODO
----
  * Create a docker image which contains [JaCoCo plugin installed][pr-99].
  * Create a docker image which contains [TestNG plugin installed][pr-99].
  
  * Create an docker image which contains at least two different jobs
    with the same name which differ only in case something like
    `FirstJob` and `firstjob` see [pull-request-127][pr-127]. 
     
  * Write an [integration test][issue-119] to check if we can download 
    a file from the jenkins workspace via Jenkins Client API.


STATUS
------

  Proof of Concept how to write integration tests
  based on Docker image for Jenkins.

[1]: https://github.com/jenkinsci/java-client-api/
[issue-119]: https://github.com/jenkinsci/java-client-api//issues/119
[pr-99]: https://github.com/jenkinsci/java-client-api//pull/99
[pr-127]: https://github.com/jenkinsci/java-client-api//pull/127
[2]: https://docs.docker.com/toolbox/overview/