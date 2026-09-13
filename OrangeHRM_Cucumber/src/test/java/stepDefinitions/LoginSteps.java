package stepDefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.*;

public class LoginSteps {

    WebDriver driver;

    @Given("User opens Chrome browser4")
    public void user_opens_chrome_browser4() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    @When("User opens OrangeHRM website4")
    public void user_opens_website4() {

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

    }

    @And("User enters username4")
    public void user_enters_username4() throws InterruptedException {

        Thread.sleep(3000);
        driver.findElement(By.name("username")).sendKeys("Admin");

    }

    @And("User enters password4")
    public void user_enters_password4() {

        driver.findElement(By.name("password")).sendKeys("admin123");

    }

    @And("User clicks Login button4")
    public void user_clicks_login_button4() {

        driver.findElement(By.xpath("//button[@type='submit']")).click();

    }

    @Then("User should see Dashboard4")
    public void user_should_see_dashboard4() throws InterruptedException {

        Thread.sleep(3000);

        String actual = driver.getCurrentUrl();

        Assert.assertTrue(actual.contains("dashboard"));

        System.out.println("Login Successful");

        driver.quit();

    }

}