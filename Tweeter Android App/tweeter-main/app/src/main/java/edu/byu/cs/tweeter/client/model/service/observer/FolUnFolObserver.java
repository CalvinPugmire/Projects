package edu.byu.cs.tweeter.client.model.service.observer;

public interface FolUnFolObserver extends ServiceObserver {
    void handleFolUnFol(boolean setfollow);
    void handleFinishFolUnFol();
}