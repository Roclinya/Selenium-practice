package org.example;

import org.example.iface.CrawlerOperation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CountSelectedDecorator extends CrawlerOperationDecorator{

    private static final Logger LOG = LoggerFactory.getLogger(CountSelectedDecorator.class);

    private final WebDriver driver;
    private final By locator;
    private final boolean excludeAllCheckbox;
    private int selectedCount;

    public CountSelectedDecorator(CrawlerOperation<WebElement> wrappedOperation, WebDriver driver, By locator, boolean excludeAllCheckbox) {
        super(wrappedOperation);
        this.driver = driver;
        this.locator = locator;
        this.excludeAllCheckbox = excludeAllCheckbox;
    }

    @Override
    public WebElement handleElement() {
        try {
            // Execute the wrapped operation (e.g., wait for visibility)
            WebElement result = super.handleElement();

            // Count selected checkboxes
            List<WebElement> elements = driver.findElements(locator);
            selectedCount = 0;
            for (WebElement element : elements) {
                if (element.isSelected()) {
                    selectedCount++;
                }
            }

            // Adjust count if excluding "checkbox-all"
            if (excludeAllCheckbox) {
                selectedCount = Math.max(0, selectedCount - 1);
            }

            LOG.info("⚠️ Number of selected elements: {}", selectedCount);
            return result; // Return the wrapped operation's result for consistency
        } catch (Exception e) {
            LOG.error("Error counting selected elements for locator {}: {}", locator, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Gets the count of selected elements.
     * @return the number of selected elements
     */
    public int getSelectedCount() {
        return selectedCount;
    }
}
