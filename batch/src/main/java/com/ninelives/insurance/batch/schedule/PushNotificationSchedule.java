package com.ninelives.insurance.batch.schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.batch.NinelivesBatchConfigProperties;
import com.ninelives.insurance.batch.task.PushNotificationActiveOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationExpireOrderConfiguration;
import com.ninelives.insurance.batch.task.PushNotificationToBeExpireOrderConfiguration;

@Component
public class PushNotificationSchedule {

	private static final Logger log = LoggerFactory.getLogger(PushNotificationSchedule.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	@Autowired
	public NinelivesBatchConfigProperties config;
	
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
    
    @Scheduled(cron="0 0 8 * * *")
	public void pushNotificationActiveOrder() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		if(config.getEnablePushNotificationActiveOrderSchedule()){
	    	log.info("Starting schedule push notification for active order at "+ LocalDate.now());
			
			JobParameters jobParam =
	  			  new JobParametersBuilder().addString("targetDate", LocalDate.now().format(formatter)).toJobParameters();

			jobLauncher.run(pushNotificationActiveOrderJob, jobParam);			
		}else{
			log.info("Schedule disabled for push notification for active order at "+ LocalDate.now());
		}
    	
	}
    
    @Scheduled(cron="0 0 8 * * *")
	public void pushNotificationExpireOrder() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
    	if(config.getEnablePushNotificationExpireOrderSchedule()){
    		log.info("Starting schedule push notification for expire order at "+ LocalDate.now());
    		
    		JobParameters jobParam =
    	  			  new JobParametersBuilder().addString("targetDate", LocalDate.now().format(formatter)).toJobParameters();

    		jobLauncher.run(pushNotificationExpireOrderJob, jobParam);    		
    	}else{
    		log.info("Schedule disabled for push notification for expire order at "+ LocalDate.now());
    	}
	}
    
    @Scheduled(cron="0 0 20 * * *")
	public void pushNotificationToBeExpireOrder() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
    	if(config.getEnablePushNotificationToBeExpireOrderSchedule()){
    		log.info("Starting schedule push notification for to be expire order at "+ LocalDate.now());
    		
    		JobParameters jobParam =
    	  			  new JobParametersBuilder().addString("targetDate", LocalDate.now().format(formatter)).toJobParameters();

    		jobLauncher.run(pushNotificationToBeExpireOrderJob, jobParam);    		
    	}else{
    		log.info("Schedule disabled for push notification for to be expire order at "+ LocalDate.now());
    	}
	}
    
//	@Scheduled(cron="0 40 14 * * *")
//	public void pushNotificationToBeActiveOrder() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
//		log.info("Starting schedule push notification for to be active order at "+ LocalDate.now());
//		
//		String todayStr = LocalDate.now().format(formatter);
//		JobParameters jobParam =
//  			  new JobParametersBuilder().addString("targetDate", todayStr).toJobParameters();
//
//		jobLauncher.run(testJob1, jobParam);
//	}
}
