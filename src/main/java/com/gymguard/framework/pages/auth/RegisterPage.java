package com.gymguard.framework.pages.auth;

import com.gymguard.framework.base.BasePage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the registration page (auth package).
 */
public class RegisterPage extends BasePage {

    @FindBy(css = "[data-test-id='input--register--email'] input")
    private WebElement emailInput;

    @FindBy(css = "[data-test-id='input--register--password'] input")
    private WebElement passwordInput;

    @FindBy(css = "[data-test-id='button--register--submit']")
    private WebElement submitButton;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage register(String email, String password) {
        actions.clearAndType(emailInput, email);
        actions.clearAndType(passwordInput, password);
        actions.click(submitButton);
        return this;
    }
}
