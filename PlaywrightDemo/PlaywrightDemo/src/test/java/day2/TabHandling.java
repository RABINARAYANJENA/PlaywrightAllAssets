package day2;

import org.testng.annotations.Test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utils.BaseClass;

public class TabHandling extends BaseClass {
	@Test
	public void f() throws InterruptedException {
		Page page = brContext.newPage();
		page.navigate("https://orangehrm.com/");
		
		Locator pp=  page.locator("text=Privacy Policy");
		pp.nth(3).    scrollIntoViewIfNeeded();
		
		Page popUp=page.waitForPopup(()->{
			pp.nth(3).click();
		});
		
		System.out.println(popUp.title());
		System.out.println(popUp.url());
		Thread.sleep(5000);
	}
}
