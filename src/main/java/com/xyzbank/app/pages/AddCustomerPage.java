package com.xyzbank.app.pages;

import com.xyzbank.app.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddCustomerPage extends BasePage {


    @FindBy(css = "input[ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(css = "input[ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(css = "input[ng-model='postCd']")
    private WebElement postCodeInput;

    @FindBy(css = "button[type='submit']")
    private WebElement addCustomerSubmitButton;

    // Constructor
    public AddCustomerPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    public void enterFirstName(String firstName) {
        type(firstNameInput, firstName);
    }


    public void enterLastName(String lastName) {
        type(lastNameInput, lastName);
    }


    public void enterPostCode(String postCode) {
        type(postCodeInput, postCode);
    }


    public void clickAddCustomerSubmitButton() {
        click(addCustomerSubmitButton);
    }


    public String addCustomer(String firstName, String lastName, String postCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostCode(postCode);
        clickAddCustomerSubmitButton();
        return getAlertTextAndAccept(); // This will now call the method from BasePage
    }


    public boolean isAddCustomerPageDisplayed() {
        try {
            waitForVisibility(firstNameInput);
            waitForVisibility(lastNameInput);
            waitForVisibility(postCodeInput);
            waitForVisibility(addCustomerSubmitButton);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}