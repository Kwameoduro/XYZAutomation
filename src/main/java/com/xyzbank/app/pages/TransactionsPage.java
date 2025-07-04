package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsPage extends BasePage {

    // Web Elements on the Transactions Page
    @FindBy(css = "button[ng-click='back()']")
    private WebElement backButton;

    @FindBy(css = "button[ng-click='reset()']")
    private WebElement resetButton;

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']/tbody")
    private WebElement transactionsTableBody;

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']/thead/tr")
    private WebElement transactionsTableHeader;


    private By startDateInputLocator = By.id("start");
    private By endDateInputLocator = By.id("end");

    // Constructor
    public TransactionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public AccountPage clickBackButton() {
        click(backButton);
        return new AccountPage(driver, wait);
    }


    public void clickResetButton() {
        click(resetButton);
        // The app might display a confirmation alert for reset, be prepared to handle it if needed.
        // For XYZ Bank app, it doesn't appear to have an alert for reset, it just clears.
    }


    public int getTransactionsCount() {
        waitForVisibility(transactionsTableBody);
        List<WebElement> rows = transactionsTableBody.findElements(By.tagName("tr"));
        return rows.size();
    }


    public List<Map<String, String>> getTransactionsTableData() {
        List<Map<String, String>> allTransactions = new ArrayList<>();
        waitForVisibility(transactionsTableBody);

        // Get header names
        List<WebElement> headerCells = transactionsTableHeader.findElements(By.tagName("td")); // Assuming 'td' for header cells in thead
        if (headerCells.isEmpty()) { // Fallback if thead uses 'th' or no 'td'
            headerCells = transactionsTableHeader.findElements(By.tagName("th"));
        }

        List<String> headers = new ArrayList<>();
        for (WebElement headerCell : headerCells) {
            headers.add(headerCell.getText().trim());
        }

        List<WebElement> rows = transactionsTableBody.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Map<String, String> transactionData = new HashMap<>();
            for (int i = 0; i < headers.size() && i < cells.size(); i++) {
                transactionData.put(headers.get(i), cells.get(i).getText().trim());
            }
            allTransactions.add(transactionData);
        }
        return allTransactions;
    }


    public boolean isTransactionsPageDisplayed() {
        try {
            waitForVisibility(driver.findElement(startDateInputLocator)); // Use the locator to find the element
            waitForVisibility(backButton);
            waitForVisibility(resetButton);
            waitForVisibility(transactionsTableBody);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void setStartDate(String date) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInputLocator));
        element.clear();
        element.sendKeys(date);
    }


    public void setEndDate(String date) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInputLocator));
        element.clear();
        element.sendKeys(date);

    }


    public String getStartDateValue() { // Corrected: Returns String, takes no args
        return wait.until(ExpectedConditions.visibilityOfElementLocated(startDateInputLocator)).getAttribute("value");
    }


    public String getEndDateValue() { // Corrected: Returns String, takes no args
        return wait.until(ExpectedConditions.visibilityOfElementLocated(endDateInputLocator)).getAttribute("value");
    }
}