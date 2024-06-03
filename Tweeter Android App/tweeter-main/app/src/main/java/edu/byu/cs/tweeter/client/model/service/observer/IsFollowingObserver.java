package edu.byu.cs.tweeter.client.model.service.observer;

public interface IsFollowingObserver extends ServiceObserver {
    void handleIsFollowing(boolean following);
}
