package selenium.Service;

import lombok.NoArgsConstructor;
import org.example.InvoiceCrawler;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import selenium.util.WebDriverFactory;

@NoArgsConstructor
@Component
public class SeleniumTestService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCrawler.class);

    public WebDriverFactory driverFactory;

    @Autowired
    public SeleniumTestService(WebDriverFactory driverFactory) {
        this.driverFactory = driverFactory;
    }

    public void performTest() {
        Runnable task = () -> {
            LOG.info("Task is Running , Main Thread: " + Thread.currentThread().getName());
            WebDriver webDriver = driverFactory.createDriver(); // 每次獲取新 WebDriver
            try {
                webDriver.get("https://data.taipei/dataset/detail?id=bcf11ef9-9855-4ee3-8750-ca3df5aaf6c4");
                System.out.println("Page title: " + webDriver.getTitle());
            } finally {
                // 執行完畢關閉瀏覽器
                driverFactory.quitDriver(webDriver);
            }
        };
        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();

    }
}
