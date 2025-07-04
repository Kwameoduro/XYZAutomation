package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OpenAccountPage extends BasePage {

    @FindBy(id = "userSelect")
    private WebElement customerNameDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement processButton;

    // Constructor
    public OpenAccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public void selectCustomerName(String customerName) {
        waitForVisibility(customerNameDropdown);
        Select select = new Select(customerNameDropdown);
        select.selectByVisibleText(customerName);
    }


    public void selectCurrency(String currency) {
        waitForVisibility(currencyDropdown);
        Select select = new Select(currencyDropdown);
        select.selectByVisibleText(currency);
    }


    public void clickProcessButton() {
        click(processButton);
    }


    public String getAlertTextAndAccept() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept(); // Dismiss the alert
        return alertText;
    }


    public String openAccount(String customerName, String currency) {
        selectCustomerName(customerName);
        selectCurrency(currency);
        clickProcessButton();
        return getAlertTextAndAccept();
    }


    public boolean isOpenAccountPageDisplayed() {
        try {
            waitForVisibility(customerNameDropdown);
            waitForVisibility(currencyDropdown);
            waitForVisibility(processButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}