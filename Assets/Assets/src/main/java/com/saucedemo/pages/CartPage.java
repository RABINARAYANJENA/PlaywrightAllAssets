package com.saucedemo.pages;

import com.microsoft.playwright.Page;
import com.saucedemo.base.BaseTest;

public class CartPage {
    private final Page page;
    private final BaseTest base;

    public CartPage(Page page, BaseTest base) {
        this.page = page;
        this.base = base;
    }

    public void proceedToCheckout() {
        page.click("id=checkout");
        base.logStep("Clicked Checkout Button", "INFO");
    }
}