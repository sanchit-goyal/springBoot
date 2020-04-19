package com.example.springbatch.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbatch.jobs.LocalJobBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class JobController {

	private JobLauncher jobLauncher;

	private LocalJobBuilder localJobBuilder;

	@GetMapping("/1234")
	public boolean executeJob() throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		List<String> StepName = Arrays.asList("step1", "step2");

		jobLauncher.run(localJobBuilder.job(StepName), null);

		return true;
	}

}
