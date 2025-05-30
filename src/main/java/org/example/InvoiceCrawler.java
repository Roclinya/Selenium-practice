package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * selenium ver 3.141.59
 */

public class InvoiceCrawler {

    //TODO: LOG.info to LOG.debug before upload this API

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCrawler.class);

    WebDriver driver;
    CrawlerUtils crawlerUtils;
    String loginUrl = "https://www.einvoice.nat.gov.tw/accounts/login";

    String universalId = "";
    String username;
    String password;
    // 登入畫面用來確認頁面載入完成的參考元件,目前抓取 <li>姓名：XXX </li> 做為參考標的
    private static final String LOCATED_ELEMENT_AFTER_LOGIN = "/html/body/div[1]/div/div[2]/nav/div[1]/ul/li[2]";
    private static final String BUSINESS_WITHHOLDING_AGENT = "/html/body/div/div/div[2]/div[2]/div/div[1]/div[2]/ul/li[2]/a/div";
    private static final String FUNCTIONB2B_MENU = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/a[3]";
    private static final String FUNCTIONB2B_MENU_QRY_DOWN = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/div[3]/ul/li[2]/a/div/span";
    private static final String FUNCTIONB2B_MENU_QRY_DOWN_PRINT = "/html/body/div[1]/div/div[2]/nav/div[1]/div/div[2]/div[3]/ul/li[2]/div/ul/li[1]/a/div/span";
    private static final String BUTTON_SEARCH = "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div/div/div[2]/ul/li[2]/button";
    private static final String BUTTON_DOWNLOAD_EXCEL = "/html/body/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div[1]/div/div[2]/div[1]/ul/li[4]/button";

    public static void main(String[] args) throws InterruptedException {

        InvoiceCrawler nat = new InvoiceCrawler("universalId", "username", "password");
        nat.initDriverAndUtilSettings();
        nat.login();
        nat.execute();
        // nat.getVerifyCode(); TODO: 驗證碼
        boolean result = nat.executeQuery();
        // Query成功才執行下載
        if (result) {
            nat.executeExcelDownload();
        } else {
            //TODO: 非必須業務邏輯 => 點選 非即時查詢tab
            WebDriver driverTest = nat.driver;
            WebDriverWait wait = new WebDriverWait(driverTest, Duration.ofSeconds(10));
            // 點選 非即時查詢tab
            WebElement offLineQuery = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.nav-link[href='/btb411w/offline']")));
            ((JavascriptExecutor) driverTest).executeScript("arguments[0].click();", offLineQuery);
        }
        LOG.info("------------------End of Execution---------------------");
    }

    public InvoiceCrawler(String universalId, String username, String password) {
        this.universalId = universalId;
        this.username = username;
        this.password = password;
    }

    public void initDriverAndUtilSettings() {
        //Start the session
        this.driver = DriverFactory.getDriver();
        // maximize the widow
        this.driver.manage().window().maximize();
        // initialize driver tools
        // TODO: Configuration for Timeouts: Instead of passing Duration to every operation, consider storing it as a configurable property
        //  in CrawlerUtils or using a configuration class.
        this.crawlerUtils = new CrawlerUtils(driver,Duration.ofSeconds(10));
    }

//    private void clickWhenClickable(By locator, WebDriverWait wait) {
//        try {
//            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//
//        } catch (TimeoutException e) {
//            LOG.error("Element not found within timeout：" + locator);
//        } catch (Exception e) {
//            LOG.error("點擊元素發生錯誤：" + locator + "，錯誤訊息：" + e.getMessage());
//            throw e;
//        }
//    }

    private void selectWhenClickable(By locator, String text,WebDriverWait wait) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            Select invoiceSelect = new Select(element);
            invoiceSelect.selectByVisibleText(text);

        } catch (TimeoutException e) {
            LOG.error("Element not found within timeout：" + locator);
        } catch (Exception e) {
            LOG.error("點擊元素發生錯誤：" + locator + "，錯誤訊息：" + e.getMessage());
            throw e;
        }
    }

    public void execute() throws InterruptedException {
        try {
//            //取得[營業人功能選單]的元素位置
//            crawlerUtils.clickWhenVisible(By.xpath(FUNCTIONB2B_MENU));
//            //取得[查詢與下載]的元素位置
//            crawlerUtils.clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN));
//            //取得[發票查詢/列印/下載]的元素位置
//            crawlerUtils.clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN_PRINT));

            //依序點選畫面選項  [營業人功能選單] > [查詢與下載] > [發票查詢/列印/下載]
            crawlerUtils.clickWhenVisible(By.xpath(FUNCTIONB2B_MENU))
            .clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN))
            .clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN_PRINT));

            // get option filter of Invoice period 取得發票期別 依照(期別、年月、日期)
            crawlerUtils.selectElement(By.id("input01"),"依日期");
            //TODO: test for 找不到資料的狀況
//            select.selectByVisibleText("依年月");
            //TODO:
//            selectCalendar(wait);
            // 1. 點擊 input 打開日期選擇器
            crawlerUtils.clickWhenVisible(By.id("dp-input-date02"));
//            WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dp-input-date02")));
//            dateInput.click();

            // 找到特定日期 2025-05-07(當下日曆開啟的月份)
//            WebElement todayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='2025-05-07']//div[contains(@class, 'dp__cell_inner')]")));
            //double click 第一次click點選想要的日期,第二次click是確認點選該日期
            crawlerUtils.doubleClickWhenClickable(By.xpath("//div[@id='2025-05-09']//div[contains(@class, 'dp__cell_inner')]"));


            // Radio選取 進項
            crawlerUtils.clickWhenClickable(By.id("queryInvType_1"));
//            WebElement queryInvType1Element = wait.until(ExpectedConditions.elementToBeClickable(By.id("queryInvType_1")));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", queryInvType1Element);

            // 發票狀態
            crawlerUtils.selectElement(By.id("input09"),"開立已確認");
//            selectWhenClickable(By.id("input09"),"開立已確認",wait);
//            WebElement invoiceElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("input09")));
//            Select invoiceSelect = new Select(invoiceElement);
//            invoiceSelect.selectByVisibleText("開立已確認");


        } catch (Exception e) {
            LOG.error("等待元件時出錯: " + e.getMessage(), e);
        }
    }

//    private static void selectCalendar(WebDriverWait wait) {
//        // 1. 點擊 input 打開日期選擇器
//
//        clickWhenVisible
//        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dp-input-date02")));
//        dateInput.click();
//
//        // 找到特定日期 2025-05-07(當下日曆開啟的月份)
//        WebElement todayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='2025-05-07']//div[contains(@class, 'dp__cell_inner')]")));
//
//        //第一次click點選想要的日期,第二次click是確認點選該日期
//        todayElement.click();
//        todayElement.click();
//    }

    private boolean executeQuery() {
        LOG.info("Query ,keep going");
        boolean isQuerySuccess = false;
        boolean isQueryFailed = false;
        try {
            // 執行查詢
            crawlerUtils.clickWhenClickable(By.xpath(BUTTON_SEARCH));

            try {
                // 使用較短時間等待  「提示視窗」(toast_box) 出現
//                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
//                WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toast_box .toast-body span")));
                WebElement toast = crawlerUtils.getWebElement(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toast_box .toast-body span")));
                String message = toast.getText().trim();
                LOG.info("「視窗提示訊息」內容為：" + message);

                if (message.contains("查詢成功")) {
                    isQuerySuccess = true;
                    LOG.info("查詢成功，繼續流程。");
                    // 繼續執行其他資料回傳true
                    return true;
                } else {
                    isQueryFailed = true;
                    LOG.warn("查詢失敗，請檢查查詢條件。");
                    return false;
                }

            } catch (TimeoutException e) {
                LOG.info("未偵測到「視窗提示訊息」，可能未正確觸發查詢。");
            }

        } catch (Exception e) {
            LOG.error("等待元件時出錯: " + e.getMessage(), e);
        }
        return isQuerySuccess;
    }

    private void executeExcelDownload() throws InterruptedException {
        LOG.info("ExcelDownload ,keep going");
        int pageCount = 1;
        // 設定最大頁數(查詢筆數上限為200筆 & 防止無窮迴圈)
        int maxPageCount = 100;
        while (pageCount <= maxPageCount) {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Step 1: 勾選所有檔案
            clickCheckboxWithRetry(By.id("checkbox-all"), 100);
            // Step 1.1: 檢查檔案勾選數量 => 檢查所有 id 為 checkbox-* 的 <input>
            crawlerUtils.countSelectedCheckboxes(By.cssSelector("input[id^='checkbox-']"),true);
            // Step 2: 下載EXCEL
            crawlerUtils.clickWhenClickable(By.xpath(BUTTON_DOWNLOAD_EXCEL));
            LOG.info(Instant.now() + " 下載EXCEL 完成");
            // Step 3: 判斷是否還有下一頁,沒有則直接結束
            By nextPageLocator = By.cssSelector("button[title='下一頁']");
            WebElement nextPageButton = getNextPageButton(nextPageLocator,pageCount);
            if (nextPageButton == null) break;

            goToNextPage(nextPageLocator);

            LOG.info("目前第 " + pageCount + " 頁");
            pageCount++;
        }
    }

    private WebElement getNextPageButton( By locator,int pageCount) {
        WebElement nextButton = crawlerUtils.getWebElement(ExpectedConditions.presenceOfElementLocated(locator));
        if (isButtonDisabled(nextButton)) {
            LOG.info("已到最後一頁，共處理 " + pageCount + " 頁");
            return null;
        }
        return nextButton;
    }

    /**
     * 使用 JS 點擊下一頁按鈕
     *
     * @By locator
     */
    private void goToNextPage(By locator) {
        crawlerUtils.clickWhenClickable(locator);
    }


    /**
     * 判斷按鈕是否 disabled
     *
     * @param button
     * @return
     */
    private boolean isButtonDisabled(WebElement button) {
        return button.getAttribute("outerHTML").contains("disabled");
    }


    private void clickCheckboxWithRetry(By checkboxSelector, int maxAttempts) {
        WebElement checkbox =  crawlerUtils.getWebElement(ExpectedConditions.visibilityOfElementLocated(checkboxSelector));
        int attempts = 0;
        long start = System.currentTimeMillis();
        LOG.info(start + " Started. ");
        while (attempts < maxAttempts) {
            if (!checkbox.isSelected()) {
                try {
                    // 確保在畫面中 & 嘗試點擊
                    crawlerUtils.clickWhenClickable(checkboxSelector);
//                    Thread.sleep(300); // 等待勾選動畫/反應

                    // 再次檢查是否已勾選成功,成功勾選就跳出
                    if (checkbox.isSelected()) {
                        long executionTime = System.currentTimeMillis() - start;
                        LOG.info(Instant.now() + " Checkbox selected successfully. Attempt: " + attempts + ", Executed in " + executionTime + "ms");
                        return;
                    }

                } catch (Exception e) {
                    LOG.info("Click attempt failed: " + e.getMessage());
                }
            }

            attempts++;
            LOG.info("Retrying checkbox selection, attempt " + attempts);
        }
        throw new RuntimeException("Failed to select checkbox after " + maxAttempts + " attempts.");
    }

    public void login() {
        // Load a new web page in the current browser window
        driver.get(loginUrl);

        //#創造一個顯性等待，等待時間15秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //取得[營業人扣繳單位]的元素位置
        crawlerUtils.clickWhenVisible(By.xpath(BUSINESS_WITHHOLDING_AGENT));
        // 輸入[統一編號]
        crawlerUtils.setFormValue(By.name("ban"), "16312227");
        // 輸入[帳號]
        crawlerUtils.setFormValue(By.id("user_id"), "16312227");
        // 輸入[密碼]
        crawlerUtils.setFormValue(By.id("user_password"), "Aci16312227");

        // 等待登入後的元件出現,代表已經成功登入才會繼續之後的動作
        crawlerUtils.getWebElement(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LOCATED_ELEMENT_AFTER_LOGIN)));
    }

//    /**
//     * 輸入登入者帳戶資料 (統一編號、帳號、密碼)
//     *
//     * @param wait
//     * @param locator
//     * @param number
//     */
//    private static void fillInFormData(WebDriverWait wait, By locator, String number) {
//        WebElement businessIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//        businessIdElement.sendKeys(number);
//    }
}
