package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

/**
 *  selenium ver 3.141.59
 */

public class TestCase {

    WebDriver driver;
    String loginUrl = "https://www.einvoice.nat.gov.tw/accounts/login";

    String universalId = "";
    String username;
    String password;
    private static final String LINK ="/html/body/div[1]/div/div/div/div/main/div/div/div/div/table[1]/tbody/tr[1]/td[5]/div/span/a";
    private static final String INVISIBLE_LINK ="/html/body/div[1]/div/div/div/div/main/div/div/div/div/table[1]/tbody/tr[1]/td[5]/div/span/aX";

    public static void main(String[] args) {

        TestCase nat = new TestCase("test","test", "test");
        nat.init();
//        nat.execute();
        nat.test();
        // nat.getVerifyCode();
        // setupQuery();
        // download();

    }

    public TestCase(String universalId, String username, String password) {
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
        driver = new ChromeDriver(service,getDefaultChromeOptions());
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


    public void execute() {
        //Take action on browser
//        driver.get("https://www.selenium.dev/selenium/web/dynamic.html");
        //臺北小巨蛋廣場租用資訊
        driver.get("https://data.taipei/dataset/detail?id=bcf11ef9-9855-4ee3-8750-ca3df5aaf6c4");

        //#創造一個顯性等待，等待時間10秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        //取得 臺北小巨蛋廣場租用資訊_202503 位置
        WebElement targetDiv = wait.ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LINK)
        ));
        // 點選[下載]按鈕
        targetDiv.click();
//        WebElement revealed = driver.findElement(By.id("revealed"));
//        driver.findElement(By.id("reveal")).click();

//        Wait<WebDriver> wait =
//                new FluentWait<>(driver)
//                        .withTimeout(Duration.ofSeconds(2))
//                        .pollingEvery(Duration.ofMillis(300))
//                        .ignoring(ElementNotInteractableException.class);
//
//        wait.until(
//                d -> {
//                    revealed.sendKeys("Displayed");
//                    return true;
//                });
    }

    public void test(){

        //臺北小巨蛋廣場租用資訊
        driver.get("https://data.taipei/dataset/detail?id=bcf11ef9-9855-4ee3-8750-ca3df5aaf6c4");

        //#創造一個顯性等待，等待時間5秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // 測試 invisibilityOfElementLocated（應該會回傳 true，不會拋例外）
        boolean isInvisibleByLocator = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(INVISIBLE_LINK)));
        System.out.println("invisibilityOfElementLocated: " + isInvisibleByLocator);

// 測試 invisibilityOf（需先取得 WebElement，會觸發 NoSuchElementException）
        try {
            WebElement element = driver.findElement(By.id(INVISIBLE_LINK)); // 如果找不到會丟 NoSuchElementException
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOf(element));
            System.out.println("invisibilityOf: " + isInvisible);
        } catch (NoSuchElementException e) {
            System.out.println("invisibilityOf: NoSuchElementException caught as expected");
        }
    }


}
