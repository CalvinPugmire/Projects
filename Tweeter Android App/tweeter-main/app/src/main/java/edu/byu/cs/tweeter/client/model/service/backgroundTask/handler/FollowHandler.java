package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.FolUnFolObserver;

public class FollowHandler extends FolUnFolHandler {
    public FollowHandler(FolUnFolObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FolUnFolObserver observer, Bundle data) {
        observer.handleFolUnFol(false);
    }

    @Override
    protected void handleAfterMessage(FolUnFolObserver observer) {
        observer.handleFinishFolUnFol();
    }
}