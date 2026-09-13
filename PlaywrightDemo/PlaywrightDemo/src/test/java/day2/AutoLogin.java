package day2;

import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class AutoLogin {
  @Test
  public void orangeHRMLogin() throws Exception {
	  
	  Playwright playwright = Playwright.create();  //Starting Playwright Server
		//Browser browser = playwright.chromium().launch();
		Browser browser=playwright.chromium().launch(
				new BrowserType.LaunchOptions().setHeadless(false));
		//For Reading JSON file which has Cookies Details
		BrowserContext brContext=browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("appLogin.json")));
		

		Page page = brContext.newPage();  // 
		page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index");
		Thread.sleep(5000);
		System.out.println(page.title());
		
  }
}