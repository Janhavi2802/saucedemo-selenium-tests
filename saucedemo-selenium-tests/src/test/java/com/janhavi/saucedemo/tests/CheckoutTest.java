package com.janhavi.saucedemo.tests;

import com.janhavi.saucedemo.base.BaseTest;
import com.janhavi.saucedemo.data.TestDataProvider;
import com.janhavi.saucedemo.pages.CartPage;
import com.janhavi.saucedemo.pages.CheckoutCompletePage;
import com.janhavi.saucedemo.pages.CheckoutInfoPage;
import com.janhavi.saucedemo.pages.CheckoutOverviewPage;
import com.janhavi.saucedemo.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String JACKET = "Sauce Labs Fleece Jacket";
    private static final double DELTA = 0.01;

    private CheckoutInfoPage goToCheckoutWith(String... products) {
        InventoryPage inventory = loginAsStandardUser();
        for (String product : products) {
            inventory.addToCart(product);
        }
        return inventory.header().openCart().checkout();
    }

    @Test(groups = {"smoke", "regression"}, description = "End-to-end purchase of one product")
    public void completeCheckoutEndToEnd() {
        CheckoutOverviewPage overview = goToCheckoutWith(BACKPACK)
                .continueWith("Janhavi", "Kulkarni", "560001");
        Assert.assertTrue(overview.isLoaded());

        CheckoutCompletePage complete = overview.finish();

        Assert.assertTrue(complete.isLoaded());
        Assert.assertEquals(complete.getConfirmationMessage(), "Thank you for your order!");
        Assert.assertEquals(complete.backHome().header().getCartCount(), 0, "Cart should be empty after order");
    }

    @Test(groups = {"regression"}, description = "Item total, tax and total are calculated correctly")
    public void orderTotalsAreCorrect() {
        InventoryPage inventory = loginAsStandardUser();
        double expectedSubtotal = inventory.getPriceOf(BACKPACK) + inventory.getPriceOf(JACKET);
        inventory.addToCart(BACKPACK).addToCart(JACKET);

        CheckoutOverviewPage overview = inventory.header().openCart().checkout()
                .continueWith("Janhavi", "Kulkarni", "560001");

        Assert.assertEquals(overview.getSubtotal(), expectedSubtotal, DELTA);
        Assert.assertEquals(overview.sumOfItemPrices(), expectedSubtotal, DELTA);
        Assert.assertEquals(overview.getTax(), Math.round(expectedSubtotal * 0.08 * 100) / 100.0, DELTA,
                "Tax should be 8% of item total");
        Assert.assertEquals(overview.getTotal(), overview.getSubtotal() + overview.getTax(), DELTA);
    }

    @Test(groups = {"regression"}, dataProvider = "validCheckoutInfo", dataProviderClass = TestDataProvider.class,
            description = "Checkout accepts different valid name and postal code formats")
    public void checkoutAcceptsValidInfo(String firstName, String lastName, String postalCode) {
        CheckoutOverviewPage overview = goToCheckoutWith(BACKPACK)
                .continueWith(firstName, lastName, postalCode);

        Assert.assertTrue(overview.isLoaded());
    }

    @Test(groups = {"regression"}, dataProvider = "missingCheckoutInfo", dataProviderClass = TestDataProvider.class,
            description = "Missing checkout fields show the right validation error")
    public void checkoutRequiresAllFields(String firstName, String lastName, String postalCode, String expectedError) {
        CheckoutInfoPage info = goToCheckoutWith(BACKPACK)
                .submitExpectingError(firstName, lastName, postalCode);

        Assert.assertEquals(info.getErrorMessage(), expectedError);
        Assert.assertTrue(info.isLoaded(), "User should stay on the information page");
    }

    @Test(groups = {"regression"}, description = "Cancel on the information page returns to the cart")
    public void cancelCheckoutKeepsCart() {
        CartPage cart = goToCheckoutWith(BACKPACK, JACKET).cancel();

        Assert.assertTrue(cart.isLoaded());
        Assert.assertEquals(cart.getItemCount(), 2);
    }

    @Test(groups = {"regression"}, description = "Cancel on the overview page keeps items in the cart")
    public void cancelOnOverviewKeepsItems() {
        InventoryPage inventory = goToCheckoutWith(BACKPACK)
                .continueWith("Janhavi", "Kulkarni", "560001")
                .cancel();

        Assert.assertTrue(inventory.isLoaded());
        Assert.assertEquals(inventory.header().getCartCount(), 1);
    }
}
