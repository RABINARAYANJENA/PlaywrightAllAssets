package stepDefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.*;

public class ForgotPasswordSteps {

    WebDriver driver;

    @Given("User opens Chrome browser1")
    public void openBrowser1() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @When("User opens OrangeHRM website1")
    public void openWebsite1() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @And("User clicks Forgot Password1")
    public void clickForgotPassword1() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.linkText("Forgot your password?")).click();
    }

    @And("User enters username for reset1")
    public void enterUsername1() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.name("username")).sendKeys("Admin");
    }

    @And("User clicks Reset Password1")
    public void clickResetPassword1() {
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Then("Reset Password message should display1")
    public void verifyMessage1() throws InterruptedException {
        Thread.sleep(3000);

        String text = driver.findElement(By.tagName("h6")).getText();

        Assert.assertEquals("Reset Password link sent successfully", text);

        System.out.println("Password Reset Link Sent");

        driver.quit();
    }
}