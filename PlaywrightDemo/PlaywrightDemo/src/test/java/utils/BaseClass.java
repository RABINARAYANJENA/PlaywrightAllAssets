package utils;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class BaseClass {
	public Browser browser;
	public Playwright playwright;
	public BrowserContext brContext;

	@BeforeTest // Pre condition
	public void beforeTest() {
		playwright = Playwright.create(); // Starting Playwright Server
		// Browser browser = playwright.chromium().launch();
		browser = playwright.chromium().
		launch(new BrowserType.
				LaunchOptions().setHeadless(false));
	}

	@AfterTest // post condition
	public void afterTest() {
		browser.close();
		playwright.close();
	}
}