package edu.byu.cs.tweeter.client.model.service.observer;

public interface GotFollowingObserver extends ServiceObserver {
    void handleGotFollowing(int count);
}