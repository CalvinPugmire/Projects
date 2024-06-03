package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowersObserver;

public class GetFollowersCountHandler extends BaseHandler<GotFollowersObserver> {
    public GetFollowersCountHandler(GotFollowersObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GotFollowersObserver observer, Bundle data) {
        int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
        observer.handleGotFollowers(count);
    }
}