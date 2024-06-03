package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.StatusObserver;

public class PostStatusHandler extends BaseHandler<StatusObserver> {
    public PostStatusHandler(StatusObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StatusObserver observer, Bundle data) {
        observer.handlePostStatus();
    }
}