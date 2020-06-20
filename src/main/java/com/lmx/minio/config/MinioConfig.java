package com.lmx.minio.config;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入minioClient工具类
 */
@Configuration
public class MinioConfig {
    final Logger logger = LoggerFactory.getLogger(MinioConfig.class);
    @Value("${minio.endpoint}")
    private  String ENDPOINT;
    @Value("${minio.accessKey}")
    private  String ACCESSKEY;
    @Value("${minio.secretKey}")
    private  String SECRETKEY;

    @Bean
    public MinioClient minioClient() {
        try {
            logger.info("endpoint: "+ENDPOINT+ " accessKey: " +ACCESSKEY+ " secretKey: "+SECRETKEY);
            return new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

}
