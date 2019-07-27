package com.itbility.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.itbility.batch.listener.JobCompletionNotificationListener;
import com.itbility.batch.model.Data;
import com.itbility.batch.model.FileDataStore;
import com.itbility.batch.processor.FileDataEncryptionProcessor;
import com.itbility.batch.reader.FileDataReader;
import com.itbility.batch.writer.FileDataWriter;

/**
 * @author vikas
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	
	@Value("${max-threads}")
	private int maxThreads;
	
	@Value("${chunk-size}")
	private int chunkSize;


	@Value("${inputFilePath}")
	private String inputFilePath;
	
	@Value("${outputFilePath}")
	private String outputFilePath;
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FileDataStore fileDataStore() {
		return new FileDataStore();
	}


	//Reader to read input file
	@Bean
	public FileDataReader fileDataReader() {
		FileDataReader fileReader = new FileDataReader();
		fileReader.setResource(new FileSystemResource(inputFilePath));
		return fileReader;
	}

	// Processor to encrypt the data before writing
	@Bean
	public FileDataEncryptionProcessor fileDataEncryptionProcessor() {
		return new FileDataEncryptionProcessor();
	}

	// Writer which writes the data in DataStore
	@Bean
	public FileDataWriter fileDataWriter() {
		return new FileDataWriter();
	}

	// JobCompletionNotificationListener (File loader)
	@Bean
	public JobExecutionListener listener() {
		JobCompletionNotificationListener jobCompletionNotificationListener = new JobCompletionNotificationListener();
		jobCompletionNotificationListener.setOutputFilePath(outputFilePath);
		return jobCompletionNotificationListener;
	}

	
	 // Configure job step
	 @Bean public Job fileReadJob() {
		 return jobBuilderFactory.get("DataEncryptionJob")
				 .incrementer(new RunIdIncrementer())
				 .listener(listener())
				 .start(step())
				 .build(); 
		 }
	 
	
	@Bean
	public TaskExecutor taskExecutor(){
	    SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("spring_batch");
	    asyncTaskExecutor.setConcurrencyLimit(maxThreads);
	    return asyncTaskExecutor;
	}
    
	
	 @Bean public Step step() { 
		 return stepBuilderFactory.get("step").<Data, Data> chunk(10000)
				 .reader(fileDataReader())
				 .processor(fileDataEncryptionProcessor())
				 .writer(fileDataWriter())
				 .taskExecutor(taskExecutor())
				 .throttleLimit(maxThreads)
				 .build(); 
		 }
	 

}
