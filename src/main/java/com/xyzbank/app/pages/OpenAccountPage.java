package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select; // Import for dropdown handling
import org.openqa.selenium.support.ui.WebDriverWait;

public class OpenAccountPage extends BasePage {

    // Web Elements on the Open Account Page
    @FindBy(id = "userSelect")
    private WebElement customerNameDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(css = "button[type='submit']") // This is the 'Process' button
    private WebElement processButton;

    // Constructor
    public OpenAccountPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /**
     * Selects a customer's name from the dropdown.
     * @param customerName The full name of the customer to select (e.g., "Harry Potter").
     */
    public void selectCustomerName(String customerName) {
        waitForVisibility(customerNameDropdown); // Ensure dropdown is visible
        Select select = new Select(customerNameDropdown);
        select.selectByVisibleText(customerName);
    }

    /**
     * Selects a currency from the dropdown.
     * @param currency The currency to select (e.g., "Dollar", "Pound", "Rupee").
     */
    public void selectCurrency(String currency) {
        waitForVisibility(currencyDropdown); // Ensure dropdown is visible
        Select select = new Select(currencyDropdown);
        select.selectByVisibleText(currency);
    }

    /**
     * Clicks the 'Process' button to open the account.
     * This action is expected to trigger a JavaScript alert.
     */
    public void clickProcessButton() {
        click(processButton);
    }

    /**
     * Handles the JavaScript alert that appears after opening an account.
     * Retrieves the alert text and accepts the alert.
     * @return The text of the alert message.
     */
    public String getAlertTextAndAccept() {
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept(); // Dismiss the alert
        return alertText;
    }

    /**
     * Performs the full operation of opening a new account for a customer.
     * @param customerName The name of the customer for whom to open the account.
     * @param currency The currency of the new account.
     * @return The alert message displayed after processing the account.
     */
    public String openAccount(String customerName, String currency) {
        selectCustomerName(customerName);
        selectCurrency(currency);
        clickProcessButton();
        return getAlertTextAndAccept();
    }

    /**
     * Verifies if the Open Account Page is displayed by checking key elements.
     * @return True if key elements are visible, false otherwise.
     */
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