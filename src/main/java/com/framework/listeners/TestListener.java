package com.framework.listeners;

import com.framework.reports.ExtentLogger;
import com.framework.reports.ExtentReportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener intercepting lifecycle events for reporting and logging.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOGGER.info("Starting Test Suite: {}", suite.getName());
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        LOGGER.info("Completed Test Suite: {}", suite.getName());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        LOGGER.info("==== Starting Test: {} ====", testName);
        ExtentReportManager.createTest(testName, description);
        ExtentLogger.info("Test execution started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.info("==== Test Passed: {} ====", testName);
        ExtentLogger.pass("Test passed successfully: " + testName);
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.error("==== Test Failed: {} ====", testName, result.getThrowable());
        ExtentLogger.fail("Test failed: " + testName, result.getThrowable());
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.warn("==== Test Skipped: {} ====", testName);
        ExtentLogger.skip("Test skipped: " + testName);
        ExtentReportManager.unload();
    }
}
