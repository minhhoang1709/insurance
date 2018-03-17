package com.ninelives.insurance.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;

@Configuration
@Profile("dev")
public class TestConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<String> reader() {
		FlatFileItemReader<String> reader = new FlatFileItemReader<String>();
		reader.setResource(new FileSystemResource("D:\\local\\sts\\9lives-batch\\test\\test.txt"));
		reader.setLineMapper(new PassThroughLineMapper());
		
		System.out.println("Checked that the reader save state is "+reader.isSaveState());
		
		return reader;
	}

	@Bean
	public MyItemProcessor processor() {
		return new MyItemProcessor();
	}

	@Bean(name="testjob1")
	public Job importUserJob() {
		return jobBuilderFactory.get("importUserJob").flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<String, String>chunk(1).reader(reader()).processor(processor()).writer(writer())
				.listener(new MyStepListener()).build();
	}
	
	@Bean
    public FlatFileItemWriter<String> writer() {
		
		FlatFileItemWriter<String> writer = new FlatFileItemWriter<String>();
		writer.setResource(new FileSystemResource("D:\\local\\sts\\9lives-batch\\test\\test-processed.txt"));
		writer.setLineAggregator(new PassThroughLineAggregator<String>());
        return writer;
    }
	
	
	private static class MyStepListener implements StepExecutionListener{

		@Override
		public void beforeStep(StepExecution stepExecution) {
			// TODO Auto-generated method stub
			System.out.println("commit count is " +stepExecution.getCommitCount() +" and parameter is " +stepExecution.getJobParameters());
		}

		@Override
		public ExitStatus afterStep(StepExecution stepExecution) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	private static class MyItemProcessor implements ItemProcessor<String, String> {
		@Override
		public String process(String item) throws Exception {
			System.out.println("process " + item);
			Thread.sleep(1000);
			return item + " processed";
		}
		
	}
	
	

	
	// private static class MyCustomStepListener implements
	// StepExecutionListener {
	//
	// @Override
	// public void beforeStep(StepExecution stepExecution) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public ExitStatus afterStep(StepExecution stepExecution) {
	// executionContext.putLong(getKey(stepExecution.getLINES_READ_COUNT),
	// reader.getPosition());
	// return null;
	// }
	// }
}
