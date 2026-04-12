package com.gymguard.framework.pages.common;

import com.gymguard.framework.base.BasePage;
import com.gymguard.framework.utils.ElementActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the top navigation bar elements.
 */
public class NavigationBarPage extends BasePage {

    @FindBy(css = "[data-test-id='nav--profile']")
    private WebElement navProfile;

    public NavigationBarPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns the profile/name text from the navigation bar.
     */
    public String getProfileName() {
        return ElementActions.getText(driver, navProfile, "nav profile").trim();
    }

    public void clickProfile() {
        ElementActions.click(driver, navProfile, "nav profile");
    }
}
