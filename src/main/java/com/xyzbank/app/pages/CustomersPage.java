package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersPage extends BasePage {

    // Web Elements on the Customers Page
    @FindBy(css = "input[placeholder='Search Customer']")
    private WebElement searchCustomerInput;

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']/tbody")
    private WebElement customersTableBody;

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']/thead/tr")
    private WebElement customersTableHeader;

    // Constructor
    public CustomersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Enters text into the search customer input field.
     * @param searchTerm The text to search for (e.g., first name, last name, postcode).
     */
    public void searchCustomer(String searchTerm) {
        // Clear the search input first to ensure a fresh search
        searchCustomerInput.clear();
        type(searchCustomerInput, searchTerm);
    }

    /**
     * Finds a customer row by customer name and clicks the delete button for that customer.
     * Assumes customerName is either full name, first name, or last name that uniquely identifies the row.
     * @param customerName The name of the customer to delete.
     * @return True if the customer was found and deleted, false otherwise.
     */
    public boolean deleteCustomer(String customerName) {
        waitForVisibility(customersTableBody);
        List<WebElement> rows = customersTableBody.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String firstName = cells.get(0).getText().trim();
            String lastName = cells.get(1).getText().trim();
            String fullName = firstName + " " + lastName;

            if (fullName.equalsIgnoreCase(customerName) ||
                    firstName.equalsIgnoreCase(customerName) ||
                    lastName.equalsIgnoreCase(customerName)) {
                WebElement deleteButton = row.findElement(By.cssSelector("button[ng-click='deleteCust(cust)']"));
                click(deleteButton);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a customer is present in the table after a search or filter.
     * @param customerName The full name of the customer (e.g., "Hermione Granger").
     * @return True if the customer is found in the displayed table, false otherwise.
     */
    public boolean isCustomerPresent(String customerName) {
        waitForVisibility(customersTableBody);
        List<WebElement> rows = customersTableBody.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            String rowText = row.getText();
            if (rowText.contains(customerName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves details for a specific customer from the table.
     * @param customerName The full name of the customer (e.g., "Harry Potter").
     * @return A Map of customer details (header -> value), or null if not found.
     */
    public Map<String, String> getCustomerDetails(String customerName) {
        waitForVisibility(customersTableBody);

        // Get header names
        List<WebElement> headerCells = customersTableHeader.findElements(By.tagName("td"));
        List<String> headers = new ArrayList<>();
        for (WebElement headerCell : headerCells) {
            headers.add(headerCell.getText().trim());
        }

        List<WebElement> rows = customersTableBody.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String currentCustomerFullName = cells.get(0).getText().trim() + " " + cells.get(1).getText().trim();

            if (currentCustomerFullName.equalsIgnoreCase(customerName)) {
                Map<String, String> customerDetails = new HashMap<>();
                for (int i = 0; i < headers.size() && i < cells.size(); i++) {
                    customerDetails.put(headers.get(i), cells.get(i).getText().trim());
                }
                return customerDetails;
            }
        }
        return null; // Customer not found
    }

    /**
     * Gets the number of customers displayed in the table.
     * @return The count of customer rows (excluding header).
     */
    public int getCustomerCount() { // <--- NEW METHOD ADDED HERE
        waitForVisibility(customersTableBody);
        List<WebElement> rows = customersTableBody.findElements(By.tagName("tr"));
        return rows.size();
    }


    /**
     * Verifies if the Customers Page is displayed by checking key elements.
     * @return True if key elements are visible, false otherwise.
     */
    public boolean isCustomersPageDisplayed() {
        try {
            waitForVisibility(searchCustomerInput);
            waitForVisibility(customersTableBody);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}