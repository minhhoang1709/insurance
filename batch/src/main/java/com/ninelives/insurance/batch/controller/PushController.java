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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.batch.task.PushNotificationActiveOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationExpireOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationToBeExpireOrderConfiguration;

@Controller
public class PushController {
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier(PushNotificationActiveOrderConfiguration.PUSH_NOTIFICATION_ACTIVE_ORDER_JOB)
    Job pushNotificationActiveOrderJob;
    
    @Autowired
    @Qualifier(PushNotificationExpireOrderConfiguration.PUSH_NOTIFICATION_EXPIRE_ORDER_JOB)
    Job pushNotificationExpireOrderJob;
    
    @Autowired
    @Qualifier(PushNotificationToBeExpireOrderConfiguration.PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB)
    Job pushNotificationToBeExpireOrderJob;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    
    @RequestMapping("/push/orderActive.html")
    @ResponseBody
    public String handleJobPushActive( @DateTimeFormat(pattern="yyyyMMdd") Date targetDate) throws Exception{
    	JobParameters jobParam =
    			  new JobParametersBuilder().addString("targetDate", formatter.format(targetDate)).toJobParameters();

    	jobLauncher.run(pushNotificationActiveOrderJob, jobParam);
        return "ok";
    }
    
    @RequestMapping("/push/orderExpire.html")
    @ResponseBody
    public String handleJobPushExpire( @DateTimeFormat(pattern="yyyyMMdd") Date targetDate) throws Exception{
    	JobParameters jobParam =
    			  new JobParametersBuilder().addString("targetDate", formatter.format(targetDate)).toJobParameters();

    	jobLauncher.run(pushNotificationExpireOrderJob, jobParam);
        return "ok";
    }
    
    @RequestMapping("/push/orderToBeExpire.html")
    @ResponseBody
    public String handleJobPushToBeExpire( @DateTimeFormat(pattern="yyyyMMdd") Date targetDate) throws Exception{
    	JobParameters jobParam =
    			  new JobParametersBuilder().addString("targetDate", formatter.format(targetDate)).toJobParameters();

    	jobLauncher.run(pushNotificationToBeExpireOrderJob, jobParam);
        return "ok";
    }
    
}
