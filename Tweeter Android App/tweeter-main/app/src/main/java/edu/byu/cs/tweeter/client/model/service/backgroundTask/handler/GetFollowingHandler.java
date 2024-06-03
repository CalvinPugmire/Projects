package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingHandler extends PageHandler<PageObserver<User>, GetFollowingTask, User> {
    public GetFollowingHandler(PageObserver<User> observer) {
        super(observer);
    }
}