package com.manager.purchaseManager.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PurchaseTasklet purchaseTasklet;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, PurchaseTasklet purchaseTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.purchaseTasklet = purchaseTasklet;
    }

    @Bean
    public Step step(){
        return  stepBuilderFactory.get("step1")
                .tasklet(purchaseTasklet)
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("Job1")
                .start(step())
                .build();
    }
}
