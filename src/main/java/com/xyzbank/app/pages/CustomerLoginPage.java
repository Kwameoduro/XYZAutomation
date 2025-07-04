package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select; // Import for dropdown handling

public class CustomerLoginPage extends BasePage {

    // Web Elements on the Customer Login Page
    @FindBy(id = "userSelect")
    private WebElement customerNameDropdown;

    @FindBy(css = "button[type='submit']") // Keeping your CSS selector, it's good
    private WebElement loginButton;

    // Constructor
    public CustomerLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait); // Call the BasePage constructor
    }

    /**
     * Selects a customer's name from the dropdown.
     * Renamed from selectCustomerName to selectCustomer for consistency with tests.
     * @param customerName The full name of the customer to select (e.g., "Harry Potter").
     */
    public void selectCustomer(String customerName) { // Renamed from selectCustomerName
        waitForVisibility(customerNameDropdown); // Ensure dropdown is visible
        Select select = new Select(customerNameDropdown);
        select.selectByVisibleText(customerName);
    }

    /**
     * Clicks the Login button and returns the AccountPage object.
     * @return A new instance of AccountPage, representing the customer's account dashboard.
     */
    public AccountPage clickLoginButton() {
        click(loginButton);
        return new AccountPage(driver, wait); // This will be the next page
    }

    /**
     * Performs the full customer login sequence.
     * Renamed from customerLogin to loginAsCustomer for consistency with tests.
     * @param customerName The name of the customer to log in as.
     * @return The AccountPage object after successful login.
     */
    public AccountPage loginAsCustomer(String customerName) { // Renamed from customerLogin
        selectCustomer(customerName); // Calls the renamed method
        return clickLoginButton();
    }

    /**
     * Verifies if the Customer Login Page is displayed.
     * @return True if the key elements are visible, false otherwise.
     */
    public boolean isCustomerLoginPageDisplayed() {
        try {
            waitForVisibility(customerNameDropdown);
            waitForVisibility(loginButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}