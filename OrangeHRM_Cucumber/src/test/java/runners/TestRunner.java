//Run With junit
//package runners;
//
//import org.junit.runner.RunWith;
//
//import io.cucumber.junit.Cucumber;
//import io.cucumber.junit.CucumberOptions;
//
//@RunWith(Cucumber.class)
//
//@CucumberOptions(
//
//features = "src/main/resources/features",
//
//glue = "stepDefinitions",
//
//plugin = {"pretty"}
//
//)
//
//public class TestRunner {
//
//}

// Run WIth TestNG
package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/main/resources/features",
    glue = "stepDefinitions",
    plugin = {"pretty"}
)
public class TestRunner extends AbstractTestNGCucumberTests {

}