package com.example.springbatch.jobs;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.springbatch.entity.UserEntiry;
import com.example.springbatch.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
@Component
@EnableBatchProcessing
public class LocalJobBuilder { // we van inject this in some service and pass the list of step names

	private JobBuilderFactory jobBuilderFactory;

	private ApplicationContext applicationContext;

	private StepBuilderFactory stepBuilderFactory;

	public Job job(List<String> steps) {

		if (CollectionUtils.isEmpty(steps)) {
			throw new IllegalStateException("Steps should not be empty");
		}

		SimpleJobBuilder builder = jobBuilderFactory.get("abc")
				.incrementer(new RunIdIncrementer())
				.start(getStep(steps.get(0)));
		// steps.remove(0);
		for (String stepName : steps) {
			builder.next(getStep(stepName));
		}

		return builder.build();
	}

	private Step getStep(String stepName) {
		try {
			return applicationContext.getBean(stepName, Step.class);
		} catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e) {
			throw new IllegalStateException("this should never occur, please check if you have created such a step");
		}
	}

	@Bean("step1")
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<UserEntiry, UserEntiry>chunk(1)
				.reader(itemreader())
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}

	@Bean("step2")
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.<UserEntiry, UserEntiry>chunk(1)
				.reader(itemreader())
				.processor(itemProcessor())
				.writer(itemWriter())
				.build();
	}

	private UserRepository userRepository;

	private ItemWriter<UserEntiry> itemWriter() {
		return new ItemWriter<UserEntiry>() {

			@Override
			public void write(List<? extends UserEntiry> items) throws Exception {
				userRepository.saveAll(items);

			}
		};
	}

	private ItemProcessor<UserEntiry, UserEntiry> itemProcessor() {
		return new ItemProcessor<UserEntiry, UserEntiry>() {

			@Override
			public UserEntiry process(UserEntiry item) throws Exception {
				return item;
			}
		};
	}

	private ItemReader<UserEntiry> itemreader() {

		return new ItemReader<UserEntiry>() {

			Iterator<UserEntiry> data = userRepository.findAll().iterator();

			@Override
			public UserEntiry read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				if (data.hasNext()) {
					return data.next();
				}
				return null;
			}
		};
	}
}
