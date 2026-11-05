package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InventoryPage extends BasePage {

    public static final String SORT_NAME_ASC = "az";
    public static final String SORT_NAME_DESC = "za";
    public static final String SORT_PRICE_LOW_HIGH = "lohi";
    public static final String SORT_PRICE_HIGH_LOW = "hilo";

    private final By pageTitle = By.cssSelector("span.title");
    private final By inventoryItem = By.className("inventory_item");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By itemButton = By.tagName("button");
    private final By sortDropdown = By.cssSelector("select[data-test='product-sort-container']");

    private final HeaderComponent header;

    public InventoryPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    public HeaderComponent header() {
        return header;
    }

    public boolean isLoaded() {
        return isDisplayed(pageTitle) && "Products".equals(text(pageTitle));
    }

    public String getTitle() {
        return text(pageTitle);
    }

    public int getProductCount() {
        visible(inventoryItem);
        return driver.findElements(inventoryItem).size();
    }

    public List<String> getProductNames() {
        visible(itemName);
        return driver.findElements(itemName).stream().map(e -> e.getText().trim()).toList();
    }

    public List<Double> getProductPrices() {
        visible(itemPrice);
        return driver.findElements(itemPrice).stream()
                .map(e -> parsePrice(e.getText()))
                .toList();
    }

    public double getPriceOf(String productName) {
        return parsePrice(findItem(productName).findElement(itemPrice).getText());
    }

    public InventoryPage addToCart(String productName) {
        WebElement button = findItem(productName).findElement(itemButton);
        if (!button.getText().equalsIgnoreCase("Add to cart")) {
            throw new IllegalStateException(productName + " is already in the cart");
        }
        button.click();
        return this;
    }

    public InventoryPage removeFromCart(String productName) {
        WebElement button = findItem(productName).findElement(itemButton);
        if (!button.getText().equalsIgnoreCase("Remove")) {
            throw new IllegalStateException(productName + " is not in the cart");
        }
        button.click();
        return this;
    }

    public String getButtonText(String productName) {
        return findItem(productName).findElement(itemButton).getText().trim();
    }

    public InventoryPage sortBy(String sortValue) {
        new Select(visible(sortDropdown)).selectByValue(sortValue);
        return this;
    }

    private WebElement findItem(String productName) {
        visible(inventoryItem);
        return driver.findElements(inventoryItem).stream()
                .filter(item -> item.findElement(itemName).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productName));
    }
}
