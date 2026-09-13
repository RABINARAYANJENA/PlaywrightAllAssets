package My.Framework.Pages;

import com.microsoft.playwright.Page;

public class DashboardPage {
    private final Page page;

    // Locator representing a unique element on the Dashboard page to ensure layout is fully drawn
    private final String dashboardDashboardMenu = "span:has-text('Dashboard'), h6:has-text('Dashboard')";

    public DashboardPage(Page page) {
        this.page = page;
    }

    /**
     * Verifies if the Dashboard Page has loaded completely by dynamically 
     * matching the URL criteria and structural visibility.
     * @return boolean
     */
    public boolean isDashboardLoaded() {
        try {
            // Best Practice: Dynamically wait for the browser URL pattern to match
            page.waitForURL("**/dashboard/**");
            
            // Secondary Check: Verify a core dashboard visual element is visible before proceeding
            page.locator(dashboardDashboardMenu).first().waitFor();
            
            return page.url().contains("dashboard");
        } catch (Exception e) {
            return false;
        }
    }
}