package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickByScriptDecorator extends CrawlerOperationDecorator {
    private static final Logger LOG = LoggerFactory.getLogger(ClickByScriptDecorator.class);
    private final WebDriver driver;

    public ClickByScriptDecorator(CrawlerOperation<WebElement> wrappedOperation, WebDriver driver) {
        super(wrappedOperation);
        this.driver = driver;
    }

    @Override
    public WebElement handleElement() {
        WebElement element;
        try {
            element = super.handleElement();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return element;
        } catch (RuntimeException e) {
            LOG.error("點擊元素時發生錯誤，錯誤訊息：" + e.getMessage(), e);
            throw e;
        }
    }

    public WebElement doubleClick() {
        WebElement element;
        try {
            element  = ((WaitUntilOperation) wrappedOperation).waitUntil();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return element;
        } catch (RuntimeException e) {
            LOG.error("點擊元素時發生錯誤，錯誤訊息：" + e.getMessage(), e);
            throw e;
        }
    }
}
