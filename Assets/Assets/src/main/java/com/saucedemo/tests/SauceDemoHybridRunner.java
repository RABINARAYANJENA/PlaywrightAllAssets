package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.utils.ExcelEngine;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SauceDemoHybridRunner extends BaseTest {

    @BeforeClass
    public void loadDataCache() throws Exception {
        ExcelEngine.loadGlobalConfigurations();
    }

    @DataProvider(name = "HybridMatrixProvider")
    public Object[][] getSuiteDataMatrix() throws Exception {
        List<String> operationalCases = ExcelEngine.getExecutableScenarios();
        List<Map<String, String>> consolidatedRows = new ArrayList<>();

        for (String targetCaseId : operationalCases) {
            List<Map<String, String>> dataRows = ExcelEngine.getTestDataIterations(targetCaseId);
            for (Map<String, String> row : dataRows) {
                row.put("Current_TestCaseID", targetCaseId);
                consolidatedRows.add(row);
            }
        }

        Object[][] matrixPayload = new Object[consolidatedRows.size()][1];
        for (int i = 0; i < consolidatedRows.size(); i++) {
            matrixPayload[i][0] = consolidatedRows.get(i);
        }
        return matrixPayload;
    }

    @Test(dataProvider = "HybridMatrixProvider")
    public void executeScenarioRow(Map<String, String> dataRow) throws Throwable {
        String targetCaseId = dataRow.get("Current_TestCaseID");
        
        test = extent.createTest(targetCaseId + " - Customer: " + dataRow.get("TargetSegment") + " (" + dataRow.get("FirstName") + ")");

        launchExecutionBrowser(ExcelEngine.getGlobalVal("Browser"), ExcelEngine.getGlobalVal("Headless"));
        
        // Explicitly extract the superclass context reference to fix Eclipse compilation error
        BaseTest masterContext = (BaseTest) this;
        
        LoginPage loginPage = new LoginPage(this.page, masterContext);
        InventoryPage inventoryPage = new InventoryPage(this.page, masterContext);
        CartPage cartPage = new CartPage(this.page, masterContext);
        CheckoutPage checkoutPage = new CheckoutPage(this.page, masterContext);

        try {
            page.navigate(ExcelEngine.getGlobalVal("URL"));
            logStep("Navigated to dynamic environment target URL", "INFO");

            loginPage.loginToApplication(ExcelEngine.getGlobalVal("Global_Username"), ExcelEngine.getGlobalVal("Global_Password"));
            Assert.assertTrue(loginPage.checkDashboardView(), "Portal catalog view initialization failure!");
            logStep("Dashboard loaded successfully - Credentials Checked", "PASS");

            inventoryPage.chooseWorkflowProducts();
            cartPage.proceedToCheckout();
            
            checkoutPage.inputCustomerInformation(dataRow.get("FirstName"), dataRow.get("LastName"), dataRow.get("ZipCode"));
            checkoutPage.completeOrderTransaction();

            boolean executionConfirmation = page.locator("text=Thank you for your order!").isVisible();
            if (executionConfirmation) {
                logStep("E2E Order Processing Validated successfully for customer " + dataRow.get("FirstName"), "PASS");
            } else {
                throw new RuntimeException("Final order checkout confirmation message was missing.");
            }

            ITestResult currentResult = Reporter.getCurrentTestResult();
            currentResult.setStatus(ITestResult.SUCCESS);
            endContextSession(targetCaseId, currentResult);

        } catch (Throwable errorDetails) {
            ITestResult currentResult = Reporter.getCurrentTestResult();
            currentResult.setStatus(ITestResult.FAILURE);
            currentResult.setThrowable(errorDetails);
            endContextSession(targetCaseId, currentResult);
            throw errorDetails; 
        }
    }
}