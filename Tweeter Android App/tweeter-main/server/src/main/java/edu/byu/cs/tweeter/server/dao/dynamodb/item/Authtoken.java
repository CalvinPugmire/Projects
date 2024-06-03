package edu.byu.cs.tweeter.server.dao.dynamodb.item;

import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoFollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Authtoken {
    private String token = "token";
    private long datetime = 0;
    private long expiration = 0;
    private String user_alias = "user_alias";

    public Authtoken() {

    }

    public Authtoken(String token, long datetime, long expiration, String user_alias) {
        this.token = token;
        this.datetime = datetime;
        this.expiration = expiration;
        this.user_alias = user_alias;
    }

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getUser_alias() {
        return user_alias;
    }

    public void setUser_alias(String user_alias) {
        this.user_alias = user_alias;
    }
}
