package com.manager.purchaseManager.jobs;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PurchaseTasklet purchaseTasklet;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public BatchConfiguration(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            PurchaseTasklet purchaseTasklet,
            JobLauncher jobLauncher,
            JobRegistry jobRegistry
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.purchaseTasklet = purchaseTasklet;
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @Bean
    public Step step(){
        return  stepBuilderFactory.get("step1")
                .tasklet(purchaseTasklet)
                .build();
    }

    @Bean
    public Job job(){
        Job job = jobBuilderFactory.get("dailyJob")
                .start(step())
                .build();
        try {
            jobRegistry.register(new ReferenceJobFactory(job));
        } catch (DuplicateJobException e) {
            e.printStackTrace();
        }
        return  job;
    }

    @Scheduled(fixedRate = 100000)
    public void performJob() throws Exception {
        JobExecution execution = jobLauncher.run(
                job(),
                new JobParametersBuilder()
                        .addString("JobId:",String.valueOf(System.currentTimeMillis()))
                        .toJobParameters()
        );
        System.out.println(execution.getStatus());
    }


}
