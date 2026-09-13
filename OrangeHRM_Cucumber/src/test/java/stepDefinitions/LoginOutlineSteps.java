package stepDefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.*;

public class LoginOutlineSteps {

	WebDriver driver;

	@Given("User opens Chrome browser2")
	public void openBrowser2() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@When("User opens OrangeHRM website2")
	public void openWebsite2() {
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
	}

	@And("User enters username2 {string}")
	public void enterUsername2(String username) {
		driver.findElement(By.name("username")).sendKeys(username);
	}

	@And("User enters password2 {string}")
	public void enterPassword2(String password) {
		driver.findElement(By.name("password")).sendKeys(password);
	}

	@And("User clicks Login button2")
	public void clickLogin2() throws InterruptedException {
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Thread.sleep(3000);
	}

	@Then("Login result should be verified2")
	public void verifyLogin2() {

		if (driver.getCurrentUrl().contains("dashboard")) {

			System.out.println("Login Successful");

		} else {

			String error = driver.findElement(By.xpath("//p[contains(@class,'alert-content-text')]")).getText();

			System.out.println("Login Failed : " + error);
		}

		driver.quit();
	}
}