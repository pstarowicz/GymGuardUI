package com.gymguard.tests.auth;

import com.gymguard.framework.base.BaseTest;
import com.gymguard.framework.pages.auth.LoginPage;
import com.gymguard.framework.pages.common.NavigationBarPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * TestNG test for login functionality (GGU-1: Login Test).
 */
public class LoginTests extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        String email = "alamakota@gmail.com";
        String password = "Alamakot@123";
        String name = "Ala Makota";
        String url = "/workouts";

        LoginPage loginPage = new LoginPage(getDriver());

        // Step 1: Verify login page elements
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed");
        Assert.assertTrue(loginPage.isEmailInputVisible(), "Email input should be visible and enabled");
        Assert.assertTrue(loginPage.isPasswordInputVisible(), "Password input should be visible and enabled");
        Assert.assertTrue(loginPage.isSubmitButtonVisible(), "Submit button should be visible");

        // Step 2: Enter credentials and verify inputs
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        Assert.assertEquals(loginPage.getEmailInputValue(), email, "Email input should contain entered email");
        Assert.assertEquals(loginPage.getPasswordInputType(), "password", "Password input should be of type password");

        // Step 3: Submit the form and verify redirect and user name in nav
        loginPage.submit();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(url));
        Assert.assertTrue(getDriver().getCurrentUrl().contains(url), "Should be on dashboard after login");

        NavigationBarPage nav = new NavigationBarPage(getDriver());
        String profileName = nav.getProfileName();
        Assert.assertEquals(profileName, name);

        // Step 4: Refresh and verify session persistence
        getDriver().navigate().refresh();
        wait.until(ExpectedConditions.urlContains(url));
        String profileAfterRefresh = nav.getProfileName();
        Assert.assertEquals(profileAfterRefresh, name);

    }
}
