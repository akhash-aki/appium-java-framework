# Enterprise Appium 2.x Java Mobile Automation Framework

A production-grade mobile test automation framework engineered using **Java 17**, **Appium 2.x (Java Client 9.x)**, **Selenium 4**, **TestNG**, and **ExtentReports 5**. Built following the **Page Object Model (POM)** and **ThreadLocal Driver Management** to enable seamless, thread-safe parallel test execution across Android and iOS emulators, simulators, and physical devices.

---

## 🏗 Framework Architecture & Directory Structure

```text
appium-java-framework/
├── pom.xml                                      # Maven build file with Appium 2.x & TestNG dependencies
├── README.md                                    # Framework documentation and quick start guide
├── src/
│   ├── main/
│   │   ├── java/com/framework/
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java            # Thread-safe properties loader (supports CLI overrides)
│   │   │   ├── constants/
│   │   │   │   ├── FrameworkConstants.java      # Static paths, timeout defaults, report locations
│   │   │   │   └── PlatformType.java            # Platform enum (ANDROID, IOS)
│   │   │   ├── driver/
│   │   │   │   ├── DriverFactory.java           # Appium 2.x Options (UiAutomator2 & XCUITest) factory
│   │   │   │   └── DriverManager.java           # ThreadLocal<AppiumDriver> container for concurrency
│   │   │   ├── listeners/
│   │   │   │   └── TestListener.java            # TestNG lifecycle listener (ExtentReports + screenshots)
│   │   │   ├── pages/
│   │   │   │   ├── BasePage.java                # Core page abstraction (waits, clicks, typing, assertions)
│   │   │   │   ├── LoginPage.java               # Page Object for Mobile Authentication
│   │   │   │   └── ProductsPage.java            # Page Object for Products Inventory Catalog
│   │   │   ├── reports/
│   │   │   │   ├── ExtentLogger.java            # Helper to log passes, failures, and base64 screenshots
│   │   │   │   └── ExtentReportManager.java     # Thread-safe ExtentReports 5 SparkReporter manager
│   │   │   └── utils/
│   │   │       ├── GestureUtils.java            # Modern W3C Actions API (swipes, taps, scrolls, pinches)
│   │   │       ├── ScreenshotUtils.java         # Base64 and file screenshot capture utilities
│   │   │       └── WaitUtils.java               # Explicit WebDriverWait wrappers
│   │   └── resources/
│   │       ├── config.properties                # Centralized configuration (server URL, caps, timeouts)
│   │       └── logback.xml                      # Structured console and file logging configuration
│   └── test/
│       ├── java/com/framework/tests/
│       │   ├── BaseTest.java                    # Session lifecycle setup/teardown (@BeforeMethod/@AfterMethod)
│       │   └── LoginTest.java                   # Sample functional test suite validating login workflows
│       └── resources/
│           ├── apps/                            # Directory for mobile application binaries (.apk / .app)
│           └── suites/
│               ├── testng.xml                   # Default suite configuration (parallel class execution)
│               ├── testng-android.xml           # Android targeted execution suite
│               └── testng-ios.xml               # iOS targeted execution suite
└── reports/                                     # Generated HTML test execution reports and screenshots
```

---

## 🚀 Key Architectural Highlights

### 1. Appium 2.x & Java Client 9.x Ready
- **Decoupled Drivers**: Leverages `UiAutomator2Options` for Android and `XCUITestOptions` for iOS, fully deprecating obsolete `DesiredCapabilities`.
- **W3C Actions API**: Replaces deprecated `TouchAction` / `MultiTouchAction` with standard W3C `PointerInput` sequences in `GestureUtils.java` for all scroll, swipe, and tap operations.

### 2. Thread-Safe Parallel Execution
- Utilizes `ThreadLocal<AppiumDriver>` in `DriverManager.java` and `ThreadLocal<ExtentTest>` in `ExtentReportManager.java`.
- Guarantees complete isolation across concurrent tests on multiple emulators or devices without state collisions.

### 3. Dynamic Configuration & CLI Overrides
- Reads from `src/main/resources/config.properties`.
- Allows instant runtime command-line overrides via JVM system properties:
  ```bash
  mvn clean test -Dplatform.name=ANDROID -Dandroid.device.name="Pixel_8_Pro"
  ```

### 4. Rich HTML ExtentReports with Base64 Failure Screenshots
- Generates a standalone, dark-themed HTML report at `reports/index.html`.
- Test failures automatically capture and embed Base64 screenshots directly into the HTML file, avoiding broken relative image links.

---

## 📋 Prerequisites & Setup

### 1. Install System Dependencies
- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.8 or higher
- **Node.js**: Version 18 or higher (LTS)
- **Android SDK & Command-line Tools** (configured via `ANDROID_HOME`)
- **Xcode & Xcode Command Line Tools** (macOS only, for iOS testing)

### 2. Install Appium 2.x and Required Drivers
```bash
# Install Appium globally
npm install -g appium

# Verify Appium installation
appium --version

# Install Android UiAutomator2 Driver
appium driver install uiautomator2

# Install iOS XCUITest Driver (macOS only)
appium driver install xcuitest

# Verify installed drivers
appium driver list --installed
```

### 3. Start Appium Server
```bash
appium --address 127.0.0.1 --port 4723
```

---

## ⚙️ Running Automated Tests

### 1. Default Suite Run
```bash
mvn clean test
```

### 2. Target Specific Suites via Maven
```bash
# Run Android Suite
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/testng-android.xml

# Run iOS Suite
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/testng-ios.xml
```

### 3. Run with Dynamic Device and Platform Overrides
```bash
mvn clean test \
  -Dplatform=ANDROID \
  -Dandroid.device.name="Samsung_Galaxy_S24" \
  -Dappium.server.url="http://127.0.0.1:4723"
```

---

## 📊 Viewing Test Reports
After test execution completes, open the interactive report in any web browser:
```bash
open reports/index.html   # On macOS
xdg-open reports/index.html # On Linux
start reports/index.html  # On Windows
```
