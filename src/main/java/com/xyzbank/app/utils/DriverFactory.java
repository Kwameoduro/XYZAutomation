package com.xyzbank.app.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;


public class DriverFactory {

    public static WebDriver getNewDriver(String browserName) {
        WebDriver driver;

        switch (browserName.toLowerCase()) {
            case "chrome":
                io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup(); // Use fully qualified name
                driver = new ChromeDriver();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new"); // For newer Chrome versions
                chromeOptions.addArguments("--no-sandbox");

                break;
            case "firefox":
                io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup(); // Use fully qualified name
                driver = new FirefoxDriver();
                FirefoxOptions firefoxOptions  = new FirefoxOptions ();
                firefoxOptions .addArguments("--headless=new"); // For newer Chrome versions
                firefoxOptions .addArguments("--no-sandbox");
                break;
            case "edge":
                io.github.bonigarcia.wdm.WebDriverManager.edgedriver().setup(); // Use fully qualified name
                driver = new EdgeDriver();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless=new"); // For newer Chrome versions
                edgeOptions.addArguments("--no-sandbox");
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
        driver.manage().window().maximize();
        return driver;
    }
}