package com.ninelives.insurance.batch.task;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.ninelives.insurance.batch.NinelivesBatchConfigProperties;
import com.ninelives.insurance.batch.model.PushNotificationData;
import com.ninelives.insurance.batch.ref.PushNotificationType;
import com.ninelives.insurance.batch.service.BatchService;
import com.ninelives.insurance.batch.support.PushNotificationDataFromStringMapper;
import com.ninelives.insurance.batch.support.PushNotificationDataToStringProcessor;
import com.ninelives.insurance.batch.support.PushNotificationFcmWriter;
import com.ninelives.insurance.batch.support.PushNotificationJobListener;
import com.ninelives.insurance.batch.support.PushNotificationMyBatisReader;
import com.ninelives.insurance.core.service.NotificationService;

@Configuration
public class PushNotificationToBeExpireOrderConfiguration {
	private static final PushNotificationType PUSH_NOTIFICATION_TYPE = PushNotificationType.TOBE_EXPIRE_ORDER;
	
	public static final String PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB = "pushNotificationToBeExpireOrderJob";
	
	public static final String PUSH_NOTIFICATION_STEP1_READER = "pushNotificationToBeExpireOrderStep1Reader";
	public static final String PUSH_NOTIFICATION_STEP1_WRITER = "pushNotificationToBeExpireOrderStep1Writer";
	
	public static final String PUSH_NOTIFICATION_STEP2_READER = "pushNotificationToBeExpireOrderStep2Reader";
	public static final String PUSH_NOTIFICATION_STEP2_WRITER = "pushNotificationToBeExpireOrderStep2Writer";
			
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public NinelivesBatchConfigProperties config;
	
	@Autowired 
	public BatchService batchService;
	
	//SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	@Bean(PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB)
	public Job pushNotificationActiveOrderJob(
			@Qualifier(PUSH_NOTIFICATION_STEP1_READER) ItemStreamReader<PushNotificationData> step1Reader,
			@Qualifier(PUSH_NOTIFICATION_STEP1_WRITER) ItemWriter<String> step1Writer,
			@Qualifier(PUSH_NOTIFICATION_STEP2_READER) ItemReader<PushNotificationData> step2Reader,
			@Qualifier(PUSH_NOTIFICATION_STEP2_WRITER) ItemWriter<PushNotificationData> step2Writer
			) {
		return jobBuilderFactory.get(PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB).start(step1(step1Reader, step1Writer))
				.next(step2(step2Reader, step2Writer)).listener(new PushNotificationJobListener(batchService)).build();
	}
	
	public Step step1(ItemStreamReader<PushNotificationData> reader, ItemWriter<String> writer) {
		return stepBuilderFactory.get(PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB + "-step1")
				.<PushNotificationData, String>chunk(1000).reader(reader)
				.processor(new PushNotificationDataToStringProcessor(PUSH_NOTIFICATION_TYPE)).writer(writer).build();
	}
	
	public Step step2(ItemReader<PushNotificationData> reader, ItemWriter<PushNotificationData> writer){
		return stepBuilderFactory.get(PUSH_NOTIFICATION_TO_BE_EXPIRE_ORDER_JOB + "-step2")
				.<PushNotificationData, PushNotificationData>chunk(1).reader(reader).writer(writer).build();
	}
		
	@Bean(PUSH_NOTIFICATION_STEP1_READER)
	@StepScope
	public PushNotificationMyBatisReader<PushNotificationData> step1Reader() {	
        final PushNotificationMyBatisReader<PushNotificationData> reader = new PushNotificationMyBatisReader<>();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setQueryId("com.ninelives.insurance.batch.mybatis.mapper.BatchMapper.selectToBeExpireOrder");
        return reader;
    }

	@Bean(PUSH_NOTIFICATION_STEP1_WRITER)
	@StepScope
    public FlatFileItemWriter<String> writer(@Value("#{jobParameters['targetDate']}") String targetDate) {
		String filepath = FilenameUtils.concat(config.getBaseDir(),
				"push." + PUSH_NOTIFICATION_TYPE + "." + targetDate + ".txt");
		
		FlatFileItemWriter<String> writer = new FlatFileItemWriter<String>();
		writer.setResource(new FileSystemResource(filepath));
		writer.setLineAggregator(new PassThroughLineAggregator<String>());
		
        return writer;
    }
	
	@Bean(PUSH_NOTIFICATION_STEP2_READER)
	@StepScope
	public FlatFileItemReader<PushNotificationData> step2Reader(@Value("#{jobParameters['targetDate']}") String targetDate) {
		String filepath = FilenameUtils.concat(config.getBaseDir(),
				"push." + PUSH_NOTIFICATION_TYPE + "." + targetDate + ".txt");
		
		FlatFileItemReader<PushNotificationData> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filepath));		
		DefaultLineMapper<PushNotificationData> lineMapper = new DefaultLineMapper<PushNotificationData>();
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
		lineMapper.setFieldSetMapper(new PushNotificationDataFromStringMapper());
		reader.setLineMapper(lineMapper);
		
		return reader;
	}
	
	@Bean(PUSH_NOTIFICATION_STEP2_WRITER)
	@StepScope
	public PushNotificationFcmWriter step2Writer(@Autowired NotificationService notificationService, 
			@Autowired MessageSource messageSource) {
		PushNotificationFcmWriter writer = new PushNotificationFcmWriter(notificationService, messageSource);
		return writer;
	}
}
