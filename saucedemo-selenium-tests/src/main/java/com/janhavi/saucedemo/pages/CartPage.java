package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By pageTitle = By.cssSelector("span.title");
    private final By cartItem = By.className("cart_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemQuantity = By.className("cart_quantity");
    private final By removeButton = By.tagName("button");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Your Cart".equals(text(pageTitle));
    }

    public List<String> getItemNames() {
        return driver.findElements(itemName).stream().map(e -> e.getText().trim()).toList();
    }

    public int getItemCount() {
        return driver.findElements(cartItem).size();
    }

    public int getQuantityOf(String productName) {
        return Integer.parseInt(findItem(productName).findElement(itemQuantity).getText().trim());
    }

    public CartPage remove(String productName) {
        findItem(productName).findElement(removeButton).click();
        return this;
    }

    public CheckoutInfoPage checkout() {
        click(checkoutButton);
        return new CheckoutInfoPage(driver);
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(driver);
    }

    private WebElement findItem(String productName) {
        return driver.findElements(cartItem).stream()
                .filter(item -> item.findElement(itemName).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Not in cart: " + productName));
    }
}
