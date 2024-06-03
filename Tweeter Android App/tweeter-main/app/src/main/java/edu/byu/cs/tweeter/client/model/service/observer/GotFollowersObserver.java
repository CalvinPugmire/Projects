package edu.byu.cs.tweeter.client.model.service.observer;

public interface GotFollowersObserver extends ServiceObserver {
    void handleGotFollowers(int count);
}