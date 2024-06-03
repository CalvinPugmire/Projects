package edu.byu.cs.tweeter.server.dao.dynamodb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.base.StatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.Feed;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.PreFeedBatch;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.Story;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
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

public class DynamoStatusDAO extends DynamoDAO implements StatusDAO {
    private static final String StoryTableName = "story";
    public static final String StoryIndexName = "story_index";

    private static final String FeedTableName = "feed";
    public static final String FeedIndexName = "feed_index";

    private static final String UserAliasAttr = "user_alias";
    private static final String DateTimeAttr = "datetime";

    public DynamoStatusDAO (DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient);
    }

    @Override
    public List<Status> getPageOfFeed(String alias, int pageSize, String lastDate) {
        String lastDatetime = getFormattedTableTime(lastDate);

        DynamoDbTable<Feed> table = enhancedClient.table(FeedTableName, TableSchema.fromBean(Feed.class));
        Key key = Key.builder().partitionValue(alias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(pageSize);

        if(isNonEmptyString(lastDatetime)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(alias).build());
            startKey.put(DateTimeAttr, AttributeValue.builder().s(lastDatetime).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<Status> result = new ArrayList<>();

        PageIterable<Feed> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Feed> page) -> {
                    page.items().forEach(feed -> result.add(new Status(feed.getPost(),
                            new User("", "", feed.getAuthor_alias(), ""),
                            getFormattedDateTime(feed.getDatetime()), feed.getUrls(), feed.getMentions())));
                });

        return result;
    }

    @Override
    public List<Status> getPageOfStory(String alias, int pageSize, String lastDate) {
        String lastDatetime = getFormattedTableTime(lastDate);

        DynamoDbTable<Story> table = enhancedClient.table(StoryTableName, TableSchema.fromBean(Story.class));
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(pageSize);

        if(isNonEmptyString(lastDatetime)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(UserAliasAttr, AttributeValue.builder().s(alias).build());
            startKey.put(DateTimeAttr, AttributeValue.builder().s(lastDatetime).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<Status> result = new ArrayList<>();

        PageIterable<Story> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<Story> page) -> {
                    page.items().forEach(story -> result.add(new Status(story.getPost(),
                            new User("", "", story.getUser_alias(), ""),
                            getFormattedDateTime(story.getDatetime()), story.getUrls(), story.getMentions())));
                });

        return result;
    }

    @Override
    public boolean insertStory(Status status) {
        DynamoDbTable<Story> table = enhancedClient.table(StoryTableName, TableSchema.fromBean(Story.class));

        Story newStory = new Story(status.getUser().getAlias(), getFormattedTableTime(status.getDatetime()), status.getPost(), status.getUrls(), status.getMentions());

        table.putItem(newStory);

        return true;
    }

    @Override
    public void insertFeed(PreFeedBatch preFeedBatch) {
        List<String> followerAliases = preFeedBatch.getFollowerAliases();
        Status status = preFeedBatch.getStatus();

        addFeedBatch(followerAliases, status);
    }

    private void addFeedBatch(List<String> followerAliases, Status status) {
        List<Feed> batchToWrite = new ArrayList<>();
        for (String fa : followerAliases) {
            Feed feed = new Feed(fa, getFormattedTableTime(status.getDatetime()), status.getUser().getAlias(), status.getPost(), status.getUrls(), status.getMentions());
            batchToWrite.add(feed);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFeeds(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFeeds(batchToWrite);
        }
    }
    private void writeChunkOfFeeds(List<Feed> feeds) {
        if(feeds.size() > 25)
            throw new RuntimeException("Too many feeds to write.");

        DynamoDbTable<Feed> table = enhancedClient.table(FeedTableName, TableSchema.fromBean(Feed.class));
        WriteBatch.Builder<Feed> writeBuilder = WriteBatch.builder(Feed.class).mappedTableResource(table);
        for (Feed item : feeds) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFeeds(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private String getFormattedDateTime(String tableTime) {
        try {
            Date tempDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tableTime);
            String formattedDate = new SimpleDateFormat("MMM d yyyy h:mm:ss aaa").format(tempDate);
            return formattedDate;
        } catch (Exception ex) {
        }
        return null;
    }

    private String getFormattedTableTime(String dateTime) {
        try {
            Date tempDate = new SimpleDateFormat("MMM d yyyy h:mm:ss aaa").parse(dateTime);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(tempDate);
            return formattedDate;
        } catch (Exception ex) {
        }
        return null;
    }
}
