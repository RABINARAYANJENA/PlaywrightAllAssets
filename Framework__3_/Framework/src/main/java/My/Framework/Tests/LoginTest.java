package My.Framework.Tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import My.Framework.Base.BaseTest;
import My.Framework.Pages.DashboardPage;
import My.Framework.Pages.LoginPage;
import My.Framework.Utilities.TestListener; // Make sure to include your TestListener class
import My.Framework.Utilities.ExcelRead;

@Listeners(TestListener.class)
public class LoginTest extends BaseTest {

    @Test(description = "Verify user is able to log in with valid credentials via Excel data")
    public void verifyUserCanLoginWithValidCredentials() throws Exception {
        
        // 1. Fetch External Test Data from Excel
        String testCaseID = ExcelRead.readExcelSuit();
        ExcelRead.readExcelData(testCaseID);

        // 2. Initialize Page Objects passing the ThreadLocal Page instance
        LoginPage loginPage = new LoginPage(getPage());
        DashboardPage dashboardPage = new DashboardPage(getPage());

        // 3. UI Actions & Step Logging
        loginPage.navigateTo("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        logStep("Navigated to OrangeHRM Login Page");

        loginPage.enterUsername(ExcelRead.getDataValue("Username"));
        logStep("Entered Username");

        loginPage.enterPassword(ExcelRead.getDataValue("Password"));
        logStep("Entered Password");

        loginPage.clickLogin();
        logStep("Clicked Login Button");

        // 4. Verification Assertion
        boolean isLoginSuccess = dashboardPage.isDashboardLoaded();
        
        // Industry-standard TestNG assertion (cleaner than manually throwing exceptions)
        Assert.assertTrue(isLoginSuccess, "Login failed - Dashboard page layout did not load as expected.");
    }
}