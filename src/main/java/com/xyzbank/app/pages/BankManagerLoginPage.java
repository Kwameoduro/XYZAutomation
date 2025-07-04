package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BankManagerLoginPage extends BasePage {

    @FindBy(css = "button[ng-click='addCust()']")
    private WebElement addCustomerButton;

    @FindBy(css = "button[ng-click='openAccount()']")
    private WebElement openAccountButton;

    @FindBy(css = "button[ng-click='showCust()']")
    private WebElement customersButton;

    // Constructor
    public BankManagerLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public AddCustomerPage clickAddCustomer() {
        click(addCustomerButton);
        return new AddCustomerPage(driver, wait);
    }


    public OpenAccountPage clickOpenAccount() {
        click(openAccountButton);
        return new OpenAccountPage(driver, wait);
    }


    public CustomersPage clickCustomers() {
        click(customersButton);
        return new CustomersPage(driver, wait);
    }


    public boolean isBankManagerLoginPageDisplayed() {
        try {
            waitForVisibility(addCustomerButton);
            waitForVisibility(openAccountButton);
            waitForVisibility(customersButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}