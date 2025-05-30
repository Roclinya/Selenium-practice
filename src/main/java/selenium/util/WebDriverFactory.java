package selenium.util;

import org.example.InvoiceCrawler;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

@Component
public class WebDriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCrawler.class);

    private final ObjectFactory<WebDriver> webDriverFactory;

    @Autowired
    public WebDriverFactory(ObjectFactory<WebDriver> webDriverFactory) {
        this.webDriverFactory = webDriverFactory;
    }

    public WebDriver createDriver() {
        LOG.info("Created new WebDriver instance");
        return webDriverFactory.getObject(); // 獲取新的 prototype Bean
    }

    public void quitDriver(WebDriver webDriver) {
        if (webDriver != null) {
            try {
                // 確保關閉 WebDriver
                LOG.info("Quitting WebDriver instance");
                webDriver.quit();
            } catch (Exception e) {
                LOG.error("Failed to quit WebDriver: " + e.getMessage());
            }
        }
    }
}
