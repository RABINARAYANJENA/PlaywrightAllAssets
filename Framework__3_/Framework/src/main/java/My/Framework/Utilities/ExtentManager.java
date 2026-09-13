package My.Framework.Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Thread-safe initialization of ExtentReports instance
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("Reports/OrangeHRM_Logs/ExtentReport.html");
            spark.config().setReportName("OrangeHRM Report");
            
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Automation Engineer Name", "Rabi Narayan Jena");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    // Crucial for framework-level architectures to prevent memory leaks in parallel runs
    public static void unload() {
        extentTest.remove();
    }
}