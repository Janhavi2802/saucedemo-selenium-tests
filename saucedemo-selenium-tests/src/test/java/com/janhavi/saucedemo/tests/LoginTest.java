package com.janhavi.saucedemo.tests;

import com.janhavi.saucedemo.base.BaseTest;
import com.janhavi.saucedemo.config.ConfigReader;
import com.janhavi.saucedemo.data.TestDataProvider;
import com.janhavi.saucedemo.pages.InventoryPage;
import com.janhavi.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Standard user can log in")
    public void validLoginOpensProductsPage() {
        InventoryPage inventory = loginAsStandardUser();

        Assert.assertTrue(inventory.isLoaded(), "Products page should be shown");
        Assert.assertTrue(inventory.currentUrl().endsWith("/inventory.html"));
        Assert.assertEquals(inventory.getProductCount(), 6);
    }

    @Test(groups = {"regression"}, dataProvider = "invalidLogins", dataProviderClass = TestDataProvider.class,
            description = "Invalid, empty and locked-out credentials show the right error")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        LoginPage page = loginPage.attemptLogin(username, password);

        Assert.assertEquals(page.getErrorMessage(), expectedError);
        Assert.assertTrue(page.isLoginButtonDisplayed(), "User should stay on the login page");
    }

    @Test(groups = {"regression"}, description = "Error banner can be dismissed")
    public void errorMessageCanBeClosed() {
        LoginPage page = loginPage.attemptLogin("", "");
        Assert.assertTrue(page.isErrorDisplayed());

        page.closeError();

        Assert.assertFalse(page.isErrorDisplayed());
    }

    @Test(groups = {"smoke", "regression"}, description = "Logout returns to login page")
    public void logoutReturnsToLoginPage() {
        LoginPage page = loginAsStandardUser().header().logout();

        Assert.assertTrue(page.isLoginButtonDisplayed());
    }

    @Test(groups = {"regression"}, description = "Inventory cannot be opened without logging in")
    public void inventoryRequiresLogin() {
        driver.get(ConfigReader.get("baseUrl") + "inventory.html");

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: You can only access '/inventory.html' when you are logged in.");
    }
}
