package com.framework.utils;

import com.framework.driver.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Mobile gestures built using the W3C Actions API (compatible with Appium 2.x and Java Client 9+).
 * Replaces legacy and deprecated TouchAction APIs.
 */
public final class GestureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestureUtils.class);

    public enum SwipeDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private GestureUtils() {
        // Prevent instantiation
    }

    /**
     * Executes a swipe gesture relative to screen dimensions.
     *
     * @param direction Direction to swipe
     * @param percent   Percentage of the screen dimension to cover (e.g. 0.5 for 50%)
     */
    public static void swipe(SwipeDirection direction, double percent) {
        AppiumDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();

        int width = size.getWidth();
        int height = size.getHeight();

        int startX = width / 2;
        int startY = height / 2;
        int endX = startX;
        int endY = startY;

        switch (direction) {
            case UP:
                startY = (int) (height * 0.75);
                endY = (int) (height * (0.75 - percent));
                break;
            case DOWN:
                startY = (int) (height * 0.25);
                endY = (int) (height * (0.25 + percent));
                break;
            case LEFT:
                startX = (int) (width * 0.85);
                endX = (int) (width * (0.85 - percent));
                break;
            case RIGHT:
                startX = (int) (width * 0.15);
                endX = (int) (width * (0.15 + percent));
                break;
        }

        performW3CSwipe(startX, startY, endX, endY, Duration.ofMillis(600));
        LOGGER.info("Performed swipe {} with distance percent {}", direction, percent);
    }

    /**
     * Low-level W3C Actions drag / swipe from (startX, startY) to (endX, endY).
     */
    public static void performW3CSwipe(int startX, int startY, int endX, int endY, Duration duration) {
        AppiumDriver driver = DriverManager.getDriver();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    /**
     * Tap at specified coordinates.
     */
    public static void tap(int x, int y) {
        AppiumDriver driver = DriverManager.getDriver();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tapSeq = new Sequence(finger, 1);

        tapSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tapSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tapSeq.addAction(new Pause(finger, Duration.ofMillis(100)));
        tapSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tapSeq));
        LOGGER.info("Performed tap at ({}, {})", x, y);
    }

    /**
     * Long press on a mobile element using W3C Actions.
     */
    public static void longPress(WebElement element, Duration pressDuration) {
        AppiumDriver driver = DriverManager.getDriver();
        Point location = element.getLocation();
        Dimension size = element.getSize();

        int centerX = location.getX() + (size.getWidth() / 2);
        int centerY = location.getY() + (size.getHeight() / 2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPressSeq = new Sequence(finger, 1);

        longPressSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        longPressSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPressSeq.addAction(new Pause(finger, pressDuration));
        longPressSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPressSeq));
        LOGGER.info("Performed long press on element at ({}, {}) for {}ms", centerX, centerY, pressDuration.toMillis());
    }

    /**
     * Android specific UiScrollable helper to find and scroll to element by visible text.
     */
    public static WebElement scrollToTextAndroid(String visibleText) {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver) {
            String uiAutomatorScroll = "new UiScrollable(new UiSelector().scrollable(true))" +
                    ".scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\"))";
            return driver.findElement(AppiumBy.androidUIAutomator(uiAutomatorScroll));
        } else {
            throw new UnsupportedOperationException("scrollToTextAndroid is only supported on AndroidDriver");
        }
    }
}
