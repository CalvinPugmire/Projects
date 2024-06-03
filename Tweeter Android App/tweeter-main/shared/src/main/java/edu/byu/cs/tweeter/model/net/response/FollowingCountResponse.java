package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response {
    private int followingCount;

    public FollowingCountResponse() {
        super(true);
    }

    public FollowingCountResponse(int followingCount) {
        super(true);
        this.followingCount = followingCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
