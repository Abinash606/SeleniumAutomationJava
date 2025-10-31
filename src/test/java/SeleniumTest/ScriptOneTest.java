package SeleniumTest;

import Pages.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class ScriptOneTest {

    public static WebDriver driver;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeClass
    public static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.get("https://www.saucedemo.com/");

        ExtentSparkReporter spark = new ExtentSparkReporter("target/SparkReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test(priority = 1)
    public void validLoginTest() {
        test = extent.createTest("Valid Login Test");
        try {
            LoginPageTest.loginMethod("standard_user", "secret_sauce");

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("inventory.html"));

            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                    "Valid login failed - not redirected to inventory page!");
            test.pass("Login successful - redirected to inventory page");
        } catch (Exception e) {
            test.fail("Login failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2)
    public void invalidLoginTest() {
        test = extent.createTest("Invalid Login Test");
        try {
            driver.navigate().to("https://www.saucedemo.com/");

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));

            LoginPageTest.loginMethod("invalid_user", "wrong_pass");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String error = LoginPageTest.getErrorMessage();
            Assert.assertTrue(error.contains("Username and password do not match"),
                    "Error message mismatch! Got: " + error);
            test.pass("Proper error displayed for invalid login");
        } catch (AssertionError e) {
            test.fail("Invalid login test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3)
    public void emptyFieldsTest() {
        test = extent.createTest("Empty Fields Login Test");
        try {
            driver.navigate().to("https://www.saucedemo.com/");

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));

            LoginPageTest.loginMethod("", "");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String error = LoginPageTest.getErrorMessage();
            Assert.assertTrue(error.contains("Username is required"),
                    "Empty field error message mismatch! Got: " + error);
            test.pass("Proper error displayed for empty login fields");
        } catch (AssertionError e) {
            test.fail("Empty fields login test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, dependsOnMethods = "validLoginTest")
    public void productPageTest() {
        test = extent.createTest("Add Products to Cart Test");
        try {
            if (!driver.getCurrentUrl().contains("inventory.html")) {
                driver.navigate().to("https://www.saucedemo.com/");
                LoginPageTest.loginMethod("standard_user", "secret_sauce");
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.urlContains("inventory.html"));
            }

            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("inventory.html"));

            ProductPageTest.productList();
            ProductPageTest.productAddToCart();

            int cartCount = ProductPageTest.getCartCount();
            Assert.assertEquals(cartCount, 2,
                    "Cart count mismatch! Expected: 2, Got: " + cartCount);
            test.pass("Products added to cart successfully - Cart count: " + cartCount);
        } catch (Exception e) {
            test.fail("Product page test failed: " + e.getMessage());
            System.err.println("Current URL: " + driver.getCurrentUrl());
            throw e;
        }
    }

    @Test(priority = 5, dependsOnMethods = "productPageTest")
    public void checkoutTest() {
        test = extent.createTest("Proceed to Checkout Test");
        try {
            CheckoutPageTest.proceedToCheckout();

            Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                    "Failed to navigate to Checkout page! Current URL: " + driver.getCurrentUrl());
            test.pass("Navigated to Checkout step one successfully");
        } catch (Exception e) {
            test.fail("Checkout test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6, dependsOnMethods = "checkoutTest")
    public void addressCheckoutTest() {
        test = extent.createTest("Fill Address and Continue Test");
        try {
            AddressCheckoutTest.fillAddressAndContinue("John", "Doe", "12345");

            Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two.html"),
                    "Failed to navigate to Checkout Overview page! Current URL: " + driver.getCurrentUrl());
            test.pass("Address filled and navigated to checkout overview successfully");
        } catch (Exception e) {
            test.fail("Address checkout test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 7, dependsOnMethods = "addressCheckoutTest")
    public void finishCheckoutTest() {
        test = extent.createTest("Finish Checkout Test");
        try {
            FinishCheckoutTest.finishCheckoutAndVerify();
            String thankYou = FinishCheckoutTest.getThankYouText();

            Assert.assertEquals(thankYou, "Thank you for your order!",
                    "Order completion message not found! Got: " + thankYou);
            test.pass("Checkout finished and Thank You page verified");
        } catch (Exception e) {
            test.fail("Finish checkout test failed: " + e.getMessage());
            throw e;
        }
    }

    @AfterTest
    public static void tearDown() {
        if (extent != null) {
            extent.flush();
        }
        if (driver != null) {
            driver.quit();
        }
    }
}