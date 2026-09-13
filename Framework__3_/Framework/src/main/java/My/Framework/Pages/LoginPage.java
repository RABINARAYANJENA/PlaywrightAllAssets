package My.Framework.Pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;

    // Standardized, resilient UI element locators
    private final String usernamePlaceholder = "Username";
    private final String passwordPlaceholder = "Password"; 
    private final String loginBtnRoleName = "Login";

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }

    public void enterUsername(String username) {
        // Explicitly wait for element visibility dynamically before interaction
        page.getByPlaceholder(usernamePlaceholder).waitFor();
        page.getByPlaceholder(usernamePlaceholder).fill(username);
    }

    public void enterPassword(String password) {
        page.getByPlaceholder(passwordPlaceholder).waitFor();
        page.getByPlaceholder(passwordPlaceholder).fill(password);
    }

    public void clickLogin() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(loginBtnRoleName)).waitFor();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(loginBtnRoleName)).click();
    }
    
    /**
     * Combined business workflow wrapper (Fluent POM approach)
     */
    public void loginToApplication(String url, String username, String password) {
        navigateTo(url);
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}