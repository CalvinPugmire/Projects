package org.example;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follow {
    private String follower_alias = "follower_alias";
    private String follower_name = "follower_name";
    private String followee_alias = "followee_alias";
    private String followee_name = "followee_name";

    public Follow () {

    }

    public Follow (String follower_alias, String follower_name, String followee_alias, String followee_name) {
        this.follower_alias = follower_alias;
        this.follower_name = follower_name;
        this.followee_alias = followee_alias;
        this.followee_name = followee_name;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDAO.IndexName)
    public String getFollower_alias() {
        return follower_alias;
    }

    public void setFollower_alias(String follower_alias) {
        this.follower_alias = follower_alias;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDAO.IndexName)
    public String getFollowee_alias() {
        return followee_alias;
    }

    public void setFollowee_alias(String followee_alias) {
        this.followee_alias = followee_alias;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }
}
