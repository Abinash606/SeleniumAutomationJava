package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

import static SeleniumTest.ScriptOneTest.driver;

public class ProductPageTest {

    private static final String productOne = "//div[contains(@class,'inventory_item_name') and contains(text(),'Sauce Labs Backpack')]";
    private static final String productTwo = "//div[contains(@class,'inventory_item_name') and contains(text(),'Sauce Labs Bike Light')]";
    private static final String addToCartBtn1 = "//button[@id='add-to-cart-sauce-labs-backpack']";
    private static final String addToCartBtn2 = "//button[@id='add-to-cart-sauce-labs-bike-light']";
    private static final String cartBadge = "//span[@class='shopping_cart_badge']";

    private static void waitForVisibility(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static void waitForClickable(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void productList() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_list")));
        waitForVisibility(By.xpath(productOne), 20);
        Assert.assertTrue(driver.findElement(By.xpath(productOne)).isDisplayed(),
                "Product 1 (Sauce Labs Backpack) not displayed!");

        waitForVisibility(By.xpath(productTwo), 20);
        Assert.assertTrue(driver.findElement(By.xpath(productTwo)).isDisplayed(),
                "Product 2 (Sauce Labs Bike Light) not displayed!");
    }

    public static void productAddToCart() {
        waitForClickable(By.xpath(addToCartBtn1), 10);
        driver.findElement(By.xpath(addToCartBtn1)).click();

        waitForClickable(By.xpath(addToCartBtn2), 10);
        driver.findElement(By.xpath(addToCartBtn2)).click();

        waitForVisibility(By.xpath(cartBadge), 10);

        String cartCount = driver.findElement(By.xpath(cartBadge)).getText();
        Assert.assertEquals(cartCount, "2", "Cart count does not match after adding products!");
    }

    public static int getCartCount() {
        waitForVisibility(By.xpath(cartBadge), 10);
        return Integer.parseInt(driver.findElement(By.xpath(cartBadge)).getText());
    }
}
