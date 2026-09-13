package My.Framework.Utilities;

import My.Framework.Base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Automatically start the report using the execution method name
        ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Object currentClass = result.getInstance();
        String screenshot = ((BaseTest) currentClass).captureScreenshotBase64();
        
        ExtentManager.getTest().pass("Test Case Passed Successfully");
        
        // Dynamic HTML: Pops into a new blank window on click with a solid Green Border (#2ECC71)
        String passHtml = "<a href=\"javascript:void(0);\" onclick=\"var w=window.open();w.document.write('<img src=\\'"
                + screenshot + "\\'/>');w.document.close();\">" 
                + "<img src='" + screenshot
                + "' style='height:80px; width:120px; border:2px solid #2ECC71; border-radius:0px; object-fit:contain; cursor:pointer;'/>"
                + "</a>";
        
        ExtentManager.getTest().pass(passHtml);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        String screenshot = ((BaseTest) currentClass).captureScreenshotBase64();
        
        ExtentManager.getTest().fail("Test Case Failed: " + result.getThrowable().getMessage());
        
        // Dynamic HTML: Pops into a new blank window on click with a solid Red Border (#E74C3C)
        String failHtml = "<a href=\"javascript:void(0);\" onclick=\"var w=window.open();w.document.write('<img src=\\'"
                + screenshot + "\\'/>');w.document.close();\">" 
                + "<img src='" + screenshot
                + "' style='height:80px; width:120px; border:2px solid #E74C3C; border-radius:0px; object-fit:contain; cursor:pointer;'/>"
                + "</a>";
        
        ExtentManager.getTest().fail(failHtml);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().skip("Test Case Skipped: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (ExtentManager.getInstance() != null) {
            ExtentManager.getInstance().flush();
            System.out.println("Extent Report Generated Successfully with Custom UI Enhancements");
        }
    }
}