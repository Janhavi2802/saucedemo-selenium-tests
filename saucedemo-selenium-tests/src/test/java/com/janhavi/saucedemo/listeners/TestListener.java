package com.janhavi.saucedemo.listeners;

import com.janhavi.saucedemo.driver.DriverFactory;
import com.janhavi.saucedemo.utils.ScreenshotUtil;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.nio.file.Path;
import java.util.Arrays;

/** Takes a screenshot on failure and logs pass/fail/skip with parameters. */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.printf("[START] %s%n", describe(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.printf("[PASS]  %s (%d ms)%n", describe(result),
                result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.printf("[FAIL]  %s -> %s%n", describe(result), result.getThrowable());
        Path shot = ScreenshotUtil.capture(DriverFactory.getDriver(), result.getMethod().getMethodName());
        if (shot != null) {
            System.out.println("        Screenshot: " + shot.toAbsolutePath());
            // Shows a link to the screenshot in the TestNG HTML report
            Reporter.setCurrentTestResult(result);
            Reporter.log("<a href='" + shot.toAbsolutePath().toUri() + "'>Screenshot</a>");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.printf("[SKIP]  %s%n", describe(result));
    }

    private static String describe(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName() + "."
                + result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        return params.length == 0 ? name : name + Arrays.toString(params);
    }
}
