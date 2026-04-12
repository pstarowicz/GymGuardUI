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
public final class WaitHelper {

	private WaitHelper() {}

	public static void waitForElementVisible(WebDriver driver, WebElement element, int seconds, String elementName) {
		try {
			new WebDriverWait(driver, Duration.ofSeconds(seconds))
					.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			throw new AssertionError("Timeout waiting for '" + elementName + "' to be visible", e);
		}
	}

}
