package com.framework.driver;

import io.appium.java_client.AppiumDriver;

import java.util.Objects;

/**
 * Thread-safe container managing AppiumDriver instances across parallel execution threads.
 */
public final class DriverManager {

    private static final ThreadLocal<AppiumDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    public static AppiumDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(AppiumDriver driver) {
        if (Objects.nonNull(driver)) {
            DRIVER_THREAD_LOCAL.set(driver);
        }
    }

    public static void unload() {
        DRIVER_THREAD_LOCAL.remove();
    }
}
