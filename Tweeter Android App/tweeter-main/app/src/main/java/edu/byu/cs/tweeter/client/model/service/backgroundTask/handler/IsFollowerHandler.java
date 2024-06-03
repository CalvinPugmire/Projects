package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowingObserver;

public class IsFollowerHandler extends BaseHandler<IsFollowingObserver> {
    public IsFollowerHandler(IsFollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(IsFollowingObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleIsFollowing(isFollower);
    }
}