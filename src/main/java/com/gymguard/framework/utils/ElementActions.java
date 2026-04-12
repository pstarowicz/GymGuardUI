package com.gymguard.framework.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Encapsulates common element-level actions so page objects can reuse
 * consistent behavior for clicking, typing and reading values.
 */
public class ElementActions {

    private final WebDriver driver;
    private final WaitHelper waitHelper;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    public void click(WebElement element) {
        waitHelper.waitForElementVisible(element, 10);
        element.click();
    }

    public void clearAndType(WebElement element, String text) {
        waitHelper.waitForElementVisible(element, 10);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(WebElement element) {
        waitHelper.waitForElementVisible(element, 10);
        return element.getText();
    }

    public String getAttribute(WebElement element, String attribute) {
        waitHelper.waitForElementVisible(element, 10);
        return element.getAttribute(attribute);
    }

}
