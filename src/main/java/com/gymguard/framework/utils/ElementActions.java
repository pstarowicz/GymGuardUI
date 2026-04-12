package com.gymguard.framework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Encapsulates common element-level actions so page objects can reuse
 * consistent behavior for clicking, typing and reading values.
 *
 * All operations accept `elementName` to produce clearer assertion messages
 * and handle their own try/catch so page objects don't need to duplicate
 * error handling.
 */
public class ElementActions {

    private final WebDriver driver;
    private final WaitHelper waitHelper;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    public void click(WebElement element, String elementName) {
        waitHelper.waitForElementVisible(element, 10, elementName);

        try {
            element.click();
        } catch (Exception e) {
            throw new AssertionError("Failed to click '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public void clearAndType(WebElement element, String text, String elementName) {
        waitHelper.waitForElementVisible(element, 10, elementName);

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

    public String getText(WebElement element, String elementName) {
        waitHelper.waitForElementVisible(element, 10, elementName);

        try {
            return element.getText();
        } catch (Exception e) {
            throw new AssertionError("Failed to read text from '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public String getAttribute(WebElement element, String attribute, String elementName) {
        waitHelper.waitForElementVisible(element, 10, elementName);

        try {
            return element.getAttribute(attribute);
        } catch (Exception e) {
            throw new AssertionError("Failed to get attribute '" + attribute + "' from '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public boolean isDisplayed(WebElement element, String elementName) {
        waitHelper.waitForElementVisible(element, 5, elementName);

        try {
            return element.isDisplayed();
        } catch (Exception e) {
            throw new AssertionError("Failed to check displayed state of '" + elementName + "': " + e.getMessage(), e);
        }
    }

    public boolean isVisible(WebElement element, String elementName) {
        waitHelper.waitForElementVisible(element, 5, elementName);

        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            throw new AssertionError("Failed to check visibility/enabled state of '" + elementName + "': " + e.getMessage(), e);
        }
    }

}
