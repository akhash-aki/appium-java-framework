package com.framework.driver;

import com.framework.config.ConfigReader;
import com.framework.constants.PlatformType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

/**
 * Factory class responsible for initializing AppiumDriver sessions for Android and iOS
 * using Appium 2.x specific Options classes (UiAutomator2Options, XCUITestOptions).
 */
public final class DriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Prevent instantiation
    }

    public static AppiumDriver createDriver(PlatformType platform, String deviceNameOverride) {
        String serverUrl = ConfigReader.getOrDefault("appium.server.url", "http://127.0.0.1:4723");
        URL url;
        try {
            url = URI.create(serverUrl).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Appium Server URL provided: " + serverUrl, e);
        }

        AppiumDriver driver;
        switch (platform) {
            case ANDROID:
                driver = createAndroidDriver(url, deviceNameOverride);
                break;
            case IOS:
                driver = createIOSDriver(url, deviceNameOverride);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform type: " + platform);
        }

        LOGGER.info("Successfully established {} session on device: {}", platform, deviceNameOverride != null ? deviceNameOverride : "Default");
        return driver;
    }

    private static AndroidDriver createAndroidDriver(URL serverUrl, String deviceNameOverride) {
        UiAutomator2Options options = new UiAutomator2Options();

        String deviceName = (deviceNameOverride != null && !deviceNameOverride.isEmpty())
                ? deviceNameOverride
                : ConfigReader.getOrDefault("android.device.name", "Android Emulator");
        options.setDeviceName(deviceName);
        options.setAutomationName(ConfigReader.getOrDefault("android.automation.name", "UiAutomator2"));

        String appPath = ConfigReader.get("android.app.path");
        if (appPath != null && new File(appPath).exists()) {
            options.setApp(new File(appPath).getAbsolutePath());
        } else {
            // Alternatively launch existing installed app via package/activity
            String appPackage = ConfigReader.get("android.app.package");
            String appActivity = ConfigReader.get("android.app.activity");
            if (appPackage != null && appActivity != null) {
                options.setAppPackage(appPackage);
                options.setAppActivity(appActivity);
            }
        }

        options.setAutoGrantPermissions(ConfigReader.getBoolean("android.auto.grant.permissions", true));
        options.setNoReset(ConfigReader.getBoolean("android.no.reset", false));
        options.setNewCommandTimeout(Duration.ofSeconds(120));

        LOGGER.info("Configured UiAutomator2Options: {}", options.toJson());
        return new AndroidDriver(serverUrl, options);
    }

    private static IOSDriver createIOSDriver(URL serverUrl, String deviceNameOverride) {
        XCUITestOptions options = new XCUITestOptions();

        String deviceName = (deviceNameOverride != null && !deviceNameOverride.isEmpty())
                ? deviceNameOverride
                : ConfigReader.getOrDefault("ios.device.name", "iPhone Simulator");
        options.setDeviceName(deviceName);
        options.setPlatformVersion(ConfigReader.getOrDefault("ios.platform.version", "17.0"));
        options.setAutomationName(ConfigReader.getOrDefault("ios.automation.name", "XCUITest"));

        String appPath = ConfigReader.get("ios.app.path");
        if (appPath != null && new File(appPath).exists()) {
            options.setApp(new File(appPath).getAbsolutePath());
        } else {
            String bundleId = ConfigReader.get("ios.bundle.id");
            if (bundleId != null) {
                options.setBundleId(bundleId);
            }
        }

        options.setNoReset(ConfigReader.getBoolean("ios.no.reset", false));
        options.setNewCommandTimeout(Duration.ofSeconds(120));

        LOGGER.info("Configured XCUITestOptions: {}", options.toJson());
        return new IOSDriver(serverUrl, options);
    }
}
