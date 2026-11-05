package com.janhavi.saucedemo.tests;

import com.janhavi.saucedemo.base.BaseTest;
import com.janhavi.saucedemo.data.TestDataProvider;
import com.janhavi.saucedemo.pages.CartPage;
import com.janhavi.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class CartTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    @Test(groups = {"smoke", "regression"}, description = "Adding a product updates badge and button")
    public void addSingleProductToCart() {
        InventoryPage inventory = loginAsStandardUser().addToCart(BACKPACK);

        Assert.assertEquals(inventory.header().getCartCount(), 1);
        Assert.assertEquals(inventory.getButtonText(BACKPACK), "Remove");
    }

    @Test(groups = {"regression"}, dataProvider = "productSets", dataProviderClass = TestDataProvider.class,
            description = "Cart shows exactly the products that were added")
    public void cartContainsAddedProducts(String[] products) {
        InventoryPage inventory = loginAsStandardUser();
        for (String product : products) {
            inventory.addToCart(product);
        }
        Assert.assertEquals(inventory.header().getCartCount(), products.length);

        CartPage cart = inventory.header().openCart();

        Assert.assertTrue(cart.isLoaded());
        Assert.assertEqualsNoOrder(cart.getItemNames().toArray(), products);
        for (String product : products) {
            Assert.assertEquals(cart.getQuantityOf(product), 1);
        }
    }

    @Test(groups = {"regression"}, description = "Removing from the products page updates the badge")
    public void removeProductFromInventoryPage() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT)
                .removeFromCart(BACKPACK);

        Assert.assertEquals(inventory.header().getCartCount(), 1);
        Assert.assertEquals(inventory.getButtonText(BACKPACK), "Add to cart");
    }

    @Test(groups = {"regression"}, description = "Removing the last item from the cart page empties the cart")
    public void removeProductFromCartPage() {
        CartPage cart = loginAsStandardUser().addToCart(BACKPACK).header().openCart();

        cart.remove(BACKPACK);

        Assert.assertEquals(cart.getItemCount(), 0);
        Assert.assertEquals(new InventoryPage(driver).header().getCartCount(), 0);
    }

    @Test(groups = {"regression"}, description = "Cart keeps its items after continue shopping")
    public void cartPersistsAfterContinueShopping() {
        InventoryPage inventory = loginAsStandardUser().addToCart(BACKPACK).header().openCart().continueShopping();

        inventory.addToCart(BIKE_LIGHT);
        List<String> items = inventory.header().openCart().getItemNames();

        Assert.assertEqualsNoOrder(items.toArray(), Arrays.asList(BACKPACK, BIKE_LIGHT).toArray());
    }

    @Test(groups = {"regression"}, description = "Cart survives logout and login for the same user")
    public void cartPersistsAcrossLogin() {
        loginAsStandardUser().addToCart(BACKPACK).header().logout();

        InventoryPage inventory = loginAsStandardUser();

        Assert.assertEquals(inventory.header().getCartCount(), 1);
    }
}
