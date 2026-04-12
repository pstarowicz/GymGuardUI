package com.gymguard.framework.pages.auth;

import com.gymguard.framework.base.BasePage;
import com.gymguard.framework.utils.ElementActions;

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
        ElementActions.clearAndType(driver, emailInput, email, "email input");
        ElementActions.clearAndType(driver, passwordInput, password, "password input");
        ElementActions.click(driver, submitButton, "submit button");
        return this;
    }

    public String getErrorMessage() {
        return ElementActions.getText(driver, errorMessage, "error message");
    }

    public boolean isLoginFormDisplayed() {
        return ElementActions.isDisplayed(driver, loginForm, "login form");
    }

    public boolean isEmailInputVisible() {
        return ElementActions.isVisible(driver, emailInput, "email input");
    }

    public boolean isPasswordInputVisible() {
        return ElementActions.isVisible(driver, passwordInput, "password input");
    }

    public boolean isSubmitButtonVisible() {
        return ElementActions.isDisplayed(driver, submitButton, "submit button");
    }

    public void enterEmail(String email) {
        ElementActions.clearAndType(driver, emailInput, email, "email input");
    }

    public void enterPassword(String password) {
        ElementActions.clearAndType(driver, passwordInput, password, "password input");
    }

    public String getEmailInputValue() {
        return ElementActions.getAttribute(driver, emailInput, "value", "email input");
    }

    public String getPasswordInputType() {
        return ElementActions.getAttribute(driver, passwordInput, "type", "password input");
    }

    public void submit() {
        ElementActions.click(driver, submitButton, "submit button");
    }
}
