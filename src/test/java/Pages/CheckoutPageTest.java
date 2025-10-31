package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static SeleniumTest.ScriptOneTest.driver;

public class CheckoutPageTest {

    private static final String cartIcon = "//a[@class='shopping_cart_link']";
    private static final String checkoutButton = "//button[@id='checkout']";

    private static void waitForElement(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickCartIcon() {
        waitForElement(By.xpath(cartIcon));
        driver.findElement(By.xpath(cartIcon)).click();
    }

    public static void clickCheckoutButton() {
        waitForElement(By.xpath(checkoutButton));
        driver.findElement(By.xpath(checkoutButton)).click();
    }

    public static void proceedToCheckout() {
        clickCartIcon();
        By cartTitle = By.xpath("//span[@class='title' and text()='Your Cart']");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(cartTitle));
        clickCheckoutButton();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }
}
