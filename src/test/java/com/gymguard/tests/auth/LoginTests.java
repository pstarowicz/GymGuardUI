package com.gymguard.tests.auth;

import com.gymguard.framework.base.BaseTest;
import com.gymguard.framework.pages.auth.LoginPage;
import org.testng.annotations.Test;

/**
 * Minimal TestNG test for login functionality (auth package).
 */
public class LoginTests extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        new LoginPage(getDriver()).login("alamakota@gmail.com", "Alamakot@123");
    }
}
