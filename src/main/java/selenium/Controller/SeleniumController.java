package selenium.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selenium.Service.SeleniumTestService;

@RestController
@RequestMapping("/api/Selenium")
public class SeleniumController {

    private final SeleniumTestService seleniumTestService;

    @Autowired
    public SeleniumController(SeleniumTestService seleniumTestService) {
        this.seleniumTestService = seleniumTestService;
    }
    @PostMapping("")
    public void getSeleniumServiceTest(){
        seleniumTestService.performTest();
    }


}
