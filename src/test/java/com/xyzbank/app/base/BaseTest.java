package com.xyzbank.app.base;

import com.xyzbank.app.utils.PropertiesLoader;
import com.xyzbank.app.utils.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseURL;
    protected String browserName;
    protected long implicitWaitSeconds;
    protected long explicitWaitSeconds;

    @BeforeEach
    void setup() {
        // Ensure config properties are loaded before using them
        PropertiesLoader.loadConfigProperties(); // Explicitly load config properties

        // Load properties from config.properties using the new getConfigProperty methods
        baseURL = PropertiesLoader.getConfigProperty("base.url");
        browserName = PropertiesLoader.getConfigProperty("browser", "chrome"); // Default to chrome
        implicitWaitSeconds = Long.parseLong(PropertiesLoader.getConfigProperty("implicit.wait.seconds", "10"));
        explicitWaitSeconds = Long.parseLong(PropertiesLoader.getConfigProperty("explicit.wait.seconds", "20"));

        // Initialize WebDriver using the DriverFactory
        driver = DriverFactory.getNewDriver(browserName);

        // Set implicit wait (useful for elements that might not appear immediately)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

        // Initialize WebDriverWait for explicit waits
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));

        // Navigate to the base URL
        driver.get(baseURL);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}