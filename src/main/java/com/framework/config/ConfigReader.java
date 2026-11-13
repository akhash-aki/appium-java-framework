package com.framework.config;

import com.framework.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            PROPERTIES.load(fis);
            LOGGER.info("Loaded configuration properties from {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            LOGGER.error("Failed to load config.properties from {}", FrameworkConstants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load config.properties. Check file path: " + FrameworkConstants.CONFIG_FILE_PATH, e);
        }
    }

    private ConfigReader() {
        // Prevent instantiation
    }

    public static String get(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        // Priority: JVM System Property -> Config File
        String systemProp = System.getProperty(key);
        if (Objects.nonNull(systemProp) && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        String value = PROPERTIES.getProperty(key);
        return Objects.nonNull(value) ? value.trim() : null;
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return (Objects.nonNull(value) && !value.isEmpty()) ? value : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        try {
            return Objects.nonNull(value) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid integer for key '{}': '{}'. Falling back to default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return Objects.nonNull(value) ? Boolean.parseBoolean(value) : defaultValue;
    }
}
