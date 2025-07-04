package com.xyzbank.app.tests;

import com.xyzbank.app.base.BaseTest;
import com.xyzbank.app.pages.BankManagerLoginPage;
import com.xyzbank.app.pages.HomePage;
import com.xyzbank.app.pages.AddCustomerPage;
import com.xyzbank.app.pages.OpenAccountPage;
import com.xyzbank.app.pages.CustomersPage;
import com.xyzbank.app.utils.PropertiesLoader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankManagerTests extends BaseTest {

    // Load test data properties once for all tests in this class
    @BeforeAll
    static void loadAllTestData() {
        PropertiesLoader.loadTestDataProperties();
    }

    @Test
    @DisplayName("Verify Bank Manager Login Page is displayed")
    void verifyBankManagerLoginPageDisplay() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        assertTrue(bankManagerLoginPage.isBankManagerLoginPageDisplayed(), "Bank Manager Login Page should be displayed");
    }

    @Test
    @DisplayName("Add New Customer Successfully (Positive Scenario)")
    void addNewCustomerSuccessfully() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();
        assertTrue(addCustomerPage.isAddCustomerPageDisplayed(), "Add Customer Page should be displayed");

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.positive") + System.currentTimeMillis();
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.positive");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.positive");

        String alertMessage = addCustomerPage.addCustomer(firstName, lastName, postCode);

        assertTrue(alertMessage.contains("Customer added successfully with customer id :"),
                "Alert message should indicate successful customer addition.");

        // Verify customer is present in the Customers List
        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);
        bankManagerLoginPage.clickCustomers();
        CustomersPage customersPage = new CustomersPage(driver, wait);
        assertTrue(customersPage.isCustomersPageDisplayed(), "Customers Page should be displayed after navigation");

        customersPage.searchCustomer(firstName);
        assertTrue(customersPage.isCustomerPresent(firstName + " " + lastName), "Newly added customer should be present in the list.");
        // We can also assert that only one customer is displayed after searching for a unique one.
        assertEquals(1, customersPage.getCustomerCount(), "Expected 1 customer in the list after unique search."); // Corrected method call
    }

    @Test
    @DisplayName("Add Customer - Attempt to Add Duplicate Customer (Edge Case)")
    void attemptToAddDuplicateCustomer() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.duplicate");
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.duplicate");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.duplicate");

        addCustomerPage.addCustomer(firstName, lastName, postCode);

        String duplicateAlertMessage = addCustomerPage.addCustomer(firstName, lastName, postCode);

        assertTrue(duplicateAlertMessage.contains("Customer with the name '" + firstName + " " + lastName + "' already exists."),
                "Alert message should indicate duplicate customer.");
    }

    @Test
    @DisplayName("Add Customer - Empty First Name (Edge Case)")
    void addCustomerEmptyFirstName() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.empty");
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.positive");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.positive");

        addCustomerPage.enterFirstName(firstName);
        addCustomerPage.enterLastName(lastName);
        addCustomerPage.enterPostCode(postCode);
        addCustomerPage.clickAddCustomerSubmitButton();

        assertFalse(addCustomerPage.isAlertPresent(), "No alert should appear for empty first name due to HTML5 validation.");
        assertTrue(addCustomerPage.isAddCustomerPageDisplayed(), "Should remain on Add Customer page for empty first name.");
    }

    @Test
    @DisplayName("Add Customer - Empty Last Name (Edge Case)")
    void addCustomerEmptyLastName() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.positive");
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.empty");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.positive");

        addCustomerPage.enterFirstName(firstName);
        addCustomerPage.enterLastName(lastName);
        addCustomerPage.enterPostCode(postCode);
        addCustomerPage.clickAddCustomerSubmitButton();

        assertFalse(addCustomerPage.isAlertPresent(), "No alert should appear for empty last name due to HTML5 validation.");
        assertTrue(addCustomerPage.isAddCustomerPageDisplayed(), "Should remain on Add Customer page for empty last name.");
    }

    @Test
    @DisplayName("Add Customer - Empty Post Code (Edge Case)")
    void addCustomerEmptyPostCode() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.positive");
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.positive");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.empty");

        addCustomerPage.enterFirstName(firstName);
        addCustomerPage.enterLastName(lastName);
        addCustomerPage.enterPostCode(postCode);
        addCustomerPage.clickAddCustomerSubmitButton();

        assertFalse(addCustomerPage.isAlertPresent(), "No alert should appear for empty post code due to HTML5 validation.");
        assertTrue(addCustomerPage.isAddCustomerPageDisplayed(), "Should remain on Add Customer page for empty post code.");
    }

    @Test
    @DisplayName("Add Customer - Special Characters in Names/Postcode (Edge Case)")
    void addCustomerWithSpecialCharacters() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.special_char") + System.currentTimeMillis();
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.special_char");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.special_char");

        String alertMessage = addCustomerPage.addCustomer(firstName, lastName, postCode);

        assertTrue(alertMessage.contains("Customer added successfully with customer id :"),
                "Customer with special characters should be added successfully if app allows.");

        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);
        bankManagerLoginPage.clickCustomers();
        CustomersPage customersPage = new CustomersPage(driver, wait);
        customersPage.searchCustomer(firstName);
        assertTrue(customersPage.isCustomerPresent(firstName + " " + lastName), "Customer with special characters should be searchable.");
    }

    @Test
    @DisplayName("Add Customer - Max Length Boundary Test (Edge Case)")
    void addCustomerWithMaxLengthInputs() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();
        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();

        String firstName = PropertiesLoader.getTestDataProperty("add.customer.firstname.max_length") + System.currentTimeMillis(); // Ensure uniqueness
        String lastName = PropertiesLoader.getTestDataProperty("add.customer.lastname.max_length");
        String postCode = PropertiesLoader.getTestDataProperty("add.customer.postcode.max_length");

        String alertMessage = addCustomerPage.addCustomer(firstName, lastName, postCode);

        assertTrue(alertMessage.contains("Customer added successfully with customer id :"),
                "Customer with max length inputs should be added successfully if app accepts.");

        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);
        bankManagerLoginPage.clickCustomers();
        CustomersPage customersPage = new CustomersPage(driver, wait);
        customersPage.searchCustomer(firstName);
        assertTrue(customersPage.isCustomerPresent(firstName + " " + lastName), "Customer with max length should be searchable.");
    }

    @Test
    @DisplayName("Open Account for Existing Customer (Positive Scenario)")
    void openAccountForExistingCustomer() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();
        String customerFirstName = "AcctUser" + System.currentTimeMillis();
        String customerLastName = "TestLN";
        String customerPostCode = "B2B2B2";
        addCustomerPage.addCustomer(customerFirstName, customerLastName, customerPostCode);

        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);

        OpenAccountPage openAccountPage = bankManagerLoginPage.clickOpenAccount();
        assertTrue(openAccountPage.isOpenAccountPageDisplayed(), "Open Account Page should be displayed");

        String customerFullName = customerFirstName + " " + customerLastName;
        String currency = PropertiesLoader.getTestDataProperty("open.account.currency.positive");

        String alertMessage = openAccountPage.openAccount(customerFullName, currency);

        assertTrue(alertMessage.contains("Account created successfully with account Number :"),
                "Alert message should indicate successful account creation.");
    }

    @Test
    @DisplayName("Search for Existing Customer (Positive Scenario)")
    void searchExistingCustomer() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();
        String searchFirstName = PropertiesLoader.getTestDataProperty("search.customer.firstname.positive") + System.currentTimeMillis();
        String searchLastName = PropertiesLoader.getTestDataProperty("search.customer.lastname.positive");
        String searchPostCode = "C3C3C3";
        addCustomerPage.addCustomer(searchFirstName, searchLastName, searchPostCode);

        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);
        CustomersPage customersPage = bankManagerLoginPage.clickCustomers();
        assertTrue(customersPage.isCustomersPageDisplayed(), "Customers Page should be displayed");

        customersPage.searchCustomer(searchFirstName);

        assertTrue(customersPage.isCustomerPresent(searchFirstName + " " + searchLastName),
                "Searched customer should be present after search.");
        assertEquals(1, customersPage.getCustomerCount(), "Only one customer should be displayed after search."); // Corrected method call
    }

    @Test
    @DisplayName("Search for Non-Existent Customer (Edge Case)")
    void searchNonExistentCustomer() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        CustomersPage customersPage = bankManagerLoginPage.clickCustomers();
        assertTrue(customersPage.isCustomersPageDisplayed(), "Customers Page should be displayed");

        String nonExistentName = PropertiesLoader.getTestDataProperty("search.customer.nonexistent.name") + System.currentTimeMillis();

        customersPage.searchCustomer(nonExistentName);

        assertFalse(customersPage.isCustomerPresent(nonExistentName), "Non-existent customer should not be present after search.");
        assertEquals(0, customersPage.getCustomerCount(), "No customers should be displayed for a non-existent search."); // Corrected method call
    }

    @Test
    @DisplayName("Delete Existing Customer (Positive Scenario)")
    void deleteExistingCustomer() {
        HomePage homePage = new HomePage(driver, wait);
        BankManagerLoginPage bankManagerLoginPage = homePage.clickBankManagerLogin();

        AddCustomerPage addCustomerPage = bankManagerLoginPage.clickAddCustomer();
        String deleteFirstName = PropertiesLoader.getTestDataProperty("delete.customer.name.positive").split(" ")[0] + System.currentTimeMillis();
        String deleteLastName = PropertiesLoader.getTestDataProperty("delete.customer.name.positive").split(" ")[1];
        String deletePostCode = "D1D1D1";
        addCustomerPage.addCustomer(deleteFirstName, deleteLastName, deletePostCode);

        bankManagerLoginPage = new BankManagerLoginPage(driver, wait);
        CustomersPage customersPage = bankManagerLoginPage.clickCustomers();
        assertTrue(customersPage.isCustomersPageDisplayed(), "Customers Page should be displayed");

        String customerToDelete = deleteFirstName + " " + deleteLastName;

        customersPage.searchCustomer(deleteFirstName);
        assertTrue(customersPage.isCustomerPresent(customerToDelete), "Customer should be present before deletion.");

        assertTrue(customersPage.deleteCustomer(customerToDelete), "Should be able to find and click delete button for the customer.");

        customersPage.searchCustomer(deleteFirstName); // This will clear previous search and apply new one
        assertFalse(customersPage.isCustomerPresent(customerToDelete), "Customer should not be present after deletion.");
        assertEquals(0, customersPage.getCustomerCount(), "No customers should be displayed after deleting the only matching one."); // Corrected method call
    }
}