package com.gymguard.framework.factories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Simple, non-thread-safe factory for creating WebDriver instances.
 *
 * This class creates a Chrome WebDriver.
 * Provide the ChromeDriver binary using one of:
 *  - System property `webdriver.chrome.driver` (e.g. -Dwebdriver.chrome.driver=/path/to/chromedriver)
 *  - Environment variable `CHROMEDRIVER_PATH`
 *  - Or ensure the chromedriver executable is available on the system PATH.
 *
 * It respects the JVM system property {@code headless} (true/false)
 * to enable headless mode when needed. Window is maximized and an implicit wait
 * of 5 seconds is configured.
 */
public final class DriverFactory {

    private DriverFactory() {
        // utility class — prevent instantiation
    }

    /**
     * Create and configure a new Chrome WebDriver instance.
     *
     * @return a configured {@link WebDriver} (ChromeDriver)
     */
    public static WebDriver createDriver() {
        // Allow explicit chromedriver path via system property or environment variable.
        // String driverPath = System.getProperty("webdriver.chrome.driver");
        // if (driverPath == null || driverPath.isBlank()) {
        //     driverPath = System.getenv("CHROMEDRIVER_PATH");
        //     if (driverPath != null && !driverPath.isBlank()) {
        //         System.setProperty("webdriver.chrome.driver", driverPath);
        //     }
        // }

        ChromeOptions options = new ChromeOptions();

        // Respect system property 'headless' (e.g. -Dheadless=true)
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless");
        }

        // Helpful defaults for CI environments
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {
            // Some environments (e.g., headless containers) may not support window operations.
        }
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }
}
