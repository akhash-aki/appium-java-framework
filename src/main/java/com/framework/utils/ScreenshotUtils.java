package com.framework.utils;

import com.framework.constants.FrameworkConstants;
import com.framework.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot utility for capturing base64 strings and image files.
 */
public final class ScreenshotUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures a Base64 encoded screenshot for direct embedding into ExtentReports.
     */
    public static String getBase64Screenshot() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LOGGER.error("Failed to capture Base64 screenshot", e);
            return "";
        }
    }

    /**
     * Saves screenshot to reports/screenshots directory.
     */
    public static String captureScreenshotAsFile(String testName) {
        try {
            File source = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destPath = FrameworkConstants.REPORT_PATH + File.separator + "screenshots" + File.separator + testName + "_" + timestamp + ".png";
            File destination = new File(destPath);
            FileUtils.copyFile(source, destination);
            LOGGER.info("Saved failure screenshot to {}", destPath);
            return destPath;
        } catch (IOException e) {
            LOGGER.error("Failed to save screenshot file for test {}", testName, e);
            return null;
        }
    }
}
