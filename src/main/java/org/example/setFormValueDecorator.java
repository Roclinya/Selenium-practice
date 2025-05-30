package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class setFormValueDecorator extends CrawlerOperationDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(setFormValueDecorator.class);

    private final String text;

    public setFormValueDecorator(CrawlerOperation<WebElement> wrappedOperation,String text) {
        super(wrappedOperation);
        this.text = text;
    }

    @Override
    public WebElement handleElement() {
        try {
            WebElement element = super.handleElement();
            element.sendKeys(text);
            return element;
        } catch (Exception e) {
            LOG.error("點擊元素時發生錯誤，錯誤訊息：" + e.getMessage(), e);
            throw e;
        }
    }
}
