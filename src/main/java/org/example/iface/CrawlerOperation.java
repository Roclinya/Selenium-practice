package org.example.iface;

import org.openqa.selenium.WebElement;

public interface CrawlerOperation<T> {
    T handleElement();
}
