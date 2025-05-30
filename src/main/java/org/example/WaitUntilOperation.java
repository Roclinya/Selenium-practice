package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WaitUntilOperation implements CrawlerOperation<WebElement> {
    private static final Logger LOG = LoggerFactory.getLogger(WaitUntilOperation.class);
    private final WebDriverWait wait;
    private final ExpectedCondition<WebElement> condition;

    public WaitUntilOperation(WebDriver driver, WebDriverWait wait, ExpectedCondition<WebElement> condition, Duration timeout) {
        this.wait = new WebDriverWait(driver, timeout);
        this.condition = condition;
    }

    protected WebElement waitUntil() {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            LOG.error("Element not found within timeout");
            throw e;
        } catch (RuntimeException e) {
            LOG.error("Error waiting for element: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public WebElement handleElement() {
        return waitUntil(); // 執行等待，子類或 Decorator 可以進一步處理
    }
}
