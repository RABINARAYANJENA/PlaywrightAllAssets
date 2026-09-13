package My.Framework.Base;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.microsoft.playwright.*;

import My.Framework.Utilities.ExtentManager;

public class BaseTest {

	// ThreadLocal containers for parallel execution
	private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
	private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

	@BeforeSuite
	public void setupReport() {
		ExtentManager.getInstance();
	}

	@BeforeMethod
	public void browserSetup() {

		Playwright pw = Playwright.create();
		playwrightThreadLocal.set(pw);

		BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(false)
				.setChannel("chrome");

		ArrayList<String> args = new ArrayList<>();
		args.add("--start-maximized");
		launchOptions.setArgs(args);

		Browser browser = pw.chromium().launch(launchOptions);
		browserThreadLocal.set(browser);

		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

		contextThreadLocal.set(context);

		// Start Playwright tracing
		context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));

		Page page = context.newPage();
		pageThreadLocal.set(page);
	}

	/**
	 * Returns current thread page instance.
	 */
	public Page getPage() {
		return pageThreadLocal.get();
	}

	/**
	 * Captures full-page screenshot and returns Base64 image.
	 */
	public String captureScreenshotBase64() {
		byte[] image = getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));

		return "data:image/png;base64," + Base64.getEncoder().encodeToString(image);
	}

	/**
	 * Logs step with screenshot preview into Extent Report.
	 */
	public void logStep(String message) {

		String screenshot = captureScreenshotBase64();

		ExtentManager.getTest().info(message);

		String intermediateStepHtml = "<a href=\"javascript:void(0);\" onclick=\"var w=window.open();"
				+ "w.document.write('<img src=\\'" + screenshot + "\\'/>');w.document.close();\">" + "<img src='"
				+ screenshot + "' style='height:80px; width:120px; " + "border:2px solid #4A90E2; "
				+ "object-fit:contain; cursor:pointer;'/>" + "</a>";

		ExtentManager.getTest().info(intermediateStepHtml);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(Method method) {

		try {

			BrowserContext context = contextThreadLocal.get();

			if (context != null) {

				// Create traces directory if missing
				Path traceDirectory = Paths.get("Traces");
				Files.createDirectories(traceDirectory);

				// Unique trace name
				String traceFileName = method.getName() + "_" + System.currentTimeMillis() + ".zip";

				Path tracePath = traceDirectory.resolve(traceFileName);

				// Stop tracing and save zip
				context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

				System.out.println("Trace saved: " + tracePath.toAbsolutePath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Close Context
		if (contextThreadLocal.get() != null) {
			contextThreadLocal.get().close();
			contextThreadLocal.remove();
		}

		// Close Browser
		if (browserThreadLocal.get() != null) {
			browserThreadLocal.get().close();
			browserThreadLocal.remove();
		}

		// Close Playwright
		if (playwrightThreadLocal.get() != null) {
			playwrightThreadLocal.get().close();
			playwrightThreadLocal.remove();
		}

		pageThreadLocal.remove();
	}

	@AfterSuite
	public void flushReport() {

		if (ExtentManager.getInstance() != null) {
			ExtentManager.getInstance().flush();
		}
	}
}