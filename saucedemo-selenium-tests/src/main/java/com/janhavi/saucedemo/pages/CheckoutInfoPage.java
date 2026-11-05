package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: "Your Information". */
public class CheckoutInfoPage extends BasePage {

    private final By pageTitle = By.cssSelector("span.title");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return "Checkout: Your Information".equals(text(pageTitle));
    }

    public CheckoutOverviewPage continueWith(String firstName, String lastName, String postalCode) {
        fill(firstName, lastName, postalCode);
        click(continueButton);
        return new CheckoutOverviewPage(driver);
    }

    /** For negative tests: stays on this page. */
    public CheckoutInfoPage submitExpectingError(String firstName, String lastName, String postalCode) {
        fill(firstName, lastName, postalCode);
        click(continueButton);
        return this;
    }

    private void fill(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
    }

    public String getErrorMessage() {
        return text(errorMessage);
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(driver);
    }
}
