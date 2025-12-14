package com.gymguard.framework.base;

import com.gymguard.framework.factories.DriverFactory;
import com.gymguard.framework.utils.ScreenshotHelper;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Base test class for UI tests.
 *
 * <p>Responsibilities:
 * - create a WebDriver instance before each test via {@link DriverFactory}
 * - navigate to the application's base URL loaded from classpath application.properties
 * - on failure, capture a screenshot via {@link ScreenshotHelper}
 * - quit the driver after each test
 *
 * Assumptions:
 * - a properties file is available on the classpath at {@code config/application.properties}
 *   or {@code application.properties} and contains the key {@code base.url}.
 */
public abstract class BaseTest {

    private WebDriver driver;

    /**
     * Create the WebDriver and navigate to the base URL before each test method.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        String baseUrl = loadBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.get(baseUrl);
        }
    }

    /**
     * Quit the WebDriver after each test method. If the test failed, capture a screenshot.
     *
     * @param result TestNG test result used to detect failures.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result != null && !result.isSuccess()) {
                try {
                    ScreenshotHelper.captureScreenshot(driver, result.getMethod().getMethodName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {
                }
                driver = null;
            }
        }
    }

    /**
     * Protected getter for subclasses to access the WebDriver instance.
     *
     * @return current WebDriver instance (may be null if setUp has not run yet)
     */
    protected WebDriver getDriver() {
        return driver;
    }

    private String loadBaseUrl() {
        Properties props = new Properties();
        String[] candidatePaths = {"config/application.properties", "application.properties"};
        for (String path : candidatePaths) {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
                if (is != null) {
                    props.load(is);
                    String url = props.getProperty("base.url");
                    if (url != null && !url.isEmpty()) {
                        return url;
                    }
                }
            } catch (IOException ignored) {
                // Try next
            }
        }
        return "http://localhost:3000";
    }
}
