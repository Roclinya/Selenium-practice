package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

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
//        nat.execute();
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
        //Start the session
//        driver = new org.openqa.selenium.chrome.ChromeDriver();
        getCrhomeDriver();
        driver.manage().window().maximize();
    }

    private void getCrhomeDriver() {
        ChromeDriverService service = new ChromeDriverService.Builder().withLogFile(getTempFile("logsToFile", ".log")).build();
        driver = new ChromeDriver(service);
    }

    protected static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--no-sandbox");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return options;
    }

    protected File getTempFile(String prefix, String suffix) {
        File logLocation = null;
        logLocation = new File(System.getProperty("user.dir")+"/"+prefix+ suffix);

        return logLocation;
//        File logLocation = null;
//        try {
//            logLocation = File.createTempFile(prefix, suffix);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        logLocation.deleteOnExit();
//        return logLocation;
    }

    public void execute(){
        //Take action on browser
        driver.get(loginUrl);
        //#創造一個顯性等待，等待時間10秒
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //取得營業人扣繳單位的元素位置
        WebElement targetDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/a[3]/div/span[2]")
        ));

        targetDiv.click();




    }

    public void login() {
        //Take action on browser
        driver.get(loginUrl);

        //Essentially you want to make sure that the element is on the page before you attempt to locate it
        //and the element is in an interactable state before you attempt to interact with it.
        // 為了更好的讓程式運Options to describe the kind of session you want; default values are used for local行，或多或少會需要使用等待方式，順利的讓driver取得元素後順利運行
        // https://vocus.cc/article/64c6009dfd897800017eba7c

        //#創造一個顯性等待，等待時間10秒
        WebDriverWait wait = new WebDriverWait(driver, 20);

        //取得營業人扣繳單位的元素位置
        WebElement targetDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div/div/div[2]/div[2]/div/div[1]/div[2]/ul/li[2]/a/div")
        ));



//        // identify element
//        WebElement element = driver.findElement(By.id("user_id"));
//        element.sendKeys("55688");
//        // obtain the value entered with getAttribute method
//        System.out.println("Value entered is: " +element.getAttribute("value"));

        ChromeOptions chromeOptions = getDefaultChromeOptions();
        String name = chromeOptions.getBrowserName();
        System.out.println("name: "+name);

        //使用者點選[營業人扣繳單位]
        targetDiv.click();
        // 輸入[帳號]
        WebElement userElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.id("user_id"))));
        userElement.sendKeys("55688");
        // 輸入[密碼]
        WebElement pwdElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.id("user_password"))));
        pwdElement.sendKeys("55688");
        // End the session
        System.out.println("wait");

        WebElement loginMark = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div/div[2]/nav/div[1]/ul/li[2]")
        ));

        System.out.println("keep going");
        driver.quit();
    }


}
