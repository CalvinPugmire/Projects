package edu.byu.cs.tweeter.server.dao.dynamodb.item;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoStatusDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Feed {
    private String user_alias = "user_alias";
    private String author_alias = "author_alias";
    private String datetime = "datetime";
    private String post = "post";
    private List<String> urls = new ArrayList<>();
    private List<String> mentions = new ArrayList<>();

    public Feed() {

    }

    public Feed(String user_alias, String datetime, String author_alias, String post, List<String> urls, List<String> mentions) {
        this.user_alias = user_alias;
        this.datetime = datetime;
        this.author_alias = author_alias;
        this.post = post;
        this.urls = urls;
        this.mentions = mentions;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoStatusDAO.FeedIndexName)
    public String getUser_alias() {
        return user_alias;
    }

    public void setUser_alias(String user_alias) {
        this.user_alias = user_alias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoStatusDAO.FeedIndexName)
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAuthor_alias() {
        return author_alias;
    }

    public void setAuthor_alias(String author_alias) {
        this.author_alias = author_alias;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
