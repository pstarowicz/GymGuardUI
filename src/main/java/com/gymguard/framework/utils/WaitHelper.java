package com.gymguard.framework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Small helper encapsulating waiting logic so pages and actions
 * don't need to create WebDriverWait everywhere.
 */
public class WaitHelper {

	private final WebDriver driver;

	public WaitHelper(WebDriver driver) {
		this.driver = driver;
	}

	public void waitForElementVisible(WebElement element, int seconds) {
		new WebDriverWait(driver, Duration.ofSeconds(seconds))
				.until(ExpectedConditions.visibilityOf(element));
	}

}
