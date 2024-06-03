package edu.byu.cs.tweeter.server.dao.dynamodb.item;

import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoFollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follows {
    private String follower_alias = "follower_alias";
    private String followee_alias = "followee_alias";

    public Follows() {

    }

    public Follows(String follower_alias, String followee_alias) {
        this.follower_alias = follower_alias;
        this.followee_alias = followee_alias;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoFollowDAO.FollowsIndexName)
    public String getFollower_alias() {
        return follower_alias;
    }

    public void setFollower_alias(String follower_alias) {
        this.follower_alias = follower_alias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoFollowDAO.FollowsIndexName)
    public String getFollowee_alias() {
        return followee_alias;
    }

    public void setFollowee_alias(String followee_alias) {
        this.followee_alias = followee_alias;
    }
}
