package com.offbytwo.jenkins.integration;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class IntegrationSuite
{
    public void name(  )
    {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { NoExecutorStartedGetComputerSetIT.class });
        testng.addListener(tla);
        testng.run();

    }
}
