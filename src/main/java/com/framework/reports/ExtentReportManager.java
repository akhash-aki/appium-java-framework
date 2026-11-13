package com.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

/**
 * Thread-safe management of ExtentReports 5.
 */
public final class ExtentReportManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> TEST_THREAD_LOCAL = new ThreadLocal<>();

    private ExtentReportManager() {
        // Prevent instantiation
    }

    public static synchronized void initReports() {
        if (Objects.isNull(extent)) {
            new File(FrameworkConstants.REPORT_PATH).mkdirs();
            String fullReportPath = FrameworkConstants.REPORT_PATH + File.separator + FrameworkConstants.REPORT_FILE_NAME;
            
            ExtentSparkReporter spark = new ExtentSparkReporter(fullReportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Appium Test Execution Report");
            spark.config().setReportName("Mobile Automation Regression Suite");
            spark.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Appium 2.x Java 17");
            extent.setSystemInfo("Java Client", "9.3.0");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));

            LOGGER.info("Initialized ExtentReports at {}", fullReportPath);
        }
    }

    public static synchronized void flushReports() {
        if (Objects.nonNull(extent)) {
            extent.flush();
            LOGGER.info("Flushed ExtentReports output.");
        }
    }

    public static ExtentTest getTest() {
        return TEST_THREAD_LOCAL.get();
    }

    public static void setTest(ExtentTest test) {
        TEST_THREAD_LOCAL.set(test);
    }

    public static void unload() {
        TEST_THREAD_LOCAL.remove();
    }

    public static synchronized ExtentTest createTest(String testCaseName, String description) {
        ExtentTest test = extent.createTest(testCaseName, description);
        setTest(test);
        return test;
    }
}
