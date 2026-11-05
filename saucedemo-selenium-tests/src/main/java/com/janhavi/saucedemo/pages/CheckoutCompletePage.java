package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By pageTitle = By.cssSelector("span.title");
    private final By completeHeader = By.className("complete-header");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Checkout: Complete!".equals(text(pageTitle));
    }

    public String getConfirmationMessage() {
        return text(completeHeader);
    }

    public InventoryPage backHome() {
        click(backHomeButton);
        return new InventoryPage(driver);
    }
}
