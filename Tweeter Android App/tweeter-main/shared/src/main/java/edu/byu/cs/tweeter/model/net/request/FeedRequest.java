package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FeedRequest {
    private String userAlias;
    private int limit;
    private String lastDatetime;
    private AuthToken authToken;

    private FeedRequest() {}

    public FeedRequest(AuthToken authToken, String userAlias, int limit, String lastDatetime) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastDatetime = lastDatetime;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastDatetime() {
        return lastDatetime;
    }

    public void setLastDatetime(String lastDatetime) {
        this.lastDatetime = lastDatetime;
    }
}
