package org.example;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;

public class S3Copy {
    public static void main(String[] args) {
        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        // Write code to do the following:
        // 1. get name of file to be copied from the command line
        String file_path = args[0];
        String key_name = args[1];
        // 2. get name of S3 bucket from the command line
        String bucket_name = args[2];
        // 3. upload file to the specified S3 bucket using the file name as the S3 key
        s3.putObject(bucket_name, key_name, new File(file_path));
    }
}