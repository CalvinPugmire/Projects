package edu.byu.cs.tweeter.server.dao.dynamodb;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.server.dao.base.S3DAO;

public class DynamoS3DAO implements S3DAO {
    private static final String DEFAULT_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String BUCKET_NAME = "cpcs340bucket";

    public String uploadImage(String username, String imageString) {
        if (imageString.equals("DEFAULT")) {
            return DEFAULT_IMAGE_URL;
        }

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        try {
            // Set up keyName (USERNAME.png)
            String keyName = username;

            // Set up input stream
            byte[] array = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(array);

            // Set up metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.setContentLength(array.length);

            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, keyName, inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicRead);

            // Perform put
            s3.putObject(request);
            // System.out.println("Successfully put " + keyName + " in bucket");

            // Grab the url of the stored image
            return s3.getUrl(BUCKET_NAME, keyName).toString();

        } catch (Exception ex) {
            System.out.println("Threw exception..");
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
