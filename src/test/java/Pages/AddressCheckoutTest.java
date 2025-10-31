package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static SeleniumTest.ScriptOneTest.driver;

public class AddressCheckoutTest {

    private static final String firstNameField = "//input[@id='first-name']";
    private static final String lastNameField = "//input[@id='last-name']";
    private static final String postalCodeField = "//input[@id='postal-code']";
    private static final String continueButton = "//input[@id='continue']";

    private static void waitForElement(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void fillAddressAndContinue(String firstName, String lastName, String postalCode) {
        waitForElement(By.xpath(firstNameField));
        driver.findElement(By.xpath(firstNameField)).clear();
        driver.findElement(By.xpath(firstNameField)).sendKeys(firstName);

        waitForElement(By.xpath(lastNameField));
        driver.findElement(By.xpath(lastNameField)).clear();
        driver.findElement(By.xpath(lastNameField)).sendKeys(lastName);

        waitForElement(By.xpath(postalCodeField));
        driver.findElement(By.xpath(postalCodeField)).clear();
        driver.findElement(By.xpath(postalCodeField)).sendKeys(postalCode);

        waitForElement(By.xpath(continueButton));
        driver.findElement(By.xpath(continueButton)).click();

        // Wait until redirected to checkout overview page
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("checkout-step-two.html"));
    }
}
