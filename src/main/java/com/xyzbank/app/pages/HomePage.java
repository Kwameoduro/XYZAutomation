package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // Web Elements on the Home Page
    @FindBy(css = "button[ng-click='customer()']")
    private WebElement customerLoginButton;

    @FindBy(css = "button[ng-click='manager()']")
    private WebElement bankManagerLoginButton;

    // Constructor
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public CustomerLoginPage clickCustomerLogin() {
        click(customerLoginButton);
        return new CustomerLoginPage(driver, wait);
    }


    public BankManagerLoginPage clickBankManagerLogin() {
        click(bankManagerLoginButton);
        return new BankManagerLoginPage(driver, wait);
    }


    public boolean isHomePageDisplayed() {
        try {
            waitForVisibility(customerLoginButton);
            waitForVisibility(bankManagerLoginButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}