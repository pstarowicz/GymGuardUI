package com.gymguard.framework.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage provides common WebDriver utilities for all page objects.
 * It centralizes WebDriver and WebDriverWait initialization and offers
 * small helper methods used across pages.
 */
public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    /**
     * Create a BasePage.
     * Initializes a WebDriverWait with a 10 second default timeout
     * and wires PageFactory to this page object.
     *
     * @param driver the WebDriver instance to use
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks the provided element after waiting for it to be visible.
     *
     * @param element the WebElement to click
     */
    protected void clickElement(WebElement element) {
        waitForElementVisible(element, 10);
        element.click();
    }

    /**
     * Clears an input element and types the provided text into it.
     *
     * @param element the input WebElement
     * @param text    the text to type
     */
    protected void clearAndType(WebElement element, String text) {
        waitForElementVisible(element, 10);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Waits until the given element is visible using the specified timeout.
     *
     * @param element the WebElement to wait for
     * @param seconds timeout in seconds
     */
    protected void waitForElementVisible(WebElement element, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Returns the visible text of the given element after waiting for visibility.
     *
     * @param element the WebElement
     * @return the element text
     */
    protected String getElementText(WebElement element) {
        waitForElementVisible(element, 10);
        return element.getText();
    }
}
