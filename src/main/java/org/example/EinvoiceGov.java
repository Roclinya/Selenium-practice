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

public class EinvoiceGov {

    WebDriver driver;
    String loginUrl = "https://www.einvoice.nat.gov.tw/accounts/login";

    String universalId = "";
    String username;
    String password;
    // 登入畫面用來確認頁面載入完成的參考元件,目前抓取 <li>姓名：林鳳湄</li> 做為參考標的
    private static final String LOCATED_ELEMENT_AFTER_LOGIN = "/html/body/div/div/div[2]/div[2]/div/div[1]/div[2]/ul/li[2]/a/div";
    private static final String FUNCTIONB2B_MENU = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/a[3]";
    private static final String FUNCTIONB2B_MENU_QRY_DOWN = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/div[3]/ul/li[2]/a/div/span";
    private static final String FUNCTIONB2B_MENU_QRY_DOWN_PRINT = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/div[3]/ul/li[2]/div/ul/li[1]/a/div/span";

    public static void main(String[] args) {

        EinvoiceGov nat = new EinvoiceGov("test","test", "test");
        nat.init();

        nat.login();

         nat.execute();
        // nat.getVerifyCode();
        // setupQuery();
        // download();


    }

    public EinvoiceGov(String universalId, String username, String password) {
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

    /**
     * To change the logging output to save to a specific file
     * @param prefix
     * @param suffix
     * @return
     */
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
        System.out.println("Execute ,keep going");
        //#創造一個顯性等待，等待時間10秒，搜尋頻率0.5秒一次(預設)
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //取得[營業人功能選單]的元素位置
        WebElement menuDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(FUNCTIONB2B_MENU)
        ));
        menuDiv.click();
        //取得[查詢與下載]的元素位置
        WebElement qryDownDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(FUNCTIONB2B_MENU_QRY_DOWN)
        ));
        qryDownDiv.click();
        //取得[發票查詢/列印/下載]的元素位置
        WebElement qryDownPrintDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(FUNCTIONB2B_MENU_QRY_DOWN_PRINT)
        ));
        qryDownPrintDiv.click();

        System.out.println("FUNCTIONB2B_MENU expanded");
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
                By.xpath(LOCATED_ELEMENT_AFTER_LOGIN)
        ));



//        // identify element
//        WebElement element = driver.findElement(By.id("user_id"));
//        element.sendKeys("55688");
//        // obtain the value entered with getAttribute method
//        System.out.println("Value entered is: " +element.getAttribute("value"));

        ChromeOptions chromeOptions = getDefaultChromeOptions();
        String name = chromeOptions.getBrowserName();
        System.out.println("Browser: "+name);

        //使用者點選[營業人扣繳單位]
        targetDiv.click();
        // 輸入[統一編號]
        WebElement businessIdElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.name("ban"))));
        businessIdElement.sendKeys("16312227");
        // 輸入[帳號]
        WebElement userElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.id("user_id"))));
        userElement.sendKeys("16312227");
        // 輸入[密碼]
        WebElement pwdElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.id("user_password"))));
        pwdElement.sendKeys("Aci16312227");
        // End the session
        System.out.println("wait for loginMark");

        WebElement loginMark = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div/div[2]/nav/div[1]/ul/li[2]")
        ));

        System.out.println("Login ,keep going");



    }


}
