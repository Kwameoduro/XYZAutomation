package com.xyzbank.app.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;

// Note: No 'import io.github.bonigarcia.wdm.WebDriverManager;' needed here
// because we will use its fully qualified name directly to avoid conflict.

public class DriverFactory { // Renamed from WebDriverManager

    /**
     * Initializes and returns a new WebDriver instance based on the provided browser name.
     * This method is designed to be called once per test setup to get a fresh driver.
     *
     * @param browserName The name of the browser (e.g., "chrome", "firefox", "edge", "safari").
     * @return A new WebDriver instance.
     * @throws IllegalArgumentException if an unsupported browser name is provided.
     */
    public static WebDriver getNewDriver(String browserName) {
        WebDriver driver; // Local variable, not static

        switch (browserName.toLowerCase()) {
            case "chrome":
                io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup(); // Use fully qualified name
                driver = new ChromeDriver();
                break;
            case "firefox":
                io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup(); // Use fully qualified name
                driver = new FirefoxDriver();
                break;
            case "edge":
                io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup(); // Use fully qualified name
                driver = new EdgeDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        driver.manage().window().maximize();
        return driver;
    }
}