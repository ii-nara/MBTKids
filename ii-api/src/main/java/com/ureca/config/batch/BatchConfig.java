package com.ureca.config.batch;

import com.ureca.repository.MbtiStatusRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig extends DefaultBatchConfiguration {

  private final MbtiStatusRepository mbtiStatusRepository;

  @Bean
  public Job deleteMbtiJob(
      JobRepository jobRepository, PlatformTransactionManager transactionManager)
      throws DuplicateJobException {
    Job job =
        new JobBuilder("deleteMbtiJob", jobRepository)
            .start(deleteMbtiStep(jobRepository, transactionManager))
            .build();
    return job;
  }

  public Step deleteMbtiStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    Step step =
        new StepBuilder("deleteMbtiStep", jobRepository)
            .tasklet(deleteMbtiTasklet(), transactionManager)
            .build();
    return step;
  }

  public Tasklet deleteMbtiTasklet() {
    return ((contribution, chunkContext) -> {
      System.out.println("***** hello batch! *****");
      LocalDateTime now = LocalDateTime.now();
      mbtiStatusRepository.deleteByDeleteAtIsBefore(now);
      return RepeatStatus.FINISHED;
    });
  }
}
