package com.ninelives.insurance.batch.task;

import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.ninelives.insurance.batch.NinelivesBatchConfigProperties;
import com.ninelives.insurance.batch.model.B2bOrderConfirmData;
import com.ninelives.insurance.batch.service.BatchService;
import com.ninelives.insurance.batch.support.B2bOrderConfirmDataFromStringMapper;
import com.ninelives.insurance.batch.support.B2bOrderConfirmWriter;
import com.ninelives.insurance.batch.support.BatchJobListener;
import com.ninelives.insurance.batch.support.BlankLineRecordSeparatorPolicy;
import com.ninelives.insurance.core.service.OrderService;

@Configuration
public class B2bOrderConfirmConfiguration {

	public static final String B2B_ORDER_CONFIRM_JOB = "b2bOrderConfirmJob";
	
	public static final String B2B_ORDER_CONFIRM_STEP1_READER = "b2bOrderConfirmStep1Reader";
	public static final String B2B_ORDER_CONFIRM_STEP1_WRITER = "b2bOrderConfirmStep1Writer";
	
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
	
	@Bean(B2B_ORDER_CONFIRM_JOB)
	public Job b2bOrderConfirmJob(
			@Qualifier(B2B_ORDER_CONFIRM_STEP1_READER) ItemStreamReader<B2bOrderConfirmData> step1Reader,
			@Qualifier(B2B_ORDER_CONFIRM_STEP1_WRITER) ItemWriter<B2bOrderConfirmData> step1Writer
			){
		return jobBuilderFactory.get(B2B_ORDER_CONFIRM_JOB).start(step1(step1Reader, step1Writer))
				.listener(new BatchJobListener(batchService)).build();
	}
	
	public Step step1(ItemStreamReader<B2bOrderConfirmData> reader, ItemWriter<B2bOrderConfirmData> writer) {
		return stepBuilderFactory.get(B2B_ORDER_CONFIRM_JOB + "-step1")
				.<B2bOrderConfirmData, B2bOrderConfirmData>chunk(1000).reader(reader).writer(writer).build();		
	}
	
	@Bean(B2B_ORDER_CONFIRM_STEP1_READER)
	@StepScope
	public FlatFileItemReader<B2bOrderConfirmData> step1Reader(@Value("#{jobParameters['targetDate']}") String targetDate) {
		String filepath = FilenameUtils.concat(config.getBaseDir(),
				"b2bOrderConfirm."  + targetDate + ".txt");
		
		FlatFileItemReader<B2bOrderConfirmData> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filepath));		
		DefaultLineMapper<B2bOrderConfirmData> lineMapper = new DefaultLineMapper<B2bOrderConfirmData>();
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
		lineMapper.setFieldSetMapper(new B2bOrderConfirmDataFromStringMapper());
		reader.setLineMapper(lineMapper);
		reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
		
		return reader;
	}
	
	@Bean(B2B_ORDER_CONFIRM_STEP1_WRITER)
	public B2bOrderConfirmWriter step1Writer(@Autowired OrderService orderService) {
		B2bOrderConfirmWriter writer = new B2bOrderConfirmWriter(orderService);
		return writer;
	}
}
