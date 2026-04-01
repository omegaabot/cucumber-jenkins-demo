package com.demo.hooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.demo.factory.DriverFactory;
import com.demo.utils.ScreenshotUtil;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private static final Logger logger =
            LogManager.getLogger(Hooks.class);

    @Before  // ✅ CHANGED: Runs BEFORE EACH scenario (not BeforeAll)
    public void beforeScenario() {
        logger.info("=== Opening browser for scenario ===");
        DriverFactory.initDriver();
    }

    // ✅ Screenshot hook (runs after EACH scenario)
    @After
    public void captureScreenshotOnFailure(Scenario scenario) {

        WebDriver driver = DriverFactory.getDriver();

        if (driver != null && scenario.isFailed()) {

            logger.error("Scenario FAILED → Taking screenshot");

            byte[] screenshotBytes =
                    ScreenshotUtil.captureScreenshotAsBytes(driver);
            scenario.attach(screenshotBytes, "image/png", scenario.getName());

            String path =
                    ScreenshotUtil.captureScreenshotToFile(driver, scenario.getName());
            logger.info("Screenshot saved at: {}", path);
        }
    }

    @After  // ✅ CHANGED: Runs AFTER EACH scenario (not AfterAll)
    public void afterScenario() {
        logger.info("=== Closing browser after scenario ===");
        DriverFactory.quitDriver();
    }
}
