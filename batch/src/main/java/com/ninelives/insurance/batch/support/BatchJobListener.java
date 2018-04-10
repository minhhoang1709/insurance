package com.ninelives.insurance.batch.support;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.ninelives.insurance.batch.service.BatchService;
import com.ninelives.insurance.model.BatchLog;

public class BatchJobListener implements JobExecutionListener{
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobListener.class);

	BatchService batchService;
	
	public BatchJobListener (BatchService batchService){
		this.batchService = batchService;
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.debug("JobExecution is <{}>", jobExecution);
		
		BatchLog batchLog = new BatchLog();
		batchLog.setJobExecutionId(jobExecution.getId());
		batchLog.setJobName(jobExecution.getJobInstance().getJobName());
		if(jobExecution.getJobParameters()!=null){
			batchLog.setJobParameter(jobExecution.getJobParameters().toString());
		}		
		batchLog.setStartDate(LocalDate.now());
		batchLog.setVersion(jobExecution.getVersion());
		batchLog.setJobInstanceId(jobExecution.getJobInstance().getId());
		batchLog.setStartTime(LocalDateTime.now());
		batchLog.setStatus(jobExecution.getStatus().name());
		
		batchService.registerBatchLog(batchLog);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		BatchLog batchLog = new BatchLog();
		batchLog.setJobExecutionId(jobExecution.getId());
		batchLog.setEndTime(LocalDateTime.now());
		batchLog.setLastUpdated(LocalDateTime.now());
		batchLog.setStatus(jobExecution.getStatus().name());
		batchLog.setExitCode(jobExecution.getExitStatus().getExitCode());
		batchLog.setExitMessage(jobExecution.getExitStatus().getExitDescription());
		batchService.updateBatchLog(batchLog);
	}

}
