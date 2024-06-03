package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowingObserver;

public class GetFollowingCountHandler extends BaseHandler<GotFollowingObserver> {
    public GetFollowingCountHandler(GotFollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GotFollowingObserver observer, Bundle data) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleGotFollowing(count);
    }
}