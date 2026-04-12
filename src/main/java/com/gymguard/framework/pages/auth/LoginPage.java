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
}
