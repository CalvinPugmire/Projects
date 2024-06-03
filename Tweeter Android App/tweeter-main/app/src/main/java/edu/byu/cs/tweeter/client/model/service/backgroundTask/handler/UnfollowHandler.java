package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.FolUnFolObserver;

public class UnfollowHandler extends FolUnFolHandler {
    public UnfollowHandler(FolUnFolObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FolUnFolObserver observer, Bundle data) {
        observer.handleFolUnFol(true);
    }

    @Override
    protected void handleAfterMessage(FolUnFolObserver observer) {
        observer.handleFinishFolUnFol();
    }
}