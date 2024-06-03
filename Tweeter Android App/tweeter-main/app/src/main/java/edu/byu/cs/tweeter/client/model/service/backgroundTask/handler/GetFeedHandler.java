package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedHandler extends PageHandler<PageObserver<Status>,GetFeedTask,Status>  {
    public GetFeedHandler(PageObserver<Status> observer) {
        super(observer);
    }
}