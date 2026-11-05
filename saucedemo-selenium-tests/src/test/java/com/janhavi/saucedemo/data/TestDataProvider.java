package com.janhavi.saucedemo.data;

import org.testng.annotations.DataProvider;

/** Test data sets shared across test classes. */
public final class TestDataProvider {

    private TestDataProvider() {
    }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return new Object[][]{
                // username, password, expected error
                {"", "", "Epic sadface: Username is required"},
                {"", "secret_sauce", "Epic sadface: Username is required"},
                {"standard_user", "", "Epic sadface: Password is required"},
                {"standard_user", "wrong_password",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"unknown_user", "secret_sauce",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"STANDARD_USER", "secret_sauce",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"' OR '1'='1", "' OR '1'='1",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"locked_out_user", "secret_sauce",
                        "Epic sadface: Sorry, this user has been locked out."},
        };
    }

    @DataProvider(name = "productSets")
    public static Object[][] productSets() {
        return new Object[][]{
                {new String[]{"Sauce Labs Backpack"}},
                {new String[]{"Sauce Labs Bike Light", "Sauce Labs Onesie"}},
                {new String[]{"Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt", "Sauce Labs Fleece Jacket"}},
                {new String[]{"Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt",
                        "Sauce Labs Fleece Jacket", "Sauce Labs Onesie", "Test.allTheThings() T-Shirt (Red)"}},
        };
    }

    @DataProvider(name = "missingCheckoutInfo")
    public static Object[][] missingCheckoutInfo() {
        return new Object[][]{
                // first, last, postal, expected error
                {"", "Kulkarni", "560001", "Error: First Name is required"},
                {"Janhavi", "", "560001", "Error: Last Name is required"},
                {"Janhavi", "Kulkarni", "", "Error: Postal Code is required"},
                {"", "", "", "Error: First Name is required"},
        };
    }

    @DataProvider(name = "validCheckoutInfo")
    public static Object[][] validCheckoutInfo() {
        return new Object[][]{
                {"Janhavi", "Kulkarni", "560001"},
                {"A", "B", "1"},                                    // minimum length
                {"Jean-Luc", "O'Neil", "SW1A 1AA"},                 // special characters, UK format
                {"जान्हवी", "कुलकर्णी", "५६०००१"},                     // Unicode
        };
    }
}
