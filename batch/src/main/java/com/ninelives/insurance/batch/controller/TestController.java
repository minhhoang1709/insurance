package com.ninelives.insurance.batch.controller;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.batch.task.PushNotificationActiveOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationExpireOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationToBeExpireOrderConfiguration;

@Controller
@Profile("localdb")
public class TestController {
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("testjob1")
    Job job;
    
    @RequestMapping("/test/jobLauncher.html")
    @ResponseBody
    public String handle(@RequestParam("date") String date) throws Exception{
    	JobParameters jobParam =
    			  new JobParametersBuilder().addString("date", date).toJobParameters();

    	jobLauncher.run(job, jobParam);
        return "ok";
    }
    
	@PostConstruct
	private void check(){
		   System.out.println("parameter incrementer is "+job.getJobParametersIncrementer());
	}
}
