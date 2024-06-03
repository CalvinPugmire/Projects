package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.base.FollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.Follows;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoFollowDAO extends DynamoDAO implements FollowDAO {
    private static final String FollowsTableName = "follows";
    public static final String FollowsIndexName = "follows_index";
    private static final String FollowerAttr = "follower_alias";
    private static final String FolloweeAttr = "followee_alias";

    public DynamoFollowDAO (DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient);
    }

    @Override
    public List<String> getFollowers(String alias) {
        DynamoDbIndex<Follows> index = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class)).index(FollowsIndexName);
        Key key = Key.builder().partitionValue(alias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder().queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        List<String> result = new ArrayList<>();

        SdkIterable<Page<Follows>> sdkIterable = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    page.items().forEach(follows -> result.add(follows.getFollower_alias()));
                });

        return result;
    }

    @Override
    public List<String> getPageOfFollowers(String followeeAlias, int pageSize, String lastFollowerAlias) {
        DynamoDbIndex<Follows> index = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class)).index(FollowsIndexName);
        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<String> result = new ArrayList<>();

        SdkIterable<Page<Follows>> sdkIterable = index.query(request);
        PageIterable<Follows> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    page.items().forEach(follows -> result.add(follows.getFollower_alias()));
                });

        return result;
    }

    @Override
    public List<String> getPageOfFollowees(String followerAlias, int pageSize, String lastFolloweeAlias) {
        DynamoDbTable<Follows> table = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastFolloweeAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<String> result = new ArrayList<>();

        PageIterable<Follows> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Follows> page) -> {
                    page.items().forEach(follows -> result.add(follows.getFollowee_alias()));
                });

        return result;
    }

    @Override
    public boolean getFollow(User follower, User followee) {
        DynamoDbTable<Follows> table = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(follower.getAlias()).sortValue(followee.getAlias())
                .build();

        Follows result = table.getItem(key);

        return result != null;
    }

    @Override
    public boolean insertFollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<Follows> table = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class));

        Follows newFollows = new Follows(followerAlias, followeeAlias);
        table.putItem(newFollows);

        return true;
    }

    @Override
    public boolean deleteFollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<Follows> table = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class));
        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();

        table.deleteItem(key);

        return true;
    }

    @Override
    public void addFollowersBatch(List<String> followers, String followee) {
        List<Follows> batchToWrite = new ArrayList<>();
        for (String f : followers) {
            Follows follows = new Follows(f, followee);
            batchToWrite.add(follows);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFollows(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFollows(batchToWrite);
        }
    }
    private void writeChunkOfFollows(List<Follows> follows) {
        if(follows.size() > 25)
            throw new RuntimeException("Too many follows to write.");

        DynamoDbTable<Follows> table = enhancedClient.table(FollowsTableName, TableSchema.fromBean(Follows.class));
        WriteBatch.Builder<Follows> writeBuilder = WriteBatch.builder(Follows.class).mappedTableResource(table);
        for (Follows item : follows) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFollows(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
