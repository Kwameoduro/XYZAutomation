package com.xyzbank.app.pages.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;
import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }


    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }


    protected WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    protected void click(WebElement element) {
        waitForClickability(element).click();
    }


    protected void type(WebElement element, String text) {
        waitForVisibility(element).clear();
        element.sendKeys(text);
    }


    public String getPageTitle() {
        return driver.getTitle();
    }


    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }


    protected String getAlertTextAndAccept() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }


    public boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}