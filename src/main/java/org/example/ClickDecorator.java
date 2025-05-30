package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickDecorator extends CrawlerOperationDecorator{

    private static final Logger LOG = LoggerFactory.getLogger(ClickDecorator.class);

    public ClickDecorator(CrawlerOperation<WebElement> wrappedOperation) {
        super(wrappedOperation);
    }

    @Override
    public WebElement handleElement() {
        try {
//            WebElement element = ((WaitUntilOperation) wrappedOperation).waitUntil();
            WebElement element = super.handleElement();
            element.click();
            return element;
        } catch (Exception e) {
            LOG.error("點擊元素時發生錯誤，錯誤訊息：" + e.getMessage(), e);
            throw e;
        }
    }
}
