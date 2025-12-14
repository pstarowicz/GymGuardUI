package com.gymguard.framework.factories;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Simple, non-thread-safe factory for creating WebDriver instances.
 * <p>
 * This class creates a Chrome WebDriver using WebDriverManager to manage the
 * driver binary. It respects the JVM system property {@code headless} (true/false)
 * to enable headless mode when needed. Window is maximized and an implicit wait
 * of 5 seconds is configured.
 *
 * Note: thread-safety is intentionally not provided here — a separate
 * DriverManager is expected to handle per-thread drivers in the test framework.
 */
public final class DriverFactory {

    private DriverFactory() {
        // utility class — prevent instantiation
    }

    /**
     * Create and configure a new Chrome WebDriver instance.
     *
     * Behavior:
     * - Uses WebDriverManager to setup chromedriver
     * - Configures ChromeOptions; enables headless when system property
     *   {@code headless} is set to true
     * - Maximizes the browser window
     * - Sets an implicit wait of 5 seconds
     *
     * @return a configured {@link WebDriver} (ChromeDriver)
     */
    public static WebDriver createDriver() {
        // Ensure chromedriver binary is present
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Respect system property 'headless' (e.g. -Dheadless=true)
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            // Use a command-line argument for headless mode for broader compatibility
            options.addArguments("--headless");
        }

        WebDriver driver = new ChromeDriver(options);

        // Maximize and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }
}
