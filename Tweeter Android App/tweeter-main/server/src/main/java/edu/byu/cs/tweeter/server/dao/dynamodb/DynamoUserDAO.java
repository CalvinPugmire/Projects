package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.base.UserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.User_;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoUserDAO extends DynamoDAO implements UserDAO {
    private static final String UserTableName = "user";
    private static final String UserAttr = "alias";

    public DynamoUserDAO (DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient);
    }

    @Override
    public User getUser(String alias) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        User_ result = table.getItem(key);

        if (result == null) {
            return null;
        } else {
            User user = new User(result.getFirstName(), result.getLastName(), result.getAlias(), result.getImageURL());
            System.out.println(user.getAlias());
            return user;
        }
    }

    @Override
    public User getUser(String alias, String password) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        User_ result = table.getItem(key);

        if (result == null) {
            return null;
        } else if (!password.equals(result.getPassword())) {
            throw new RuntimeException("[Bad Request] Invalid password");
        } else {
            User user = new User(result.getFirstName(), result.getLastName(), result.getAlias(), result.getImageURL());
            return user;
        }
    }

    @Override
    public User insertUser(String firstName, String lastName, String alias, String password, String imageURL) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));

        User_ newUser_ = new User_(alias, firstName, lastName, imageURL, password, 0, 0);
        table.putItem(newUser_);

        User user = new User(firstName, lastName, alias, imageURL);
        return user;
    }

    @Override
    public int getFollowersCount(String alias) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        User_ result = table.getItem(key);

        if (result == null) {
            return 0;
        } else {
            return result.getFollowers();
        }
    }

    @Override
    public int getFollowingCount(String alias) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        User_ result = table.getItem(key);

        if (result == null) {
            return 0;
        } else {
            return result.getFollowees();
        }
    }

    @Override
    public void updateUserCounts(String alias, int followersChange, int followeesChange) {
        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        User_ result = table.getItem(key);

        if (result == null) {
        } else {
            result.setFollowers(result.getFollowers()+followersChange);
            result.setFollowees(result.getFollowees()+followeesChange);
            table.updateItem(result);
        }
    }

    @Override
    public void addUserBatch(List<User> users, String password) {
        List<User_> batchToWrite = new ArrayList<>();
        for (User u : users) {
            User_ user_ = new User_(u.getAlias(), u.getFirstName(), u.getLastName(), u.getImageUrl(), password, 0, 1);
            batchToWrite.add(user_);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUser_s(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUser_s(batchToWrite);
        }
    }
    private void writeChunkOfUser_s(List<User_> user_s) {
        if(user_s.size() > 25)
            throw new RuntimeException("Too many users to write.");

        DynamoDbTable<User_> table = enhancedClient.table(UserTableName, TableSchema.fromBean(User_.class));
        WriteBatch.Builder<User_> writeBuilder = WriteBatch.builder(User_.class).mappedTableResource(table);
        for (User_ item : user_s) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUser_s(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
