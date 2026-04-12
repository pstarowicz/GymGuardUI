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
        actions.clearAndType(emailInput, email, "email input");
        actions.clearAndType(passwordInput, password, "password input");
        actions.click(submitButton, "submit button");
        return this;
    }

    public String getErrorMessage() {
        return actions.getText(errorMessage, "error message");
    }

    public boolean isLoginFormDisplayed() {
        return actions.isDisplayed(loginForm, "login form");
    }

    public boolean isEmailInputVisible() {
        return actions.isVisible(emailInput, "email input");
    }

    public boolean isPasswordInputVisible() {
        return actions.isVisible(passwordInput, "password input");
    }

    public boolean isSubmitButtonVisible() {
        return actions.isDisplayed(submitButton, "submit button");
    }

    public void enterEmail(String email) {
        actions.clearAndType(emailInput, email, "email input");
    }

    public void enterPassword(String password) {
        actions.clearAndType(passwordInput, password, "password input");
    }

    public String getEmailInputValue() {
        return actions.getAttribute(emailInput, "value", "email input");
    }

    public String getPasswordInputType() {
        return actions.getAttribute(passwordInput, "type", "password input");
    }

    public void submit() {
        actions.click(submitButton, "submit button");
    }
}
