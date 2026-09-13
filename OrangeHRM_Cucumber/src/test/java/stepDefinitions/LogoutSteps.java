package stepDefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.*;

public class LogoutSteps {

    WebDriver driver;

    @Given("User opens Chrome browser3")
    public void openBrowser3() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @When("User opens OrangeHRM website3")
    public void openWebsite3() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @And("User enters username3")
    public void enterUsername3() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.name("username")).sendKeys("Admin");
    }

    @And("User enters password3")
    public void enterPassword3() {
        driver.findElement(By.name("password")).sendKeys("admin123");
    }

    @And("User clicks Login button3")
    public void clickLogin3() throws InterruptedException {
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        Thread.sleep(3000);
    }

    @And("User clicks Profile3")
    public void clickProfile3() {
        driver.findElement(By.className("oxd-userdropdown-tab")).click();
    }

    @And("User clicks Logout3")
    public void clickLogout3() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.linkText("Logout")).click();
    }

    @Then("User should return to Login page3")
    public void verifyLoginPage3() throws InterruptedException {
        Thread.sleep(3000);

        Assert.assertTrue(driver.getCurrentUrl().contains("login"));

        System.out.println("Logout Successful");

        driver.quit();
    }
}