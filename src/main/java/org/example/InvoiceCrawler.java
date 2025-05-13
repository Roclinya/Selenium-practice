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
        nat.initDriverSettings();
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

    public void initDriverSettings() {
        //Start the session
        this.driver = DriverFactory.getDriver();
        // maximize the widow
        this.driver.manage().window().maximize();
//        // implicit wait for 10 sec per DOM element TODO: 看還需不需要使用?
//        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void clickWhenVisible(By locator, WebDriverWait wait) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.click();

    }

    private void clickWhenClickable(By locator, WebDriverWait wait) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

        } catch (TimeoutException e) {
            LOG.error("元素未出現：" + locator);
        } catch (Exception e) {
            LOG.error("點擊元素發生錯誤：" + locator + "，錯誤訊息：" + e.getMessage());
            throw e;
        }
    }

    private void selectWhenClickable(By locator, String text,WebDriverWait wait) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            Select invoiceSelect = new Select(element);
            invoiceSelect.selectByVisibleText(text);

        } catch (TimeoutException e) {
            LOG.error("元素未出現：" + locator);
        } catch (Exception e) {
            LOG.error("點擊元素發生錯誤：" + locator + "，錯誤訊息：" + e.getMessage());
            throw e;
        }
    }

    public void execute() throws InterruptedException {
        // 取得今天的日期，格式為 yyyy-MM-dd，例如 "2025-05-07"
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ISO_DATE);

        try {
            LOG.info("Execute ,keep going");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            //取得[營業人功能選單]的元素位置
            clickWhenVisible(By.xpath(FUNCTIONB2B_MENU), wait);
            //取得[查詢與下載]的元素位置
            clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN), wait);
            //取得[發票查詢/列印/下載]的元素位置
            clickWhenVisible(By.xpath(FUNCTIONB2B_MENU_QRY_DOWN_PRINT), wait);

            selectCalendarFilterCondition(wait);
// TODO: 強制等待sleep 改成 implicit wait
//            Thread.sleep(1000); // 等待日曆出現
            selectCalendar(wait);
            LOG.info("成功選取今天的日期: " + todayStr);


            // Radio選取 進項
            clickWhenClickable(By.id("queryInvType_1"),wait);
//            WebElement queryInvType1Element = wait.until(ExpectedConditions.elementToBeClickable(By.id("queryInvType_1")));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", queryInvType1Element);

            // 發票狀態
            selectWhenClickable(By.id("input09"),"開立已確認",wait);
//            WebElement invoiceElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("input09")));
//            Select invoiceSelect = new Select(invoiceElement);
//            invoiceSelect.selectByVisibleText("開立已確認");


        } catch (Exception e) {
            LOG.info("等待元件時出錯：" + todayStr);
            LOG.error(e.getMessage(), e);
        }
    }

    private static void selectCalendarFilterCondition(WebDriverWait wait) {
        WebElement selectElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("input01")));
        Select select = new Select(selectElement);
        //TODO: test
//            select.selectByVisibleText("依年月");
//            LOG.info();("select 依年月 完成 ");
        select.selectByVisibleText("依日期");
        LOG.info("select 日期 完成 ");
    }

    private static void selectCalendar(WebDriverWait wait) {
        // 1. 點擊 input 打開日期選擇器
        WebElement dateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dp-input-date02")));
        dateInput.click();

        // 找到特定日期 2025-05-07(當下日曆開啟的月份)
        WebElement todayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='2025-05-07']//div[contains(@class, 'dp__cell_inner')]")));

        //第一次click點選想要的日期,第二次click是確認點選該日期
        todayElement.click();
        todayElement.click();
    }

    private boolean executeQuery() {
        LOG.info("Query ,keep going");
        boolean isQuerySuccess = false;
        boolean isQueryFailed = false;
        try {
            // 執行查詢
//        WebElement searchElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn btn-primary"))); TODO: 原本使用class名稱抓取
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            clickWhenClickable(By.xpath(BUTTON_SEARCH), wait);

            try {
                // 使用較短時間等待  「提示視窗」(toast_box) 出現
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".toast_box .toast-body span")));

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
            LOG.info("等待元件時出錯：executeQuery");
            LOG.error(e.getMessage(), e);
        }
        return isQuerySuccess;
    }

    private void executeExcelDownload() throws InterruptedException {
        LOG.info("ExcelDownload ,keep going");
        int pageCount = 1;
        // 設定最大頁數(查詢筆數上限為200筆 & 防止無窮迴圈)
        int maxPageCount = 100;
        // 每次下載限制為200筆
        int maxSelectPerDownload = 200;

        while (pageCount <= maxPageCount) {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            long waitSec = 500;
//              Thread.sleep(waitSec); // 等待
//            LOG.info();("⚠️ 等待中..."+waitSec+" sec");
            // Step 1: 勾選所有檔案
            clickCheckboxWithRetry(By.id("checkbox-all"), wait, driver, 100);
            // Step 1.1: 檢查檔案勾選數量 => 檢查所有 id 為 checkbox-* 的 <input>
            getSelectedCount();
            // Step 2: 下載EXCEL
            clickWhenClickable(By.xpath(BUTTON_DOWNLOAD_EXCEL), wait);
            LOG.info(Instant.now() + " 下載EXCEL 完成");
            // Step 3: 判斷是否還有下一頁,沒有則直接結束
            WebElement nextPageButton = getNextPageButton(wait, pageCount);
            if (nextPageButton == null) break;

            goToNextPage(nextPageButton);

            LOG.info("目前第 " + pageCount + " 頁");
            pageCount++;
        }
    }

    private WebElement getNextPageButton(WebDriverWait wait, int pageCount) {
        WebElement nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[title='下一頁']")));
        if (isButtonDisabled(nextButton)) {
            LOG.info("已到最後一頁，共處理 " + pageCount + " 頁");
            return null;
        }
        return nextButton;
    }

    private void getSelectedCount() {
        int selectedCount = 0;
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[id^='checkbox-']"));
        for (WebElement checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                selectedCount++;
            }
        }
        LOG.info("⚠️ 勾選數量: " + (selectedCount - 1));// 扣除checkbox-all的那一筆
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

    /**
     * 使用 JS 點擊下一頁按鈕
     *
     * @param nextButton
     */
    private void goToNextPage(WebElement nextButton) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);
    }


    private void clickCheckboxWithRetry(By checkboxSelector, WebDriverWait wait, WebDriver driver, int maxAttempts) {
        WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(checkboxSelector));
        int attempts = 0;
        long start = System.currentTimeMillis();
        LOG.info(start + " Started. ");
        while (attempts < maxAttempts) {
            if (!checkbox.isSelected()) {
                try {
                    // 確保在畫面中 & 嘗試點擊
//                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkbox);
                    clickWhenClickable(checkboxSelector, wait);
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
        //Take action on browser
        driver.get(loginUrl);

        //#創造一個顯性等待，等待時間15秒
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //取得[營業人扣繳單位]的元素位置
        clickWhenVisible(By.xpath(BUSINESS_WITHHOLDING_AGENT), wait);
        // 輸入[統一編號]
        fillInFormData(wait, By.name("ban"), "16312227");
        // 輸入[帳號]
        fillInFormData(wait, By.id("user_id"), "16312227");
        // 輸入[密碼]
        fillInFormData(wait, By.id("user_password"), "Aci16312227");
        // End the session
        LOG.info("wait for loginMark");

        WebElement loginMark = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(LOCATED_ELEMENT_AFTER_LOGIN)
        ));

        LOG.info("Login ,keep going");

    }

    /**
     * 輸入登入者帳戶資料 (統一編號、帳號、密碼)
     *
     * @param wait
     * @param ban
     * @param number
     */
    private static void fillInFormData(WebDriverWait wait, By ban, String number) {
        WebElement businessIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(ban));
        businessIdElement.sendKeys(number);
    }
}
