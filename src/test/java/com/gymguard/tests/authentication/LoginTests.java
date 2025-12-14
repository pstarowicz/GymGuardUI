package com.gymguard.tests.authentication;

import com.gymguard.framework.base.BaseTest;
import com.gymguard.framework.base.TestListener;
import com.gymguard.framework.pages.authentication.LoginPage;
import com.gymguard.framework.pages.dashboard.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Minimal TestNG test for login functionality.
 */
@Listeners(TestListener.class)
public class LoginTests extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboard = loginPage.login("user@example.com", "Password123");
        Assert.assertNotNull(dashboard, "Dashboard page should be returned after login");
    }
}
