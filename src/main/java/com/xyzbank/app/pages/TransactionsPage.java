package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
//import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsPage extends BasePage {

    // Web Elements on the Transactions Page
    @FindBy(css = "button[ng-click='back()']")
    private WebElement backButton;

    @FindBy(id = "start")
    private WebElement startDateInput;

    @FindBy(id = "end")
    private WebElement endDateInput;

    @FindBy(css = "button[ng-click='reset()']")
    private WebElement resetButton;

    @FindBy(css = "table")
    private WebElement transactionsTable;

    @FindBy(css = "table tbody")
    private WebElement transactionsTableBody;

    @FindBy(css = "table thead tr")
    private WebElement transactionsTableHeader;

    // Constructor
    public TransactionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    //    ("Verify Transactions Page is displayed")
    public boolean isTransactionsPageDisplayed() {
        try {
            waitForVisibility(backButton);
            waitForVisibility(transactionsTable);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //    ("Set start date filter: {startDate}")
    public void setStartDate(String startDate) {
        waitForVisibility(startDateInput);
        startDateInput.clear();
        startDateInput.sendKeys(startDate);
    }


    //    ("Set end date filter: {endDate}")
    public void setEndDate(String endDate) {
        waitForVisibility(endDateInput);
        endDateInput.clear();
        endDateInput.sendKeys(endDate);
    }


    //    ("Get start date value")
    public String getStartDateValue() {
        waitForVisibility(startDateInput);
        return startDateInput.getAttribute("value");
    }


    //    ("Get end date value")
    public String getEndDateValue() {
        waitForVisibility(endDateInput);
        return endDateInput.getAttribute("value");
    }


    //    ("Click Reset button")
    public void clickResetButton() {
        click(resetButton);
        // Small wait for the reset to take effect
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


     //    ("Navigate back to Account page")
    public AccountPage clickBackButton() {
        click(backButton);
        return new AccountPage(driver, wait);
    }


    //    ("Get all transaction table data")
    public List<Map<String, String>> getTransactionsTableData() {
        waitForVisibility(transactionsTable);
        List<Map<String, String>> transactions = new ArrayList<>();

        try {
            // Get header names
            List<WebElement> headerCells = transactionsTableHeader.findElements(By.tagName("td"));
            List<String> headers = new ArrayList<>();
            for (WebElement headerCell : headerCells) {
                headers.add(headerCell.getText().trim());
            }

            // Get transaction rows
            List<WebElement> rows = transactionsTableBody.findElements(By.tagName("tr"));

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= headers.size()) {
                    Map<String, String> transaction = new HashMap<>();
                    for (int i = 0; i < headers.size() && i < cells.size(); i++) {
                        transaction.put(headers.get(i), cells.get(i).getText().trim());
                    }
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving transaction data: " + e.getMessage());
        }

        return transactions;
    }


     //    ("Get transactions count")
    public int getTransactionsCount() {
        try {
            waitForVisibility(transactionsTableBody);
            List<WebElement> rows = transactionsTableBody.findElements(By.tagName("tr"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }


     //    ("Get transaction data by index: {index}")
    public Map<String, String> getTransactionByIndex(int index) {
        List<Map<String, String>> allTransactions = getTransactionsTableData();
        if (index >= 0 && index < allTransactions.size()) {
            return allTransactions.get(index);
        }
        return null;
    }


     //    ("Get most recent transaction")
    public Map<String, String> getMostRecentTransaction() {
        List<Map<String, String>> allTransactions = getTransactionsTableData();
        if (!allTransactions.isEmpty()) {
            return allTransactions.get(allTransactions.size() - 1);
        }
        return null;
    }


    //    ("Filter transactions by type: {transactionType}")
    public List<Map<String, String>> getTransactionsByType(String transactionType) {
        List<Map<String, String>> allTransactions = getTransactionsTableData();
        List<Map<String, String>> filteredTransactions = new ArrayList<>();

        for (Map<String, String> transaction : allTransactions) {
            if (transactionType.equalsIgnoreCase(transaction.get("Transaction Type")) ||
                    transactionType.equalsIgnoreCase(transaction.get("Type"))) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }


    //    ("Calculate total amount for transaction type: {transactionType}")
    public double getTotalAmountByType(String transactionType) {
        List<Map<String, String>> transactions = getTransactionsByType(transactionType);
        double totalAmount = 0.0;

        for (Map<String, String> transaction : transactions) {
            try {
                String amountStr = transaction.get("Amount");
                if (amountStr != null && !amountStr.isEmpty()) {
                    totalAmount += Double.parseDouble(amountStr);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing amount: " + transaction.get("Amount"));
            }
        }

        return totalAmount;
    }

    //    ("Check if transaction exists")
    public boolean isTransactionPresent(Map<String, String> expectedTransaction) {
        List<Map<String, String>> allTransactions = getTransactionsTableData();

        for (Map<String, String> transaction : allTransactions) {
            boolean matches = true;
            for (Map.Entry<String, String> expectedEntry : expectedTransaction.entrySet()) {
                String expectedValue = expectedEntry.getValue();
                String actualValue = transaction.get(expectedEntry.getKey());

                if (!expectedValue.equals(actualValue)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return true;
            }
        }

        return false;
    }


    //    ("Wait for transactions table to load")
    public void waitForTransactionsToLoad() {
        waitForVisibility(transactionsTable);
        try {
            Thread.sleep(1000); // Allow time for data to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //    ("Refresh transactions page")
    public void refreshTransactions() {
        driver.navigate().refresh();
        waitForTransactionsToLoad();
    }
}