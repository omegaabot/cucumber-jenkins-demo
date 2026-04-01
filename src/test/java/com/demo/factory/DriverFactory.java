package com.demo.factory;

import java.time.Duration;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

    private static final Logger logger =
            LogManager.getLogger(DriverFactory.class);

    private static WebDriver driver;

    private DriverFactory() {}

    public static void initDriver() {
        if (driver == null) {

            logger.info("Initializing Edge browser");

            EdgeOptions options = new EdgeOptions();

            // Add these three lines for CI/Jenkins headless mode
            options.addArgument("--headless=new");  // New headless mode for Edge
            options.addArgument("--no-sandbox");    // Bypass sandbox
            options.addArgument("--disable-gpu");   // Disable GPU acceleration

            // Existing options
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--start-maximized");

            options.setExperimentalOption("prefs", Map.of(
                    "credentials_enable_service", false,
                    "profile.password_manager_enabled", false
            ));

            driver = new EdgeDriver(options);

            driver.manage()
                  .timeouts()
                  .implicitlyWait(Duration.ofSeconds(10));

            printSessionId();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void printSessionId() {
        if (driver instanceof RemoteWebDriver) {
            String sessionId =
                    ((RemoteWebDriver) driver).getSessionId().toString();
            logger.info("Browser Session ID: {}", sessionId);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            logger.info("Closing Edge browser");
            driver.quit();
            driver = null;
        }
    }
}
