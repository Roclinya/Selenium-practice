package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *  selenium ver 3.141.59
 */

public class EnvoiceGov {

    WebDriver driver;
    String loginUrl = "https://www.einvoice.nat.gov.tw/accounts/login";

    String universalId = "";
    String username;
    String password;

    public static void main(String[] args) {

        EnvoiceGov nat = new EnvoiceGov("test","test", "test");
        nat.init();
        nat.login();

        // nat.getVerifyCode();
        // setupQuery();
        // download();

    }

    public EnvoiceGov(String universalId, String username, String password) {
        this.universalId = universalId;
        this.username = username;
        this.password = password;
    }

    public void init() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Larry\\OneDrive - 緯創軟體股份有限公司\\chromedriver-win64\\chromedriver.exe");
        driver = new org.openqa.selenium.chrome.ChromeDriver();
        driver.manage().window().maximize();
    }

    public void login() {
        driver.get(loginUrl);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement targetDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div/div/div[2]/div[2]/div/div[1]/div[2]/ul/li[2]/a/div")
        ));

        targetDiv.click();
    }

}
