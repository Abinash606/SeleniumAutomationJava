package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static SeleniumTest.ScriptOneTest.driver;
public class LoginPageTest {
    public static String userName = "//input[@id='user-name']";
    public static  String password = "//input[@id='password']";
    public  static  String button = "//input[@id='login-button']";
    public static String errorMsg = "//h3[@data-test='error']";


    public static void loginMethod( String user, String pass)
    {
        driver.findElement(By.xpath(userName)).sendKeys(user);
        driver.findElement(By.xpath(password)).sendKeys(pass);
        driver.findElement(By.xpath(button)).click();

    }

    public static String getErrorMessage()
    {
        try {
            WebElement error = driver.findElement(By.xpath(errorMsg));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }
    public static boolean isLoginSuccessful() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("inventory.html"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
