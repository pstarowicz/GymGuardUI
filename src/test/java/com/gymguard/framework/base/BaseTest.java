package com.gymguard.framework.base;

import com.gymguard.framework.factories.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Base test class for UI tests.
 *
 * <p>Responsibilities:
 * - create a WebDriver instance before each test via {@link DriverFactory}
 * - navigate to the application's base URL loaded from classpath application.properties
 * - quit the driver after each test
 *
 * Assumptions:
 * - a properties file is available on the classpath at {@code config/application.properties}
 *   or {@code application.properties} and contains the key {@code base.url}.
 */
@Listeners(TestListener.class)
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
     * Quit the WebDriver after each test method.
     *
     * @param result TestNG test result used to detect failures.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            
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
    public WebDriver getDriver() {
        return driver;
    }

    private String loadBaseUrl() {
        Properties props = new Properties();
        String[] candidatePaths = {"config/application.properties"};
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
