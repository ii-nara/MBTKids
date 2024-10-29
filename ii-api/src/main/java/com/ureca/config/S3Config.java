package com.ureca.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// S3 연결 설정파일
@Configuration
public class S3Config {

  private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

  @Value("${s3.accesskey}")
  private String accessKey;

  @Value("${s3.secretkey}")
  private String secretKey;

  @Value("${s3.bucketname}")
  private String bucketName;

  @Value("${s3.region}")
  private String region;

  @Bean
  public AmazonS3 s3Client() {
    AmazonS3 s3Client = null;
    try {
      BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      s3Client =
          AmazonS3ClientBuilder.standard()
              .withRegion(region) // 리전 설정
              .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // 자격 증명 설정
              .build(); // 클라이언트 빌드
    } catch (Exception e) {
      logger.error("S3Client Config Error!!", e);
    }
    return s3Client;
  } // s3Client
}
