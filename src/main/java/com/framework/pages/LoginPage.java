package com.framework.pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Page Object representing the Mobile Application Login Screen (e.g. Swag Labs sample app).
 */
public class LoginPage extends BasePage {

    // Locators using AppiumBy (AccessibilityId & XPath fallback)
    private final By usernameField = AppiumBy.accessibilityId("test-Username");
    private final By passwordField = AppiumBy.accessibilityId("test-Password");
    private final By loginButton = AppiumBy.accessibilityId("test-LOGIN");
    private final By errorMessage = AppiumBy.xpath("//*[@content-desc='test-Error message']//android.widget.TextView");

    public LoginPage enterUsername(String username) {
        clearAndSendKeys(usernameField, username, "Username Input");
        return this;
    }

    public LoginPage enterPassword(String password) {
        clearAndSendKeys(passwordField, password, "Password Input");
        return this;
    }

    public ProductsPage clickLogin() {
        click(loginButton, "Login Button");
        return new ProductsPage();
    }

    public LoginPage clickLoginExpectingFailure() {
        click(loginButton, "Login Button");
        return this;
    }

    public String getErrorMessageText() {
        return getText(errorMessage, "Error Message Banner");
    }

    public boolean isLoginButtonVisible() {
        return isDisplayed(loginButton);
    }
}
