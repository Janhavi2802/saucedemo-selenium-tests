package com.janhavi.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Header shared by every page after login: cart icon, badge and side menu. */
public class HeaderComponent extends BasePage {

    private final By cartLink = By.cssSelector("a.shopping_cart_link");
    private final By cartBadge = By.cssSelector("span.shopping_cart_badge");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetAppLink = By.id("reset_sidebar_link");
    private final By closeMenuButton = By.id("react-burger-cross-btn");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    /** 0 when the badge is not shown. */
    public int getCartCount() {
        return isDisplayed(cartBadge) ? Integer.parseInt(text(cartBadge)) : 0;
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(menuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }

    /** Clears the cart server-side so each test starts clean. */
    public void resetAppState() {
        click(menuButton);
        click(resetAppLink);
        click(closeMenuButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(logoutLink));
    }
}
