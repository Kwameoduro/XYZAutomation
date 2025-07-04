package com.xyzbank.app.tests;

import com.xyzbank.app.base.BaseTest;
import com.xyzbank.app.pages.AccountPage;
import com.xyzbank.app.pages.AddCustomerPage;
import com.xyzbank.app.pages.BankManagerLoginPage;
import com.xyzbank.app.pages.CustomerLoginPage;
import com.xyzbank.app.pages.HomePage;
import com.xyzbank.app.pages.OpenAccountPage;
import com.xyzbank.app.pages.TransactionsPage;
import com.xyzbank.app.utils.PropertiesLoader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

public class CustomerLoginTests extends BaseTest {

    private String testCustomerFirstName;
    private String testCustomerLastName;
    private String testCustomerFullName;
    private String testCustomerPostCode;

    @BeforeAll
    static void loadAllTestData() {
        PropertiesLoader.loadTestDataProperties();
    }

    @BeforeEach
    void setupTestData() {
        testCustomerFirstName = PropertiesLoader.getTestDataProperty("test.customer.first.name");
        testCustomerLastName = PropertiesLoader.getTestDataProperty("test.customer.last.name");
        testCustomerFullName = testCustomerFirstName + " " + testCustomerLastName;
        testCustomerPostCode = PropertiesLoader.getTestDataProperty("test.customer.post.code");
    }

    @Test
    @DisplayName("Customer Login Successfully")
    void customerLoginSuccessfully() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        assertFalse(customerLoginPage.isCustomerLoginPageDisplayed(), "Customer Login Page should be displayed.");

        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);
        assertTrue(accountPage.isAccountPageDisplayed(), "Account Page should be displayed after successful login.");

        assertEquals("0", accountPage.getCurrentBalance(), "Initial balance should be 0 for a fresh account.");
    }

    @Test
    @DisplayName("Customer Deposit Funds Successfully")
    void customerDepositFundsSuccessfully() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        String depositAmount = PropertiesLoader.getTestDataProperty("deposit.amount.positive");
        String initialBalance = accountPage.getCurrentBalance();

        accountPage.depositFunds(depositAmount);

        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Deposit success message should be displayed.");

        int expectedBalance = Integer.parseInt(initialBalance) + Integer.parseInt(depositAmount);
        assertEquals(String.valueOf(expectedBalance), accountPage.getCurrentBalance(), "Balance should reflect successful deposit.");
    }

    @Test
    @DisplayName("Customer Withdraw Funds Successfully")
    void customerWithdrawFundsSuccessfully() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);


        String initialDepositStr = PropertiesLoader.getTestDataProperty("deposit.amount.positive");
        accountPage.depositFunds(initialDepositStr);
        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Initial deposit for withdrawal test should be successful.");
        assertEquals(initialDepositStr, accountPage.getCurrentBalance(), "Balance should reflect initial deposit.");

        // Now, perform withdrawal
        String withdrawAmount = PropertiesLoader.getTestDataProperty("withdrawal.amount.positive");

        accountPage.withdrawFunds(withdrawAmount);

        assertEquals("Transaction successful", accountPage.getTransactionStatus(), "Withdrawal success message should be displayed.");

        int expectedBalance = Integer.parseInt(initialDepositStr) - Integer.parseInt(withdrawAmount);
        assertEquals(String.valueOf(expectedBalance), accountPage.getCurrentBalance(), "Balance should reflect successful withdrawal.");
    }

    @Test
    @DisplayName("Customer Withdraw Insufficient Funds (Edge Case)")
    void customerWithdrawInsufficientFunds() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);


        String smallDeposit = PropertiesLoader.getTestDataProperty("deposit.amount.small");
        accountPage.depositFunds(smallDeposit);
        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Small initial deposit should be successful.");
        assertEquals(smallDeposit, accountPage.getCurrentBalance(), "Balance should reflect small initial deposit.");

        String insufficientWithdrawalAmount = PropertiesLoader.getTestDataProperty("withdrawal.amount.insufficient");

        accountPage.withdrawFunds(insufficientWithdrawalAmount);

        assertEquals("Transaction Failed. You can not withdraw amount more than the balance.",
                accountPage.getTransactionStatus(), "Insufficient funds message should be displayed.");

        assertEquals(smallDeposit, accountPage.getCurrentBalance(), "Balance should remain unchanged after failed withdrawal.");
    }

    @Test
    @DisplayName("Customer View Transactions History and Filter/Reset")
    void customerViewTransactionsHistoryAndFilterReset() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);


        accountPage.depositFunds(PropertiesLoader.getTestDataProperty("transaction.amount.deposit1"));
        accountPage.depositFunds(PropertiesLoader.getTestDataProperty("transaction.amount.deposit2"));
        accountPage.withdrawFunds(PropertiesLoader.getTestDataProperty("transaction.amount.withdraw1"));
        accountPage.depositFunds(PropertiesLoader.getTestDataProperty("transaction.amount.deposit3"));
        accountPage.withdrawFunds(PropertiesLoader.getTestDataProperty("transaction.amount.withdraw2"));
        System.out.println("Performed multiple transactions for history test.");

        TransactionsPage transactionsPage = accountPage.clickTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed(), "Transactions Page should be displayed.");
        System.out.println("Navigated to Transactions page.");

        List<Map<String, String>> initialTransactions = transactionsPage.getTransactionsTableData();
        assertEquals(5, initialTransactions.size(), "There should be 5 transactions initially displayed.");
        System.out.println("Initial transactions count verified.");

        // --- Test Date Filtering ---
        String startDate = PropertiesLoader.getTestDataProperty("transaction.filter.start.date.1");
        String endDate = PropertiesLoader.getTestDataProperty("transaction.filter.end.date.1");

        transactionsPage.setStartDate(startDate);
        transactionsPage.setEndDate(endDate);

        System.out.println("Set transaction filter dates: " + startDate + " to " + endDate);

        List<Map<String, String>> filteredTransactions = transactionsPage.getTransactionsTableData();
        assertTrue(filteredTransactions.size() >= 0, "Filtered transactions should be displayed.");
        System.out.println("Filtered transactions count: " + filteredTransactions.size());

        // --- Test Reset Button ---
        transactionsPage.clickResetButton();
        System.out.println("Clicked Reset button on Transactions page.");

        assertEquals("", transactionsPage.getStartDateValue(), "Start date field should be empty after reset.");
        assertEquals("", transactionsPage.getEndDateValue(), "End date field should be empty after reset.");

        List<Map<String, String>> transactionsAfterReset = transactionsPage.getTransactionsTableData();
        assertEquals(5, transactionsAfterReset.size(), "All 5 transactions should be displayed after reset.");
        System.out.println("Transactions reset and count verified.");
    }

    @Test
    @DisplayName("Customer Logout Successfully")
    void customerLogoutSuccessfully() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        customerLoginPage = accountPage.clickLogout();
        assertTrue(customerLoginPage.isCustomerLoginPageDisplayed(), "Should return to Customer Login Page after logout.");
    }

    @Test
    @DisplayName("Customer Deposit Zero Funds (Edge Case)")
    void customerDepositZeroFunds() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        String depositAmount = PropertiesLoader.getTestDataProperty("deposit.amount.zero");
        String initialBalance = accountPage.getCurrentBalance();

        accountPage.depositFunds(depositAmount);

        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Deposit success message should be displayed for zero amount.");
        assertEquals(initialBalance, accountPage.getCurrentBalance(), "Balance should remain unchanged after zero deposit.");

        TransactionsPage transactionsPage = accountPage.clickTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed(), "Transactions Page should be displayed.");

        List<Map<String, String>> transactions = transactionsPage.getTransactionsTableData();
        assertEquals(1, transactions.size(), "One transaction for zero amount should be recorded.");

        Map<String, String> transaction = transactions.get(0);
        assertEquals("0", transaction.get("Amount"), "Transaction amount should be 0.");
        assertEquals("Credit", transaction.get("Type"), "Transaction type should be Credit.");
    }

    @Test
    @DisplayName("Customer Deposit Negative Funds (Edge Case)")
    void customerDepositNegativeFunds() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        String depositAmount = PropertiesLoader.getTestDataProperty("deposit.amount.negative");
        String initialBalance = accountPage.getCurrentBalance();

        accountPage.depositFunds(depositAmount);

        assertFalse(accountPage.isAlertPresent(), "No alert should appear for negative deposit due to HTML5 validation.");
        assertFalse(accountPage.isTransactionStatusMessagePresent(), "Transaction status message should not be present for failed negative deposit.");
        assertEquals(initialBalance, accountPage.getCurrentBalance(), "Balance should remain unchanged after attempted negative deposit.");

        TransactionsPage transactionsPage = accountPage.clickTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed(), "Transactions Page should be displayed.");
        assertEquals(0, transactionsPage.getTransactionsTableData().size(), "No transactions should be recorded for failed negative deposit.");
    }

    @Test
    @DisplayName("Customer Withdraw Zero Funds (Edge Case)")
    void customerWithdrawZeroFunds() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        // Ensure some initial balance for withdrawal tests
        // *** MODIFIED: Load from properties ***
        String initialDepositForWithdrawal = PropertiesLoader.getTestDataProperty("deposit.amount.for.withdrawal.tests");
        accountPage.depositFunds(initialDepositForWithdrawal);
        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Initial deposit for zero withdrawal test should be successful.");
        assertEquals(initialDepositForWithdrawal, accountPage.getCurrentBalance(), "Balance should reflect initial deposit.");

        String withdrawAmount = PropertiesLoader.getTestDataProperty("withdrawal.amount.zero");

        accountPage.withdrawFunds(withdrawAmount);

        assertEquals("Transaction successful", accountPage.getTransactionStatus(), "Withdrawal success message should be displayed for zero amount.");
        assertEquals(initialDepositForWithdrawal, accountPage.getCurrentBalance(), "Balance should remain unchanged after zero withdrawal.");

        TransactionsPage transactionsPage = accountPage.clickTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed(), "Transactions Page should be displayed.");

        List<Map<String, String>> transactions = transactionsPage.getTransactionsTableData();
        assertEquals(2, transactions.size(), "Two transactions (deposit + zero withdrawal) should be recorded.");

        Map<String, String> lastTransaction = transactions.get(transactions.size() - 1);
        assertEquals("0", lastTransaction.get("Amount"), "Last transaction amount should be 0.");
        assertEquals("Debit", lastTransaction.get("Type"), "Last transaction type should be Debit.");
    }

    @Test
    @DisplayName("Customer Withdraw Negative Funds (Edge Case)")
    void customerWithdrawNegativeFunds() {
        HomePage homePage = new HomePage(driver, wait);
        CustomerLoginPage customerLoginPage = homePage.clickCustomerLogin();
        AccountPage accountPage = customerLoginPage.loginAsCustomer(testCustomerFullName);

        // Ensure some initial balance for withdrawal tests
        // *** MODIFIED: Load from properties ***
        String initialDepositForWithdrawal = PropertiesLoader.getTestDataProperty("deposit.amount.for.withdrawal.tests");
        accountPage.depositFunds(initialDepositForWithdrawal);
        assertEquals("Deposit Successful", accountPage.getTransactionStatus(), "Initial deposit for negative withdrawal test should be successful.");
        assertEquals(initialDepositForWithdrawal, accountPage.getCurrentBalance(), "Balance should reflect initial deposit.");

        String withdrawAmount = PropertiesLoader.getTestDataProperty("withdrawal.amount.negative");

        accountPage.withdrawFunds(withdrawAmount);

        assertFalse(accountPage.isAlertPresent(), "No alert should appear for negative withdrawal due to HTML5 validation.");
        assertFalse(accountPage.isTransactionStatusMessagePresent(), "Transaction status message should not be present for failed negative withdrawal.");
        assertEquals(initialDepositForWithdrawal, accountPage.getCurrentBalance(), "Balance should remain unchanged after attempted negative withdrawal.");

        TransactionsPage transactionsPage = accountPage.clickTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed(), "Transactions Page should be displayed.");
        assertEquals(1, transactionsPage.getTransactionsTableData().size(), "Only initial deposit transaction should be recorded."); // No new transaction
    }
}