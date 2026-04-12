package com.gymguard.framework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Encapsulates common element-level actions so page objects can reuse
 * consistent behavior for clicking, typing and reading values.
 *
 * All operations accept `elementName` to produce clearer assertion messages
 * and throw `AssertionError` on failures.
 */
public final class ElementActions {

    private ElementActions() {}

    public static void click(WebDriver driver, WebElement element, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 10, elementName);

        try {
            element.click();
        } catch (Exception e) {
            throw new AssertionError("Failed to click '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public static void clearAndType(WebDriver driver, WebElement element, String text, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 10, elementName);

        try {
            element.clear();
        } catch (Exception e) {
            throw new AssertionError("Failed to clear '" + elementName + "': " + e.getMessage(), e);
        }

        try {
            element.sendKeys(text);
        } catch (Exception e) {
            throw new AssertionError("Failed to type into '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public static String getText(WebDriver driver, WebElement element, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 10, elementName);

        try {
            return element.getText();
        } catch (Exception e) {
            throw new AssertionError("Failed to read text from '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public static String getAttribute(WebDriver driver, WebElement element, String attribute, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 10, elementName);

        try {
            return element.getAttribute(attribute);
        } catch (Exception e) {
            throw new AssertionError("Failed to get attribute '" + attribute + "' from '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public static boolean isDisplayed(WebDriver driver, WebElement element, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 5, elementName);

        try {
            return element.isDisplayed();
        } catch (Exception e) {
            throw new AssertionError("Failed to check displayed state of '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public static boolean isVisible(WebDriver driver, WebElement element, String elementName) {
        WaitHelper.waitForElementVisible(driver, element, 5, elementName);

        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            throw new AssertionError("Failed to check visibility/enabled state of '" + elementName + "': " + e.getMessage(), e);
        }
    }

}
