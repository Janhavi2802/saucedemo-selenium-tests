package com.janhavi.saucedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    /** Saves a PNG to target/screenshots and returns its path, or null if it failed. */
    public static Path capture(WebDriver driver, String testName) {
        if (driver == null) {
            return null;
        }
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String safeName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            Path file = SCREENSHOT_DIR.resolve(safeName + "_" + LocalDateTime.now().format(STAMP) + ".png");
            Files.write(file, png);
            return file;
        } catch (IOException | RuntimeException e) {
            System.err.println("Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
