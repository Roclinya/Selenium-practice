package selenium.controller;

import selenium.jobs.HelloWorldJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import selenium.quartzService.SchedulerService;

@RestController
@RequestMapping("/api/quartz")
public class QuartzController {

    private final SchedulerService schedulerService;

    @Autowired
    public QuartzController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    @PostMapping("/runhelloworld")
    public void runHelloWorldJob() {
        schedulerService.schedule(HelloWorldJob.class);
    }

}
