package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** Checkout step two: order summary with item total, tax and total. */
public class CheckoutOverviewPage extends BasePage {

    private final By pageTitle = By.cssSelector("span.title");
    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Checkout: Overview".equals(text(pageTitle));
    }

    public List<String> getItemNames() {
        return driver.findElements(itemName).stream().map(e -> e.getText().trim()).toList();
    }

    public double sumOfItemPrices() {
        return driver.findElements(itemPrice).stream()
                .mapToDouble(e -> parsePrice(e.getText()))
                .sum();
    }

    public double getSubtotal() {
        return parsePrice(text(subtotalLabel));
    }

    public double getTax() {
        return parsePrice(text(taxLabel));
    }

    public double getTotal() {
        return parsePrice(text(totalLabel));
    }

    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public InventoryPage cancel() {
        click(cancelButton);
        return new InventoryPage(driver);
    }
}
