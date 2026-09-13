package com.saucedemo.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    public Page page;
    
    protected static ExtentReports extent;
    public ExtentTest test;
    protected static Properties config;
    
    protected static String currentTimestampRun;
    protected static String standaloneReportsDir;
    protected static String standaloneVideosDir;
    protected static String standaloneTracesDir;

    @BeforeSuite
    public void initAutomationSuite() throws IOException {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            config.load(fis);
        }

        currentTimestampRun = "Run_" + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        
        standaloneReportsDir = "reports/" + currentTimestampRun + "/";
        standaloneVideosDir = "videos/" + currentTimestampRun + "/";
        standaloneTracesDir = "traces/" + currentTimestampRun + "/";

        new File(standaloneReportsDir).mkdirs();
        if (Boolean.parseBoolean(config.getProperty("video.recording.enabled"))) new File(standaloneVideosDir).mkdirs();
        if (Boolean.parseBoolean(config.getProperty("playwright.tracing.enabled"))) new File(standaloneTracesDir).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(standaloneReportsDir + "SauceDemoGraphicalReport.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Separated Infrastructure Dashboard");
        spark.config().setReportName("Visual Automation Run Logs - Env: " + config.getProperty("execution.environment"));

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Automation Engineer Name", "Rabi Narayan Jena");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    public void launchExecutionBrowser(String browserType, String headless) {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Boolean.parseBoolean(headless))
                .setChannel(browserType.toLowerCase());
        
        ArrayList<String> args = new ArrayList<>();
        args.add("--start-maximized");
        options.setArgs(args);

        browser = playwright.chromium().launch(options);
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions().setViewportSize(null);
        
        if (Boolean.parseBoolean(config.getProperty("video.recording.enabled"))) {
            contextOptions.setRecordVideoDir(Paths.get(standaloneVideosDir)).setRecordVideoSize(1280, 720);
        }

        context = browser.newContext(contextOptions);

        if (Boolean.parseBoolean(config.getProperty("playwright.tracing.enabled"))) {
            context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        }

        page = context.newPage();
        page.setDefaultTimeout(Double.parseDouble(config.getProperty("timeout.page.load")));
    }

    public void logStep(String description, String status) {
        if (!Boolean.parseBoolean(config.getProperty("screenshot.on.action")) && status.equalsIgnoreCase("INFO")) {
            test.info(description);
            return;
        }

        byte[] buffer = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        String base64Img = Base64.getEncoder().encodeToString(buffer);

        String color = status.equalsIgnoreCase("PASS") ? "#2ecc71" : 
                       status.equalsIgnoreCase("FAIL") ? "#e74c3c" : "#3498db";

        String htmlElement = "<div style='margin-top:10px; font-weight:bold; color:#ecf0f1;'> " + description + "</div>"
                + "<a href='data:image/png;base64," + base64Img + "' target='_blank'>"
                + "<img src='data:image/png;base64," + base64Img + "' "
                + "style='border: 3px solid " + color + "; max-width: 160px; margin-top: 5px; border-radius: 6px; cursor: pointer;' />"
                + "</a><br/><small style='color:#bdc3c7;'>➔ Click action screenshot to expand</small>";

        if (status.equalsIgnoreCase("FAIL")) test.fail(htmlElement);
        else if (status.equalsIgnoreCase("PASS")) test.pass(htmlElement);
        else test.info(htmlElement);
    }

    public void endContextSession(String testID, ITestResult result) {
        String uniqueIterationAppend = testID + "_" + System.currentTimeMillis();
        
        if (result.getStatus() == ITestResult.FAILURE) {
            logStep("CRITICAL EXECUTION INTERRUPTION DETECTED", "FAIL");
        }

        if (Boolean.parseBoolean(config.getProperty("playwright.tracing.enabled"))) {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(standaloneTracesDir + uniqueIterationAppend + ".zip")));
        }
        
        context.close();
        browser.close();
        playwright.close();

        if (page.video() != null && Boolean.parseBoolean(config.getProperty("video.recording.enabled"))) {
            String rawVideoName = Paths.get(page.video().path().toString()).getFileName().toString();
            String relativePathToVideo = "../../videos/" + currentTimestampRun + "/" + rawVideoName;
            test.info("<a href='" + relativePathToVideo + "' target='_blank' style='color:#00bc8c; font-weight:bold;'>🎬 [Play Video out of Isolated Video Directory]</a>");
        }
        
        if (Boolean.parseBoolean(config.getProperty("playwright.tracing.enabled"))) {
            String relativePathToTrace = "../../traces/" + currentTimestampRun + "/" + uniqueIterationAppend + ".zip";
            test.info("<a href='" + relativePathToTrace + "' download style='color:#f39c12; font-weight:bold;'>🔍 [Download Zip file out of Isolated Traces Directory]</a>");
        }
    }

    @AfterSuite
    public void wrapReportSuite() {
        if (extent != null) extent.flush();
    }
}