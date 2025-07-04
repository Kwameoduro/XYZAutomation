package com.xyzbank.app.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;


public class DriverFactory {

    public static WebDriver getNewDriver(String browserName) {
        WebDriver driver;

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