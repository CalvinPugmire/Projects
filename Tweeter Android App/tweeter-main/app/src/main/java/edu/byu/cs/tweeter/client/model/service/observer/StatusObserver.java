package edu.byu.cs.tweeter.client.model.service.observer;

public interface StatusObserver extends ServiceObserver {
    void handlePostStatus();
}