package com.janhavi.saucedemo.pages;

import com.janhavi.saucedemo.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorCloseButton = By.cssSelector("button.error-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("baseUrl"));
        visible(loginButton);
        return this;
    }

    /** Happy path: returns the next page. */
    public InventoryPage loginAs(String username, String password) {
        submit(username, password);
        return new InventoryPage(driver);
    }

    /** For negative tests: stays on the login page. */
    public LoginPage attemptLogin(String username, String password) {
        submit(username, password);
        return this;
    }

    private void submit(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return text(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public LoginPage closeError() {
        click(errorCloseButton);
        return this;
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginButton);
    }
}
