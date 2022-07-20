package com.meta.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Log4j2
@Component
public class S3Util {

    @Value("${QINIU_AK:}")
    private String QINIU_AK;
    @Value("${QINIU_SK:}")
    private String QINIU_SK;
    @Value("${QINIU_BUCKET:}")
    private String QINIU_BUCKET;
    @Value("${QINIU_DOMAIN:}")
    private String QINIU_DOMAIN;

    public AmazonS3 getAmazonS3(){
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(QINIU_DOMAIN, Regions.US_WEST_2.name());
        AWSCredentials credentials = new BasicAWSCredentials(QINIU_AK, QINIU_SK);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        ClientConfiguration clientConfig = new ClientConfiguration();
        Protocol https = Protocol.HTTPS;
//        System.setProperty(https.name(), "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        clientConfig.setProtocol(https);
        //设置https协议访问
        AmazonS3 amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(credentialsProvider)
                .disableChunkedEncoding()
                .build();
        return amazonS3;
    }

    public void upload(MultipartFile file) throws IOException {
//        AmazonS3 amazonS3 = this.getAmazonS3();

        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration("delay.s3-cn-east-1.qiniucs.com", "cn-east-1");
        AWSCredentials credentials = new BasicAWSCredentials(QINIU_AK, QINIU_SK);
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfig)
                .build();



//        AmazonS3 amazonS3 = new AmazonS3Client(new BasicAWSCredentials(QINIU_AK,QINIU_SK));
//        amazonS3.setEndpoint("s3-cn-east-1.qiniucs.com");
//        S3ClientOptions options = new S3ClientOptions();
//        options.withChunkedEncodingDisabled(true);
//        amazonS3.setS3ClientOptions(options);

        String originalFilename = file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        amazonS3.putObject(QINIU_BUCKET, originalFilename, new ByteArrayInputStream(bytes), null);
        amazonS3.shutdown();

    }

}
