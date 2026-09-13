package com.saucedemo.pages;

import com.microsoft.playwright.Page;
import com.saucedemo.base.BaseTest;

public class LoginPage {
    private final Page page;
    private final BaseTest base;

    public LoginPage(Page page, BaseTest base) {
        this.page = page;
        this.base = base;
    }

    public void loginToApplication(String username, String password) {
        page.fill("input[placeholder='Username']", username);
        base.logStep("Entered Username", "INFO");
        page.fill("input[name='password']", password);
        base.logStep("Entered Password", "INFO");
        page.click("text=Login");
    }

    public boolean checkDashboardView() {
        page.waitForSelector(".title");
        return page.locator(".title").isVisible();
    }
}