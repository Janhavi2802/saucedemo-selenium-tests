package com.janhavi.saucedemo.tests;

import com.janhavi.saucedemo.base.BaseTest;
import com.janhavi.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Sort by name A to Z")
    public void sortByNameAscending() {
        List<String> names = loginAsStandardUser().sortBy(InventoryPage.SORT_NAME_ASC).getProductNames();

        List<String> expected = new ArrayList<>(names);
        Collections.sort(expected);
        Assert.assertEquals(names, expected);
    }

    @Test(groups = {"regression"}, description = "Sort by name Z to A")
    public void sortByNameDescending() {
        List<String> names = loginAsStandardUser().sortBy(InventoryPage.SORT_NAME_DESC).getProductNames();

        List<String> expected = new ArrayList<>(names);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(names, expected);
    }

    @Test(groups = {"regression"}, description = "Sort by price low to high")
    public void sortByPriceLowToHigh() {
        List<Double> prices = loginAsStandardUser().sortBy(InventoryPage.SORT_PRICE_LOW_HIGH).getProductPrices();

        List<Double> expected = new ArrayList<>(prices);
        Collections.sort(expected);
        Assert.assertEquals(prices, expected);
    }

    @Test(groups = {"regression"}, description = "Sort by price high to low")
    public void sortByPriceHighToLow() {
        List<Double> prices = loginAsStandardUser().sortBy(InventoryPage.SORT_PRICE_HIGH_LOW).getProductPrices();

        List<Double> expected = new ArrayList<>(prices);
        expected.sort(Comparator.reverseOrder());
        Assert.assertEquals(prices, expected);
    }

    @Test(groups = {"regression"}, description = "Every product has a name and a positive price")
    public void allProductsHaveNameAndPrice() {
        InventoryPage inventory = loginAsStandardUser();

        inventory.getProductNames().forEach(name -> Assert.assertFalse(name.isBlank()));
        inventory.getProductPrices().forEach(price -> Assert.assertTrue(price > 0, "Price " + price));
    }
}
