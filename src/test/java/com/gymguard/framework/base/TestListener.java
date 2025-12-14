package com.gymguard.framework.base;

import com.gymguard.framework.utils.ScreenshotHelper;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

/**
 * TestNG listener used in tests. Captures a screenshot on test failure and logs the path.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
         if (result == null) {
            return;
        }

        Object testInstance = result.getInstance();
        WebDriver driver = null;

        if (testInstance != null) {
            // Prefer calling public getDriver(), fall back to declared method if needed.
            try {
                Method getter = testInstance.getClass().getMethod("getDriver");
                Object maybeDriver = getter.invoke(testInstance);
                if (maybeDriver instanceof WebDriver) {
                    driver = (WebDriver) maybeDriver;
                }
            } catch (Exception ignored) {
                try {
                    Method getter = testInstance.getClass().getDeclaredMethod("getDriver");
                    getter.setAccessible(true);
                    Object maybeDriver = getter.invoke(testInstance);
                    if (maybeDriver instanceof WebDriver) {
                        driver = (WebDriver) maybeDriver;
                    }
                } catch (Exception ex) {
                    logger.debug("Could not obtain WebDriver from test instance", ex);
                }
            }
        }

        try {
            String screenshotPath = ScreenshotHelper.captureScreenshot(driver, result.getMethod().getMethodName());
            if (screenshotPath != null) {
                logger.error("Test failed. Screenshot saved: {}", screenshotPath);
            } else {
                logger.error("Test failed. Screenshot capture returned null for: {}", result.getMethod().getMethodName());
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for test {}", result.getMethod().getMethodName(), e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
