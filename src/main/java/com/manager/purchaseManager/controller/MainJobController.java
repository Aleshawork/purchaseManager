package com.manager.purchaseManager.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/job")
public class MainJobController {

    private static final String KEY = "JobId";
    private  final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobRepository jobRepository;


    public MainJobController(JobLauncher jobLauncher, JobRegistry jobRegistry, JobRepository jobRepository) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
        this.jobRepository = jobRepository;
    }

    @GetMapping(value = "/{name}")
    public String executeJob(@PathVariable("name") String name) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Job job = null;
        try {
            job = jobRegistry.getJob("dailyJob");
        } catch (NoSuchJobException e) {
            e.printStackTrace();
            return "";
        }
        return (String) jobLauncher.run(
                job,
                new JobParametersBuilder()
                        .addString(KEY,String.valueOf(System.currentTimeMillis()))
                        .toJobParameters()

                ).getExecutionContext().get(KEY);

    }

    @GetMapping(value="/result/{id}")
    public BatchStatus getBatchStatusById(@PathVariable("id") String id){
        BatchStatus status = jobRepository
                .getLastJobExecution("dailyJob", new JobParametersBuilder().addString(KEY, id).toJobParameters())
                .getStatus();
        if (Objects.isNull(status)){
            return BatchStatus.UNKNOWN;
        } else{
            return status;
        }


    }
}
