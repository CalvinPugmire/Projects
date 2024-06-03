package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersHandler extends PageHandler<PageObserver<User>, GetFollowersTask, User> {
    public GetFollowersHandler(PageObserver<User> observer) {
        super(observer);
    }
}