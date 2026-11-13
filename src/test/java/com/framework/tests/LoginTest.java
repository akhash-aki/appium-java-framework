package com.framework.tests;

import com.framework.pages.LoginPage;
import com.framework.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Functional validation suite for the mobile login workflows.
 */
public class LoginTest extends BaseTest {

    @Test(description = "Verify user can log in with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();

        ProductsPage productsPage = loginPage
                .enterUsername("standard_user")
                .enterPassword("secret_sauce")
                .clickLogin();

        Assert.assertTrue(productsPage.isProductsHeaderDisplayed(), "Products catalog header was not displayed!");
        Assert.assertEquals(productsPage.getHeaderTitle(), "PRODUCTS", "Page title mismatch!");
    }

    @Test(description = "Verify error message appears when using invalid password")
    public void testInvalidPasswordShowsError() {
        LoginPage loginPage = new LoginPage();

        loginPage.enterUsername("standard_user")
                .enterPassword("invalid_password")
                .clickLoginExpectingFailure();

        String errorText = loginPage.getErrorMessageText();
        Assert.assertTrue(errorText.contains("Username and password do not match"),
                "Expected error message not found! Actual: " + errorText);
    }

    @Test(description = "Verify validation error when username is left blank")
    public void testEmptyUsernameValidation() {
        LoginPage loginPage = new LoginPage();

        loginPage.enterUsername("")
                .enterPassword("secret_sauce")
                .clickLoginExpectingFailure();

        String errorText = loginPage.getErrorMessageText();
        Assert.assertTrue(errorText.contains("Username is required"),
                "Expected required validation error! Actual: " + errorText);
    }
}
