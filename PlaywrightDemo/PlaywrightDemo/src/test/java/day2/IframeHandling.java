package day2;

import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

public class IframeHandling {
	@Test
	public void f() throws InterruptedException {
		Playwright pw = Playwright.create();
		LaunchOptions option =new LaunchOptions();
		option.setChannel("msedge");
		option.setHeadless(false);
		//Browser browser=pw.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
		Browser browser=pw.chromium().launch(option);
		BrowserContext context = browser.newContext();

		Page page=context.newPage();
		page.navigate("file:///D:/Users/Premchand.Vishwakarm/Desktop/Training/HTML/iframe.html");
		
		FrameLocator iframe=page.frameLocator("iframe[name='hq']");
		iframe.locator("span.navbar-toggler-icon").click();
		Thread.sleep(8000);
		browser.close();
		pw.close();
	}
}