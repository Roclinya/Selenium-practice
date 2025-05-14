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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

@Component
public class WebDriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceCrawler.class);

    private WebDriver driver;

    @Value("${browser}")
    private String browser;

    @Bean
    @Scope("prototype")
    public WebDriver webDriver() {
        return createDriver();
    }


    public WebDriver createDriver() {
        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                driver = getChromeDriver();
                break;
        }


        return driver;
    }

    protected static ChromeDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Larry\\OneDrive - 緯創軟體股份有限公司\\chromedriver-win64\\chromedriver.exe");
        ChromeDriverService service = new ChromeDriverService.Builder().withLogFile(getTempFile("logsToFile", ".log")).build();
        return new ChromeDriver(service,getDefaultChromeOptions());
    }
    /**
     * To change the logging output to save to a specific file
     * @param prefix
     * @param suffix
     * @return
     */
    protected static File getTempFile(String prefix, String suffix) {
        File logLocation = null;
        logLocation = new File(System.getProperty("user.dir")+"/"+prefix+ suffix);

        return logLocation;
    }

    /**
     * get ChromeOptions with customized settings
     * @return
     */
    protected static ChromeOptions getDefaultChromeOptions() {
        // Set the path where you want the files to be downloaded
        String downloadFilepath = "C:\\Users\\Larry\\Downloads"; // TODO　"C:\\Users\\Larry\\OneDrive - 緯創軟體股份有限公司";

        HashMap<Object, Object> chromePrefs = new HashMap<Object, Object>();

        //Set download path in the Chrome options
        chromePrefs.put("download.default_directory", downloadFilepath);
        chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);  // 允許Chrome多檔下載
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return options;
    }

    public void quitDriver(WebDriver webDriver) {
        if (webDriver != null) {
            try {
                webDriver.quit();
            } catch (Exception e) {
                LOG.error("Failed to quit WebDriver: " + e.getMessage());
            }
        }
    }
}
