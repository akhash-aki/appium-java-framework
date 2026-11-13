package com.framework.pages;

import com.framework.driver.DriverManager;
import com.framework.reports.ExtentLogger;
import com.framework.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Page providing common UI interaction primitives and wait wrappers.
 */
public abstract class BasePage {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AppiumDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected void click(By by, String elementName) {
        WebElement element = WaitUtils.waitForClickability(by);
        element.click();
        logger.info("Clicked on element: {}", elementName);
        ExtentLogger.info("Clicked on: <b>" + elementName + "</b>");
    }

    protected void sendKeys(By by, String text, String elementName) {
        WebElement element = WaitUtils.waitForVisibility(by);
        element.sendKeys(text);
        logger.info("Entered text into {}: '{}'", elementName, text);
        ExtentLogger.info("Entered text into <b>" + elementName + "</b>: " + text);
    }

    protected void clearAndSendKeys(By by, String text, String elementName) {
        WebElement element = WaitUtils.waitForVisibility(by);
        element.clear();
        element.sendKeys(text);
        logger.info("Cleared and entered text into {}: '{}'", elementName, text);
        ExtentLogger.info("Cleared and entered text into <b>" + elementName + "</b>: " + text);
    }

    protected String getText(By by, String elementName) {
        WebElement element = WaitUtils.waitForVisibility(by);
        String text = element.getText();
        logger.info("Retrieved text from {}: '{}'", elementName, text);
        ExtentLogger.info("Retrieved text from <b>" + elementName + "</b>: " + text);
        return text;
    }

    protected boolean isDisplayed(By by) {
        try {
            return WaitUtils.waitForVisibility(by).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", by);
            return false;
        }
    }
}
