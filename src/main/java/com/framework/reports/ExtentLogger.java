package com.framework.reports;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.framework.utils.ScreenshotUtils;

import java.util.Objects;

/**
 * Utility to log messages and screenshots directly into the active ExtentTest.
 */
public final class ExtentLogger {

    private ExtentLogger() {
        // Prevent instantiation
    }

    public static void pass(String message) {
        if (Objects.nonNull(ExtentReportManager.getTest())) {
            ExtentReportManager.getTest().log(Status.PASS, message);
        }
    }

    public static void fail(String message, Throwable throwable) {
        if (Objects.nonNull(ExtentReportManager.getTest())) {
            String base64 = ScreenshotUtils.getBase64Screenshot();
            if (base64 != null && !base64.isEmpty()) {
                ExtentReportManager.getTest().fail(message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } else {
                ExtentReportManager.getTest().fail(message);
            }
            if (throwable != null) {
                ExtentReportManager.getTest().fail(throwable);
            }
        }
    }

    public static void info(String message) {
        if (Objects.nonNull(ExtentReportManager.getTest())) {
            ExtentReportManager.getTest().log(Status.INFO, message);
        }
    }

    public static void skip(String message) {
        if (Objects.nonNull(ExtentReportManager.getTest())) {
            ExtentReportManager.getTest().log(Status.SKIP, message);
        }
    }
}
