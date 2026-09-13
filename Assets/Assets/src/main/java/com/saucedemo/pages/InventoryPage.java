package com.saucedemo.pages;

import com.microsoft.playwright.Page;
import com.saucedemo.base.BaseTest;

public class InventoryPage {
    private final Page page;
    private final BaseTest base;

    public InventoryPage(Page page, BaseTest base) {
        this.page = page;
        this.base = base;
    }

    public void chooseWorkflowProducts() {
        page.click("id=add-to-cart-sauce-labs-backpack");
        base.logStep("Selected Item: Backpack", "INFO");
        page.click("id=add-to-cart-sauce-labs-bolt-t-shirt");
        base.logStep("Selected Item: Bolt T-Shirt", "INFO");
        page.click(".shopping_cart_link");
        base.logStep("Navigated to Shopping Cart Overview", "INFO");
    }
}