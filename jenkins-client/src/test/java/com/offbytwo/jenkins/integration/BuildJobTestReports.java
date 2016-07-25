package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import com.offbytwo.jenkins.model.TestCase;
import com.offbytwo.jenkins.model.TestChild;
import com.offbytwo.jenkins.model.TestChildReport;
import com.offbytwo.jenkins.model.TestReport;
import com.offbytwo.jenkins.model.TestResult;
import com.offbytwo.jenkins.model.TestSuites;

public class BuildJobTestReports {

    @Test
    public void shouldAddStringParamToAnExistingJob() throws IOException {
         JenkinsServer js = new
         JenkinsServer(URI.create("http://localhost:10090/buildserver/"), "admin", "admin");
//        JenkinsServer js = new JenkinsServer(URI.create("http://ci.soebes.de:8080/"));
        // MavenJobWithDetails mavenJob = js.getMavenJob("javaee");
        MavenJobWithDetails mavenJob = js.getMavenJob("maven-test");

        BuildWithDetails details = mavenJob.getLastSuccessfulBuild().details();
        // BuildWithDetails details = mavenJob.getBuilds().get(8).details();
        System.out.println("Build Number: " + details.getNumber());

        TestReport testReport = mavenJob.getLastBuild().getTestReport();
        System.out.println("------ Tests");
        System.out.println("    urlName: " + testReport.getUrlName());
        System.out.println("  failCount: " + testReport.getFailCount());
        System.out.println("  skipCount: " + testReport.getSkipCount());
        System.out.println(" totalCount: " + testReport.getTotalCount());

        List<TestChildReport> childReports = testReport.getChildReports();
        for (TestChildReport testChildReport : childReports) {
            TestChild child = testChildReport.getChild();
            System.out.println(" Child number: " + child.getNumber());
            System.out.println(" Child    url: " + child.getUrl());

            TestResult testResult = testChildReport.getResult();
            System.out.println(" Child   duration: " + testResult.getDuration());
            System.out.println(" Child  failCount: " + testResult.getFailCount());
            System.out.println(" Child  passCount: " + testResult.getPassCount());
            System.out.println(" Child  skipCount: " + testResult.getSkipCount());
            List<TestSuites> suites = testResult.getSuites();
            for (TestSuites testSuite : suites) {
                System.out.println("   TestSuite  duration:" + testSuite.getDuration());
                System.out.println("   TestSuite        id:" + testSuite.getId());
                System.out.println("   TestSuite      name:" + testSuite.getName());
                System.out.println("   TestSuite timestamp:" + testSuite.getTimestamp());
                List<TestCase> testCases = testSuite.getCases();
                double sumTestCases = 0.0;
                for (TestCase testCase : testCases) {
                    System.out.println("     ------------------------------------------");
                    System.out.println("     TestCase:             age: " + testCase.getAge());
                    System.out.println("     TestCase:       classname: " + testCase.getClassName());
                    System.out.println("     TestCase:        duration: " + testCase.getDuration());
                    System.out.println("     TestCase:     failedSince: " + testCase.getFailedSince());
                    System.out.println("     TestCase:            name: " + testCase.getName());
                    System.out.println("     TestCase:          status: " + testCase.getStatus());
                    System.out.println("     TestCase:    errorDetails: " + testCase.getErrorDetails());
                    System.out.println("     TestCase: errorStackTrace: " + testCase.getErrorStackTrace());
                    sumTestCases += testCase.getDuration();
                }
                System.out.println("----> SUM: " + sumTestCases);
            }
        }

    }
}
