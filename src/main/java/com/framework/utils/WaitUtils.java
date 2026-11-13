package com.framework.utils;

import com.framework.config.ConfigReader;
import com.framework.constants.FrameworkConstants;
import com.framework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Explicit wait utility methods wrapping WebDriverWait.
 */
public final class WaitUtils {

    private WaitUtils() {
        // Prevent instantiation
    }

    private static WebDriverWait getWait(Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout);
    }

    private static WebDriverWait getDefaultWait() {
        int timeoutSeconds = ConfigReader.getInt("explicit.wait.seconds", FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
        return getWait(Duration.ofSeconds(timeoutSeconds));
    }

    public static WebElement waitForVisibility(By by) {
        return getDefaultWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement waitForVisibility(By by, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement waitForClickability(By by) {
        return getDefaultWait().until(ExpectedConditions.elementToBeClickable(by));
    }

    public static WebElement waitForClickability(By by, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.elementToBeClickable(by));
    }

    public static WebElement waitForPresence(By by) {
        return getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static boolean waitForInvisibility(By by) {
        return getDefaultWait().until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public static boolean waitForTextToBePresent(By by, String text) {
        return getDefaultWait().until(ExpectedConditions.textToBePresentInElementLocated(by, text));
    }
}
