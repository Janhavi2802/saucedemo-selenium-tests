package com.janhavi.saucedemo.base;

import com.janhavi.saucedemo.config.ConfigReader;
import com.janhavi.saucedemo.driver.DriverFactory;
import com.janhavi.saucedemo.listeners.TestListener;
import com.janhavi.saucedemo.pages.InventoryPage;
import com.janhavi.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Every test gets a fresh browser (clean cart and session) and starts on the
 * login page. alwaysRun = true keeps setup running when suites filter by group.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional String browser) {
        String chosen = (browser == null || browser.isBlank()) ? ConfigReader.get("browser") : browser;
        driver = DriverFactory.initDriver(chosen);
        loginPage = new LoginPage(driver).open();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    /** Shortcut for tests that start after a successful login. */
    protected InventoryPage loginAsStandardUser() {
        return loginPage.loginAs(ConfigReader.get("standardUser"), ConfigReader.get("password"));
    }
}
