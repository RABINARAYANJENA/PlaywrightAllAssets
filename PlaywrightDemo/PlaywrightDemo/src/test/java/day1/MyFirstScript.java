package day1;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class MyFirstScript {

	public static void main(String[] args) throws InterruptedException {
		Playwright playwright = Playwright.create();  //Starting Playwright Server
		//Browser browser = playwright.chromium().launch();
		Browser browser=playwright.chromium().launch(
				new BrowserType.LaunchOptions().setHeadless(false));
		

		Page page = browser.newPage();  // 
		page.navigate("https://google.com");
		System.out.println(page.title());
		
		page.locator(".gLFyf").fill("http://magneticautomations.in");
		//Locator submit=page.locator("input[type='submit']");
		//submit.nth(2).click();
		page.locator("text='Google Search'").nth(1).click();
		
		Thread.sleep(15000);	
		browser.close();
		playwright.close();
	}
}