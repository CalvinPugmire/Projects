package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryHandler extends PageHandler<PageObserver<Status>, GetStoryTask,Status> {
    public GetStoryHandler(PageObserver<Status> observer) {
        super(observer);
    }
}