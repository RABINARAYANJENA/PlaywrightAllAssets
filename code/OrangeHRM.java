package OrangeHRM;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.BrowserType.LaunchOptions;

public class OrangeHRM {
	Playwright pw;
	Browser bw;
	Page page;
	BrowserContext context;
	ExtentReports extent;
	ExtentTest test;
	
	@BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("Reports/OrangeHRM/ExtentReport.html");
        spark.config().setReportName("OrangeHRM Report");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Windows", "11");
    }
	 
	@BeforeTest
	public void browserSetup() {
		pw = Playwright.create();
		LaunchOptions lo =new LaunchOptions();
		lo.setHeadless(false);
		lo.setChannel("chrome");
		ArrayList<String> arr= new ArrayList<String>();
		arr.add("--start-maximized");
		lo.setArgs(arr);
		bw = pw.chromium().launch(lo);
		context =bw.newContext(new Browser.NewContextOptions().setViewportSize(null));
		page = context.newPage();
	}
	
	@Test
	public void login() throws Exception {
		String testCaseID=ExcelRead.readExcelSuit();
		ExcelRead.readExcelData(testCaseID);
		test= extent.createTest("OrangeHRM Login");
		page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		test.info("Navigate to https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		
		page.waitForSelector("input[name='username']");
		page.getByPlaceholder("Username").fill(ExcelRead.getDataValue("Username"));
		test.info("Enter username : "+ExcelRead.getDataValue("Username"));
		
		page.locator("//input[@name='password']").fill(ExcelRead.getDataValue("Password"));
		test.info("Enter password : "+ ExcelRead.getDataValue("Password"));
		
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Login ")).click();
		test.info("Click on login button");
		
		page.waitForTimeout(5000);
		String currentURL=page.url();
		if(currentURL.contains("dashboard")) {
			test.pass("Successfully login to dashboard page!!!");
		}
		else {
			test.fail("Not successfully login to dashboard page");
			Assert.fail();
		}
	}
	
	@AfterTest
	public void tearDown() {
		bw.close();
		pw.close();
		extent.flush();
	}

}
