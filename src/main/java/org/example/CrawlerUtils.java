package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public class CrawlerUtils {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCrawler.class);


    private WebDriver driver;
    private WebDriverWait wait;
    private Duration timeout;

    public CrawlerUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        this.timeout = timeout;
    }

    public WebDriverWait getWebDriverWait() {
        return wait;
    }

    public WebElement getWebElement(ExpectedCondition<WebElement> condition) {
        try {
          return wait.until(condition);
        } catch (TimeoutException e) {
            LOG.error("Element not found within timeout");
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Error waiting for element: {}", e.getMessage());
            throw e; // TODO: 或可包成自定義例外再丟
        }
    }

    public CrawlerUtils clickWhenVisible(By locator) {
        WaitUntilOperation waitUntilOperation = new WaitUntilOperation(driver, wait, ExpectedConditions.visibilityOfElementLocated(locator), timeout);
        ClickDecorator operation = new ClickDecorator(waitUntilOperation);
        operation.handleElement();
        return this;
    }

    public CrawlerUtils clickWhenClickable(By locator) {
        WaitUntilOperation waitUntilOperation = new WaitUntilOperation(driver, wait, ExpectedConditions.elementToBeClickable(locator), timeout);
        ClickByScriptDecorator operation = new ClickByScriptDecorator(waitUntilOperation, driver);
        operation.handleElement();
        return this;
    }

    /**
     * Double click target element at once
     *
     * @param locator
     */
    public CrawlerUtils doubleClickWhenClickable(By locator) {
        WaitUntilOperation waitUntilOperation = new WaitUntilOperation(driver, wait, ExpectedConditions.elementToBeClickable(locator), timeout);
        ClickByScriptDecorator operation = new ClickByScriptDecorator(waitUntilOperation, driver);
        operation.doubleClick();
        return this;
    }

    /**
     * Selects an option from a dropdown by visible text when the element is clickable.
     * @param locator the locator for the select element
     * @param text the visible text of the option to select
     * @return this CrawlerUtils instance for chaining
     */
    public CrawlerUtils selectElement(By locator, String text) {
        WaitUntilOperation waitUntilOperation = new WaitUntilOperation(driver, wait, ExpectedConditions.elementToBeClickable(locator), timeout);
        SelectDecorator selectOperation = new SelectDecorator(waitUntilOperation);
        Select select = selectOperation.getSelectElement();
        select.selectByVisibleText(text);
        return this;
    }

    /**
     * 輸入登入者帳戶資料 (統一編號、帳號、密碼)
     *
     * @param locator
     * @param text
     */
    public CrawlerUtils setFormValue(By locator, String text) {
        // 初始化資料丟入opration,接著執行
        WaitUntilOperation waitUntilOperation = new WaitUntilOperation(driver, wait, ExpectedConditions.elementToBeClickable(locator), timeout);
        setFormValueDecorator operation = new setFormValueDecorator(waitUntilOperation, text);
        operation.handleElement();
        return this;
    }

    /**
     * Counts selected checkboxes matching the locator and logs the count.
     * @param locator the locator for the checkboxes (e.g., By.cssSelector("input[id^='checkbox-']"))
     * @param excludeAllCheckbox whether to exclude an "all" checkbox from the count
     * @return this CrawlerUtils instance for chaining
     */
    public CrawlerUtils countSelectedCheckboxes(By locator, boolean excludeAllCheckbox) {
        WaitUntilOperation waitOperation = new WaitUntilOperation(
                driver, wait, ExpectedConditions.presenceOfElementLocated(locator), timeout
        );
        CountSelectedDecorator operation = new CountSelectedDecorator(waitOperation, driver, locator, excludeAllCheckbox);
        operation.handleElement();
        return this;
    }

    /**
     * Gets the count of selected checkboxes.
     * @param locator the locator for the checkboxes
     * @param excludeAllCheckbox whether to exclude an "all" checkbox from the count
     * @return the number of selected checkboxes
     */
    public int getSelectedCheckboxCount(By locator, boolean excludeAllCheckbox) {
        CrawlerOperation<WebElement> waitOperation = new WaitUntilOperation(
                driver, wait, ExpectedConditions.presenceOfElementLocated(locator), timeout
        );
        CountSelectedDecorator operation = new CountSelectedDecorator(waitOperation, driver, locator, excludeAllCheckbox);
        operation.handleElement();
        return operation.getSelectedCount();
    }


}
