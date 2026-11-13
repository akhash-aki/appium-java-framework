package com.framework.constants;

import java.io.File;

public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = PROJECT_PATH + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    public static final String CONFIG_FILE_PATH = RESOURCES_PATH + File.separator + "config.properties";

    public static final String REPORT_PATH = PROJECT_PATH + File.separator + "reports";
    public static final String REPORT_FILE_NAME = "index.html";
    public static final int DEFAULT_EXPLICIT_WAIT = 15;
}
