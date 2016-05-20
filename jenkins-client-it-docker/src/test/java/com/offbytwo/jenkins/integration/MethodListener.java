package com.offbytwo.jenkins.integration;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SkipException;

public class MethodListener implements IInvokedMethodListener {

    private String lastClassWithoutPackage;

    private String getClassNameWithoutPackage(String canonicalName) {
        int lastIndexOf = canonicalName.lastIndexOf('.');
        return canonicalName.substring(lastIndexOf + 1);
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            ITestNGMethod testMethod = method.getTestMethod();
            String classNameWithoutPackage = getClassNameWithoutPackage(testMethod.getTestClass().getName());

            if (lastClassWithoutPackage == null) {
                System.out.println(classNameWithoutPackage + ":");
            } else {
                if (!lastClassWithoutPackage.equals(classNameWithoutPackage)) {
                    System.out.println(classNameWithoutPackage + ":");
                }
            }
            lastClassWithoutPackage = classNameWithoutPackage;
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            ITestNGMethod testMethod = method.getTestMethod();
            String classNameWithoutPackage = getClassNameWithoutPackage(testMethod.getTestClass().getName());

            if (testResult.isSuccess()) {
                System.out.print("  SUCCESS ");
            } else {
                // Isn't there an more elegant way?
                if (testResult.getThrowable() instanceof SkipException) {
                    System.out.print("  SKIPPED ( " + testResult.getThrowable().getMessage() + " ) ");
                } else {
                    System.out.print("  FAILURE ");
                }
            }

            System.out.println("  " + testMethod.getMethodName());
            lastClassWithoutPackage = classNameWithoutPackage;

        }
    }

}
