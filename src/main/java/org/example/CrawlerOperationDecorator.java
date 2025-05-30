package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.WebElement;

public abstract class CrawlerOperationDecorator implements CrawlerOperation<WebElement> {
    protected final CrawlerOperation<WebElement> wrappedOperation;

    public CrawlerOperationDecorator(CrawlerOperation<WebElement> wrappedOperation) {
        this.wrappedOperation = wrappedOperation;
    }

    @Override
    public WebElement handleElement() {
        return (WebElement) wrappedOperation.handleElement(); // 調用基礎操作
    }
}
