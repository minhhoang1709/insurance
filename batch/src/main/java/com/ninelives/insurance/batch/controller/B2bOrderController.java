package com.ninelives.insurance.batch.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.batch.task.B2bOrderConfirmConfiguration;

@Controller
public class B2bOrderController {
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier(B2bOrderConfirmConfiguration.B2B_ORDER_CONFIRM_JOB)
    Job b2bOrderConfirmJob;
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    
    @RequestMapping("/b2b/orderConfirm.html")
    @ResponseBody
	public String handleJobPushActive(@DateTimeFormat(pattern = "yyyyMMdd") Date targetDate,
			@RequestParam("runId") String runId) throws Exception {
    	JobParameters jobParam =
    			  new JobParametersBuilder().addString("targetDate", formatter.format(targetDate))
    			  .addString("runId", runId).toJobParameters();
    	    	
    	jobLauncher.run(b2bOrderConfirmJob, jobParam);
        return "ok";
    }
}
