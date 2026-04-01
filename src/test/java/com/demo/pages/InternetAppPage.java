package com.demo.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InternetAppPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By username = By.id("username");
    private By password = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By flashMessage = By.id("flash");
    private By logoutButton = By.cssSelector("a[href='/logout']");

    public InternetAppPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openLoginPage() {
        driver.get("https://the-internet.herokuapp.com/login");
    }

    public void login(String user, String pass) {

        // ✅ If already logged in (browser reuse)
        if (isElementPresent(logoutButton)) {
            System.out.println("Already logged in – skipping login");
            return;
        }

        driver.findElement(username).clear();
        driver.findElement(username).sendKeys(user);

        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pass);

        driver.findElement(loginButton).click();

        // ✅ Wait for either flash message OR logout button to appear
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(flashMessage));
        } catch (Exception e) {
            // Flash message didn't appear - that's OK, login might have failed
            System.out.println("Flash message not visible - login may have failed");
        }
    }

    public boolean isLoginSuccess() {
        return isElementPresent(logoutButton);
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }

    public boolean isLogoutSuccess() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(flashMessage));
        return driver.findElement(flashMessage)
                .getText()
                .contains("You logged out of the secure area!");
    }

    private boolean isElementPresent(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty();
    }
}
