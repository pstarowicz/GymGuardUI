package com.gymguard.framework.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage provides common WebDriver utilities for all page objects.
 * It keeps only the WebDriver; helpers are static utilities used by pages.
 */
public class BasePage {

    protected final WebDriver driver;

    /**
     * Create a BasePage.
     *
     * @param driver the WebDriver instance to use
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

}
