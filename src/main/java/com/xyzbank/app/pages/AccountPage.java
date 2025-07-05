package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
//import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountPage extends BasePage {

    // Web Elements on the Account Page
    @FindBy(css = "strong:nth-child(1)")
    private WebElement welcomeMessage;

    @FindBy(css = "strong:nth-child(2)")
    private WebElement accountNumber;

    @FindBy(css = "div.center strong:nth-child(2)")
    private WebElement currentBalance;

    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionsButton;

    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement depositButton;

    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawalButton;

    @FindBy(css = ".btn.logout")
    private WebElement logoutButton;

    // Deposit/Withdrawal form elements
    @FindBy(css = "input[placeholder='amount']")
    private WebElement amountInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    // Transaction status message
    @FindBy(css = "span[ng-show='message']")
    private WebElement transactionStatusMessage;

    // Account dropdown for multiple accounts
    @FindBy(id = "accountSelect")
    private WebElement accountDropdown;

    // Constructor
    public AccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


     //    ("Verify Account Page is displayed")
    public boolean isAccountPageDisplayed() {
        try {
            waitForVisibility(welcomeMessage);
            waitForVisibility(currentBalance);
            waitForVisibility(transactionsButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


     //    ("Get current account balance")
    public String getCurrentBalance() {
        waitForVisibility(currentBalance);
        String balanceText = currentBalance.getText().trim();
        // Extract only the numeric part from the balance text
        return balanceText.replaceAll("[^0-9]", "");
    }


    //    ("Get account number")
    public String getAccountNumber() {
        waitForVisibility(accountNumber);
        String accountText = accountNumber.getText().trim();
        // Extract only the numeric part from the account number text
        return accountText.replaceAll("[^0-9]", "");
    }


    //    ("Get welcome message")
    public String getWelcomeMessage() {
        waitForVisibility(welcomeMessage);
        return welcomeMessage.getText().trim();
    }


    //    ("Click Deposit button")
    public void clickDepositButton() {
        click(depositButton);
        waitForVisibility(amountInput);
    }


   //    ("Click Withdrawal button")
    public void clickWithdrawalButton() {
        click(withdrawalButton);
        waitForVisibility(amountInput);
    }


   //    ("Deposit funds: {amount}")
    public void depositFunds(String amount) {
        clickDepositButton();
        waitForVisibility(amountInput);
        amountInput.clear();
        amountInput.sendKeys(amount);
        click(submitButton);

        // Wait for the transaction to complete
        try {
            Thread.sleep(2000); // Small wait for transaction processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //    ("Withdraw funds: {amount}")
    public void withdrawFunds(String amount) {
        waitForVisibility(withdrawalButton);
        clickWithdrawalButton();
        waitForVisibility(amountInput);
        amountInput.clear();
        amountInput.sendKeys(amount);
        click(submitButton);

        // Wait for the transaction to complete
        try {
            Thread.sleep(1000); // Small wait for transaction processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //    ("Get transaction status message")
    public String getTransactionStatus() {
        try {
            waitForVisibility(transactionStatusMessage);
            return transactionStatusMessage.getText().trim();
        } catch (Exception e) {
            return "No status message found";
        }
    }


    //    ("Check if transaction status message is present")
    public boolean isTransactionStatusMessagePresent() {
        try {
            return transactionStatusMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    //    ("Check if alert is present")
    public boolean isAlertPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }


    //    ("Accept alert if present")
    public void acceptAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            // No alert present, do nothing
        }
    }


    //    ("Navigate to Transactions page")
    public TransactionsPage clickTransactions() {
        click(transactionsButton);
        return new TransactionsPage(driver, wait);
    }


    //    ("Logout from customer account")
    public CustomerLoginPage clickLogout() {
        click(logoutButton);
        return new CustomerLoginPage(driver, wait);
    }


   //    ("Select account: {accountNumber}")
    public void selectAccount(String accountNumber) {
        if (isElementPresent(accountDropdown)) {
            waitForVisibility(accountDropdown);
            selectByVisibleText(accountDropdown, accountNumber);
        }
    }

    private void selectByVisibleText(WebElement accountDropdown, String accountNumber) {
    }


    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    //    @Step("Refresh account page")
    public void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }


    private void waitForPageLoad() {
        try {
            waitForVisibility(welcomeMessage);
            waitForVisibility(currentBalance);
        } catch (Exception e) {
            // Page might not be fully loaded, continue
        }
    }
}