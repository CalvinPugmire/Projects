package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;

public class LogoutHandler extends BaseHandler<LogoutObserver> {
    public LogoutHandler(LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LogoutObserver observer, Bundle data) {
        observer.handleLogout();
    }
}