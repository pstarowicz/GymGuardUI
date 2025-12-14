package com.gymguard.framework.pages.authentication;

import com.gymguard.framework.base.BasePage;
import com.gymguard.framework.pages.dashboard.DashboardPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the login page.
 * <p>
 * Exposes simple actions: login and reading an error message.
 */
public class LoginPage extends BasePage {

	// Use stable data-test-id attributes provided by the application UI
	@FindBy(css = "[data-test-id='input--login--email'] input")
	private WebElement emailInput;

	@FindBy(css = "[data-test-id='input--login--password'] input")
	private WebElement passwordInput;

	@FindBy(css = "[data-test-id='button--login--submit']")
	private WebElement submitButton;

	// The React MUI Alert typically renders with role="alert" inside the form.
	// We locate the alert inside the login form to read error text.
	@FindBy(css = "[role='alert']")
	private WebElement errorMessage;

	/**
	 * Create the LoginPage.
	 *
	 * @param driver the WebDriver instance
	 */
	public LoginPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Perform login and return the resulting DashboardPage.
	 *
	 * @param email the email to use
	 * @param password the password to use
	 * @return DashboardPage instance
	 */
	public DashboardPage login(String email, String password) {
		clearAndType(emailInput, email);
		clearAndType(passwordInput, password);
		clickElement(submitButton);
		return new DashboardPage();
	}

	/**
	 * Returns the visible error message text shown on the page.
	 * If no error is present this may throw or return an empty string depending on the page state.
	 *
	 * @return the error message text
	 */
	public String getErrorMessage() {
		return getElementText(errorMessage);
	}
}

