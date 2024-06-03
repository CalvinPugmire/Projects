package edu.byu.cs.tweeter.server.dao.dynamodb;

import edu.byu.cs.tweeter.server.dao.base.DAOFactory;
import edu.byu.cs.tweeter.server.dao.base.FollowDAO;
import edu.byu.cs.tweeter.server.dao.base.S3DAO;
import edu.byu.cs.tweeter.server.dao.base.StatusDAO;
import edu.byu.cs.tweeter.server.dao.base.UserDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOFactory implements DAOFactory {
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2).build();

    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient).build();

    @Override
    public FollowDAO getFollowDAO() {
        return new DynamoFollowDAO(enhancedClient);
    }

    @Override
    public StatusDAO getStatusDAO() {
        return new DynamoStatusDAO(enhancedClient);
    }

    @Override
    public UserDAO getUserDAO() {
        return new DynamoUserDAO(enhancedClient);
    }

    @Override
    public S3DAO getS3DAO() {
        return new DynamoS3DAO();
    }
}
