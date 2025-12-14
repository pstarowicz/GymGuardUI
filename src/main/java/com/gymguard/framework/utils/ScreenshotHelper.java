package com.gymguard.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Small helper to capture screenshots used by tests and listeners.
 */
public class ScreenshotHelper {

	private static final String SCREENSHOTS_DIR = "screenshots";

	/**
	 * Capture a screenshot if the driver supports it. Returns the saved file path
	 * or null if capture failed.
	 */
	public static String captureScreenshot(WebDriver driver, String name) {
		if (driver == null) {
			return null;
		}

		try {
			if (!(driver instanceof TakesScreenshot)) {
				return null;
			}

			Object raw = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			if (!(raw instanceof byte[])) {
				return null;
			}
			byte[] bytes = (byte[]) raw;

			String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
			String fileName = (name != null ? name : "screenshot") + "-" + timestamp + ".png";
			Path dir = Paths.get(SCREENSHOTS_DIR);
			if (!Files.exists(dir)) {
				Files.createDirectories(dir);
			}

			Path out = dir.resolve(fileName);
			Files.write(out, bytes);
			return out.toString();
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}

