package com.gymguard.framework.pages.auth;

import com.gymguard.framework.base.BasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the login page (auth package).
 */
public class LoginPage extends BasePage {

    @FindBy(css = "[data-test-id='input--login--email'] input")
    private WebElement emailInput;

    @FindBy(css = "[data-test-id='input--login--password'] input")
    private WebElement passwordInput;

    @FindBy(css = "[data-test-id='button--login--submit']")
    private WebElement submitButton;

    @FindBy(css = "[role='alert']")
    private WebElement errorMessage;

    @FindBy(css = "[data-test-id='form--login']")
    private WebElement loginForm;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage login(String email, String password) {
        clearAndType(emailInput, email);
        clearAndType(passwordInput, password);
        clickElement(submitButton);
        return this;
    }

    public String getErrorMessage() {
        return getElementText(errorMessage);
    }

    public boolean isLoginFormDisplayed() {
        try {
            waitForElementVisible(loginForm, 5);
            return loginForm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailInputVisible() {
        try {
            waitForElementVisible(emailInput, 5);
            return emailInput.isDisplayed() && emailInput.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordInputVisible() {
        try {
            waitForElementVisible(passwordInput, 5);
            return passwordInput.isDisplayed() && passwordInput.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSubmitButtonVisible() {
        try {
            waitForElementVisible(submitButton, 5);
            return submitButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterEmail(String email) {
        clearAndType(emailInput, email);
    }

    public void enterPassword(String password) {
        clearAndType(passwordInput, password);
    }

    public String getEmailInputValue() {
        try {
            waitForElementVisible(emailInput, 5);
            return emailInput.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getPasswordInputType() {
        try {
            waitForElementVisible(passwordInput, 5);
            return passwordInput.getAttribute("type");
        } catch (Exception e) {
            return "";
        }
    }

    public void submit() {
        clickElement(submitButton);
    }
}
