package Playwright_Practice.Playwright_Practice;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

public class Demo {

	public static void main(String[] args) {
		Playwright playwright = Playwright.create();
		boolean headless = false;
		ArrayList<String>a=new ArrayList<String>();
		a.add("--start-maximized");
		Browser bw = playwright.chromium().launch(
			new BrowserType.LaunchOptions()
				.setHeadless(headless)
				.setChannel("chrome")
				.setArgs(a)
		);

		Page page = bw.newPage();
		page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

		page.waitForTimeout(8000); // ✅ wait for page load

		System.out.println("Title:" + page.title());

		// ✅ Dynamic screenshot
		String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		page.screenshot(new Page.ScreenshotOptions()
				.setPath(Paths.get("D:\\RABI_WORKSPACE\\PLAYWRIGHT_DEMO\\Playwright_Practice\\Screenshot\\img_" + timestamp + ".png")));

		// ✅ Locators
		Locator username = page.locator("input[name='username']");
		username.fill("Admin");

		Locator password = page.locator("input[name='password']");
		password.fill("admin123");

		// ✅ Correct login button
		Locator loginButton = page.getByRole(AriaRole.BUTTON, 
			new Page.GetByRoleOptions().setName("Login"));
		loginButton.click();

		bw.close();
		playwright.close();
	}
}