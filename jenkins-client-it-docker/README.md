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
