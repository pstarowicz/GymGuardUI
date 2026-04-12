package com.gymguard.framework.base;

import com.gymguard.framework.utils.ElementActions;
import com.gymguard.framework.utils.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * BasePage provides common WebDriver utilities for all page objects.
 * It initializes helpers so pages can reuse consistent actions and waits.
 */
public class BasePage {

    protected final WebDriver driver;
    protected final WaitHelper waitHelper;
    protected final ElementActions actions;

    /**
     * Create a BasePage.
     * Initializes a WebDriverWait with a 10 second default timeout,
     * the helper classes and wires PageFactory to this page object.
     *
     * @param driver the WebDriver instance to use
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        this.actions = new ElementActions(driver);
        PageFactory.initElements(driver, this);
    }

}
