package com.framework.tests;

import com.framework.config.ConfigReader;
import com.framework.constants.PlatformType;
import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;
import com.framework.listeners.TestListener;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Objects;

/**
 * Base Test class handling driver session initialization and teardown per test method.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "deviceName"})
    public void setUp(@Optional("") String platformParam, @Optional("") String deviceNameParam) {
        String platformStr = (platformParam != null && !platformParam.trim().isEmpty())
                ? platformParam.trim()
                : ConfigReader.getOrDefault("platform.name", "ANDROID");

        PlatformType platform = PlatformType.valueOf(platformStr.toUpperCase());
        String deviceName = (deviceNameParam != null && !deviceNameParam.trim().isEmpty())
                ? deviceNameParam.trim()
                : null;

        logger.info("Initializing driver session for platform [{}] on thread [{}]", platform, Thread.currentThread().getName());
        AppiumDriver driver = DriverFactory.createDriver(platform, deviceName);
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        AppiumDriver driver = DriverManager.getDriver();
        if (Objects.nonNull(driver)) {
            logger.info("Terminating driver session on thread [{}]", Thread.currentThread().getName());
            try {
                driver.quit();
            } catch (Exception e) {
                logger.warn("Exception while quitting Appium driver session: {}", e.getMessage());
            } finally {
                DriverManager.unload();
            }
        }
    }
}
