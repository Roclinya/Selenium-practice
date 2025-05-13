package org.example;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;

public class DriverFactory {

    protected static WebDriver driver;

    // 私有建構子，避免被 new
    private DriverFactory(){
    }

    // 多執行緒時，當物件需要被建立時才使用synchronized保證Singleton一定是單一的 ，增加程式校能
    public static WebDriver getDriver(){
        if(driver == null){
            synchronized(WebDriver.class){
                if(driver == null){
                    driver = getChromeDriver();
                }
            }
        }
        return driver;
    }

    protected static ChromeDriver getChromeDriver() {
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
}
