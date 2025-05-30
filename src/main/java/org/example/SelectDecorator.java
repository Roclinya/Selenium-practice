package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectDecorator extends CrawlerOperationDecorator{

    private static final Logger LOG = LoggerFactory.getLogger(SelectDecorator.class);

    public SelectDecorator(CrawlerOperation<WebElement> wrappedOperation) {
        super(wrappedOperation);
    }

    @Override
    public WebElement handleElement() {
        try {
            return super.handleElement();
        } catch (Exception e) {
            LOG.error("點擊元素時發生錯誤，錯誤訊息：" + e.getMessage(), e);
            throw e;
        }
    }

    public Select getSelectElement() {
        try {
            WebElement element = super.handleElement();
            // Add validation to ensure the element supports Select (e.g., check the tag name).
            if (!element.getTagName().equalsIgnoreCase("select")) {
                throw new IllegalStateException("Element is not a select element: " + element);
            }
            return new Select(element);
        } catch (Exception e) {
            LOG.error("Error getting select element: {}", e.getMessage(), e);
            throw e;
        }
    }
}
