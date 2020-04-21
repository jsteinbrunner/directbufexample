package com.zillow.directbufexample;

import java.util.UUID;
import java.util.concurrent.CompletionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

@RestController
@Slf4j
public class Controller {
    @Value("${aws.s3.bucket")
    private String bucket;

    @Autowired
    private S3AsyncClient s3AsyncClient;

    @Autowired
    private S3Client s3Client;

    @GetMapping("/headAsync")
    public void asyncHeadRequest() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(UUID.randomUUID().toString())
                .build();

        try {
            s3AsyncClient.headObject(headObjectRequest).join();
        } catch (Exception e) {
            if (e.getMessage().contains("Direct buffer memory")) {
                throw e;
            }
        }
    }

    @GetMapping("/headSync")
    public void syncHeadRequest() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(UUID.randomUUID().toString())
                .build();

        try {
            s3Client.headObject(headObjectRequest);
        } catch (Exception e) {
            if (e.getMessage().contains("Direct buffer memory")) {
                throw e;
            }
        }
    }
}
