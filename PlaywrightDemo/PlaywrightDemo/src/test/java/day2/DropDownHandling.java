package day2;

import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.options.SelectOption;

import utils.BaseClass;

public class DropDownHandling extends BaseClass {
	@Test(enabled = false,priority = 1,description = "This is for Drop DOwn Example")
	public void BlazeTest() throws InterruptedException {
		Page page = browser.newPage();
		page.navigate("https://blazedemo.com/");

		// Using Values
		page.selectOption("select[name='fromPort']", "Boston");

		Thread.sleep(3000);
		// Using Index
		// page.selectOption("select[name='fromPort']", new SelectOption().setIndex(4));

		// Using Label/Visible Text
		// page.selectOption("select[name='fromPort']", new
		// SelectOption().setLabel("Portland"));

		// Using Value Text
		page.selectOption("select[name='fromPort']", new SelectOption().setValue("San Diego"));

		// Thread.sleep(3000);
		page.selectOption("select[name='toPort']", "Rome");

		Thread.sleep(3000);
		page.locator("div.container > input").click();
		Thread.sleep(3000);

		page.locator(".btn.btn-small").nth(2).click();
		Thread.sleep(3000);
		Locator cardType = page.locator("#cardType");

		//System.out.println(cardType.textContent());

		System.out.println(cardType.locator("option").count());

		Locator cardOptions = cardType.locator("option");
		for (int i = 0; i < cardOptions.count(); i++) {
			System.out.println(cardOptions.nth(i).textContent());
		}
		
		page.screenshot(new Page.ScreenshotOptions()
						.setPath(Paths.get("screenshot/abc.png"))
						.setFullPage(false));
		
	}
	
	@Test(priority = 2,description =  "This is for Tab Handling")
	public void f() throws InterruptedException {
		Page page = browser.newPage();
		page.navigate("https://orangehrm.com/");
		
		Locator pp=  page.locator("text=Privacy Policy");
		pp.nth(3).    scrollIntoViewIfNeeded();
		
		Page popUp=page.waitForPopup(()->{
			pp.nth(3).click();
		});
		
		Assert.assertEquals(popUp.title(), "OrangeHRM Service Privacy Policy | OrangeHRM");
		System.out.println(popUp.title());
		System.out.println(popUp.url());
		Thread.sleep(5000);
	}

	
	
	
	
	
}