package edu.byu.cs.tweeter.model.net.response;

public class FollowersCountResponse extends Response {
    private int followersCount;

    public FollowersCountResponse() {
        super(true);
    }

    public FollowersCountResponse(int followersCount) {
        super(true);
        this.followersCount = followersCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }
}
