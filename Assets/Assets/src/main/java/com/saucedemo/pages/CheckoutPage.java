package com.saucedemo.pages;

import com.microsoft.playwright.Page;
import com.saucedemo.base.BaseTest;

public class CheckoutPage {
    private final Page page;
    private final BaseTest base;

    public CheckoutPage(Page page, BaseTest base) {
        this.page = page;
        this.base = base;
    }

    public void inputCustomerInformation(String fName, String lName, String zip) {
        page.fill("input[placeholder='First Name']", fName);
        page.fill("input[placeholder='Last Name']", lName);
        page.fill("input[placeholder='Zip/Postal Code']", zip);
        base.logStep("Populated Customer Shipping Address fields", "INFO");
        page.click("id=continue");
        base.logStep("Clicked Continue", "INFO");
    }

    public void completeOrderTransaction() {
        page.click("id=finish");
        base.logStep("Clicked Finish to Process Checkout Order", "INFO");
    }
}