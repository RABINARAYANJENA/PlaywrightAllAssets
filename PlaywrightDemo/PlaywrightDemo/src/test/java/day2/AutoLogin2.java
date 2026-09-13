package day2;

import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class AutoLogin2 {
  @Test
  public void orangeHRMLogin() {
	  
	  Playwright playwright = Playwright.create();  //Starting Playwright Server
		//Browser browser = playwright.chromium().launch();
		Browser browser=playwright.chromium().launch(
				new BrowserType.LaunchOptions().setHeadless(false));
		
		BrowserContext brContext=browser.newContext();
		

		Page page = brContext.newPage();  // 
		page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		System.out.println(page.title());
		page.getByPlaceholder("Username").fill("admin");
		page.getByPlaceholder("Password").fill("admin123");
		page.getByRole(AriaRole.BUTTON,  new Page.GetByRoleOptions().setName("Login"))
			    .click();  
		
		//Storing Current Browser Details like Cookies & Session details
		brContext.storageState(
				new BrowserContext.StorageStateOptions().setPath(Paths.get("appLogin.json")));
		
  }
}