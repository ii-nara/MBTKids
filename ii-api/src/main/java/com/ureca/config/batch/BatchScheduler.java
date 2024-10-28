package com.ureca.config.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

  private final JobLauncher jobLauncher;
  private final JobRegistry jobRegistry;

//  @Bean
//  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
//    JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
//    jobProcessor.setJobRegistry(jobRegistry);
//    return jobProcessor;
//  }

  @Scheduled(cron = "0 0 0 * * * ") // 매일 오전 00:00 실행
  public void runJob() {
    String time = LocalDateTime.now().toString();
    try {
      Job job = jobRegistry.getJob("deleteMbtiJob"); // job 이름
      JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
      jobLauncher.run(job, jobParam.toJobParameters());
    } catch (NoSuchJobException e) {
      throw new RuntimeException(e);
    } catch (JobInstanceAlreadyCompleteException |
             JobExecutionAlreadyRunningException |
             JobParametersInvalidException |
             JobRestartException e
    ) {
      throw new RuntimeException(e);
    }
  }
}