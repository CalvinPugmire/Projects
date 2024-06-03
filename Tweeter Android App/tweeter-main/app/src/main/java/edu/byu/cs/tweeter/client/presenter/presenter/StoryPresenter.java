package edu.byu.cs.tweeter.client.presenter.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagePresenter<StoryPresenter.View, Status> {
    public interface View extends PageView<Status> {}
    private final StatusService statusService;

    public StoryPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void loadAction (User user, int pageSize, Status lastStatus) {
        statusService.loadMoreStory(user, pageSize, lastStatus, new StoryObserver());
    }
    public class StoryObserver extends PagePresenter<StoryPresenter.View, Status>.PgObserver {
        @Override
        protected void getLastItem (List<Status> statuses) {
            lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
        }

        @Override
        public String getFailMessage() {
            return "Failed to get story: ";
        }

        @Override
        protected String getExMessage () {
            return "Failed to get story because of exception: ";
        }
    }
}
