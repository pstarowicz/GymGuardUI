package com.gymguard.tests.authentication;

import com.gymguard.framework.base.BaseTest;
import com.gymguard.framework.pages.authentication.LoginPage;
import com.gymguard.framework.pages.dashboard.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Minimal TestNG test for login functionality.
 */
public class LoginTests extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        new LoginPage(getDriver()).login("alamakota@gmail.com", "Alamakot@123");

       // Assert.assertNotNull(dashboard, "Dashboard page should be returned after login");
    }
}
