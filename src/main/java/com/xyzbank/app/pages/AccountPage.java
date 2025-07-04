package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountPage extends BasePage {

    // Web Elements on the Account Page
    @FindBy(xpath = "//button[normalize-space()='Deposit']")
    private WebElement depositTab;

    @FindBy(xpath = "//button[normalize-space()='Withdrawl']")
    private WebElement withdrawlTab;

    @FindBy(xpath = "//button[normalize-space()='Transactions']")
    private WebElement transactionsTab;

    @FindBy(xpath = "//div[@ng-hide='noAccount']/strong[2]")
    private WebElement balanceDisplayElement;

    @FindBy(xpath = "//div[@ng-hide='noAccount']/strong[3]")
    private WebElement currencyDisplayElement;


    @FindBy(xpath = "//input[@placeholder='amount']")
    private WebElement amountInput;

    // Submit buttons for deposit and withdrawal (they have different text)
    @FindBy(xpath = "//button[@type='submit'][normalize-space()='Deposit']")
    private WebElement depositSubmitButton;


    @FindBy(xpath = "//form[@name='myForm']//button[normalize-space()='Withdraw']")

    private WebElement withdrawSubmitButton;


    @FindBy(xpath = "//span[@ng-show='message'] | //span[@class='error ng-binding'] | //span[@class='ng-binding ng-scope'] | //div[contains(@class, 'alert')]")
    private WebElement transactionStatusMessage;

    // Logout button
    @FindBy(xpath = "//button[normalize-space()='Logout']")
    private WebElement logoutButton;

    // Constructor
    public AccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public String getCurrentBalance() {
        waitForVisibility(balanceDisplayElement);
        String fullText = balanceDisplayElement.getText(); // e.g., "Balance : 100"
        return fullText.replaceAll("[^0-9]", "").trim(); // Remove non-digits
    }


    public String getTransactionStatus() {
        waitForVisibility(transactionStatusMessage);
        return transactionStatusMessage.getText().trim();
    }


    public void depositFunds(String amount) {
        System.out.println("DEBUG (AccountPage): Attempting to deposit: " + amount);
        click(depositTab); // Click the Deposit tab
        System.out.println("DEBUG (AccountPage): Deposit tab clicked. Waiting for amount input...");
        waitForVisibility(amountInput); // Ensure amount input is visible after tab switch
        type(amountInput, amount); // Type amount
        System.out.println("DEBUG (AccountPage): Amount typed. Clicking deposit button...");
        click(depositSubmitButton); // Click the Deposit button
        System.out.println("DEBUG (AccountPage): Deposit button clicked. Waiting for status message...");
        waitForVisibility(transactionStatusMessage); // Wait for the status message to appear
        System.out.println("DEBUG (AccountPage): Deposit status message appeared: " + getTransactionStatus());
    }


    public void withdrawFunds(String amount) {
        System.out.println("DEBUG (AccountPage): Attempting to withdraw: " + amount); // Added debug log

        // 1. Click the "Withdrawl" tab
        click(withdrawlTab);
        System.out.println("DEBUG (AccountPage): 'Withdrawl' tab clicked. Waiting for amount input..."); // Added debug log

        // 2. Wait for the amount input field to be visible and enter the amount
        waitForVisibility(amountInput); // This ensures the element is visible on the withdrawal form
        System.out.println("DEBUG (AccountPage): Amount input field is visible. Attempting to type amount..."); // Added debug log
        type(amountInput, amount); // Use your BasePage type method (which includes clear and sendKeys)
        System.out.println("DEBUG (AccountPage): Amount '" + amount + "' typed. Waiting for withdraw submit button..."); // Added debug log

        // 3. Click the actual "Withdraw" submit button
        waitForClickability(withdrawSubmitButton);
        click(withdrawSubmitButton); // Use your BasePage click method
        System.out.println("DEBUG (AccountPage): 'Withdraw' button clicked. Waiting for transaction status..."); // Added debug log

        // 4. Wait for the transaction status message to appear
        waitForVisibility(transactionStatusMessage);
        System.out.println("DEBUG (AccountPage): Transaction status message appeared: " + getTransactionStatus()); // Added debug log
    }


    public TransactionsPage clickTransactions() {
        System.out.println("DEBUG (AccountPage): Clicking Transactions tab.");
        click(transactionsTab);
        return new TransactionsPage(driver, wait);
    }


    public CustomerLoginPage clickLogout() {
        System.out.println("DEBUG (AccountPage): Clicking Logout button.");
        click(logoutButton);
        return new CustomerLoginPage(driver, wait);
    }


    public boolean isAccountPageDisplayed() {
        try {
            waitForVisibility(depositTab);
            waitForVisibility(withdrawlTab);
            waitForVisibility(transactionsTab);
            waitForVisibility(balanceDisplayElement);
            waitForVisibility(currencyDisplayElement);
            waitForVisibility(logoutButton);
            System.out.println("DEBUG (AccountPage): Account Page is displayed.");
            return true;
        } catch (Exception e) {
            System.err.println("DEBUG (AccountPage): Account Page is NOT displayed. Reason: " + e.getMessage());
            return false;
        }
    }


    public boolean isTransactionStatusMessagePresent() {
        try {
            // Using a short wait here to see if it becomes visible briefly
            wait.withTimeout(java.time.Duration.ofSeconds(1)).until(ExpectedConditions.visibilityOf(transactionStatusMessage));
            return transactionStatusMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}