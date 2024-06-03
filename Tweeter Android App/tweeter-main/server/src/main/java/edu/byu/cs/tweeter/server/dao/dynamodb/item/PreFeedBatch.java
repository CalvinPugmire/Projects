package edu.byu.cs.tweeter.server.dao.dynamodb.item;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class PreFeedBatch {
    private Status status;
    private List<String> followerAliases;

    private PreFeedBatch () {}

    public PreFeedBatch (Status status, List<String> followerAliases) {
        this.status = status;
        this.followerAliases = followerAliases;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFollowerAliases() {
        return followerAliases;
    }

    public void setFollowerAliases(List<String> followerAliases) {
        this.followerAliases = followerAliases;
    }
}
