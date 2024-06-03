package org.example;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class FollowDAO {
    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";
    private static final String FollowerAttr = "follower_alias";
    private static final String FolloweeAttr = "followee_alias";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    public void insertFollow(String follower_alias, String follower_name, String followee_alias, String followee_name) {
        DynamoDbTable<Follow> table = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class));
        Key key = Key.builder()
                .partitionValue(follower_alias).sortValue(followee_alias)
                .build();

        Follow newFollow = new Follow(follower_alias, follower_name, followee_alias, followee_name);
        table.putItem(newFollow);
    }

    public void deleteFollow(String follower_alias, String followee_alias) {
        DynamoDbTable<Follow> table = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class));
        Key key = Key.builder()
                .partitionValue(follower_alias).sortValue(followee_alias)
                .build();

        table.deleteItem(key);
    }

    public Follow getFollow(String follower_alias, String followee_alias) {
        DynamoDbTable<Follow> table = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class));
        Key key = Key.builder()
                .partitionValue(follower_alias).sortValue(followee_alias)
                .build();

        Follow result = table.getItem(key);

        if (result == null) {
            return new Follow("Not found.", "Not found.", "Not found.", "Not found.");
        } else {
            return result;
        }
    }

    public void updateFollow(String follower_alias, String followee_alias, String follower_name, String followee_name) {
        DynamoDbTable<Follow> table = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class));
        Key key = Key.builder()
                .partitionValue(follower_alias).sortValue(followee_alias)
                .build();

        Follow result = table.getItem(key);

        result.setFollower_name(follower_name);
        result.setFollowee_name(followee_name);

        table.updateItem(result);
    }

    //public DataPage<Visit> getVisitedLocations(String visitor, int pageSize, String lastLocation) {
    public DataPage<Follow> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbTable<Follow> table = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Follow> result = new DataPage<Follow>();

        PageIterable<Follow> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Follow> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        return result;
    }

    //public DataPage<Visit>  getVisitors(String location, int pageSize, String lastVisitor) {
    public DataPage<Follow> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias ) {
        DynamoDbIndex<Follow> index = enhancedClient.table(TableName, TableSchema.fromBean(Follow.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize);

        if(isNonEmptyString(lastUserAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Follow> result = new DataPage<Follow>();

        SdkIterable<Page<Follow>> sdkIterable = index.query(request);
        PageIterable<Follow> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<Follow> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        return result;
    }
}