package com.gymguard.framework.factories;

import org.openqa.selenium.WebDriver;

/**
 * Simple PageFactory helper used to create page object instances.
 *
 * <p>Usage example:
 * <pre>
 * MyPage page = PageFactory.initElements(driver, MyPage.class);
 * </pre>
 *
 * <p>This helper expects the page class to expose a public constructor that accepts a single
 * {@link WebDriver} parameter.
 */
public final class PageFactory {

    private PageFactory() {
        // utility class - prevent instantiation
    }

    /**
     * Create and return a new instance of the given page class by invoking the
     * page class constructor that accepts a single {@link WebDriver} argument.
     *
     * @param driver the WebDriver instance to pass to the page's constructor
     * @param pageClass the class of the page object to instantiate
     * @param <T> the page object type
     * @return a new instance of {@code pageClass}
     * @throws RuntimeException if the page class cannot be instantiated (no matching constructor or reflection error)
     */
    public static <T> T initElements(WebDriver driver, Class<T> pageClass) {
        try {
            return pageClass.getConstructor(WebDriver.class).newInstance(driver);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate page class: " + pageClass.getName(), e);
        }
    }
}
