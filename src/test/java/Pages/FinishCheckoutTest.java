package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static SeleniumTest.ScriptOneTest.driver;

public class FinishCheckoutTest {

    private static final String finishButton = "//button[@id='finish']";
    private static final String thankYouHeader = "//h2[normalize-space()='Thank you for your order!']";

    private static void waitForElement(By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void finishCheckoutAndVerify() {
        waitForElement(By.xpath(finishButton));
        driver.findElement(By.xpath(finishButton)).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(thankYouHeader)));    }

    public static String getThankYouText() {
        return driver.findElement(By.xpath(thankYouHeader)).getText();
    }
}
