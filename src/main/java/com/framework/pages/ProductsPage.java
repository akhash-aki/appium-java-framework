package com.framework.pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Page Object representing the Products Inventory Catalog after successful authentication.
 */
public class ProductsPage extends BasePage {

    private final By productsHeader = AppiumBy.xpath("//android.widget.TextView[@text='PRODUCTS']");
    private final By cartButton = AppiumBy.accessibilityId("test-Cart");

    public boolean isProductsHeaderDisplayed() {
        return isDisplayed(productsHeader);
    }

    public String getHeaderTitle() {
        return getText(productsHeader, "Products Page Header");
    }

    public boolean isCartButtonDisplayed() {
        return isDisplayed(cartButton);
    }
}
