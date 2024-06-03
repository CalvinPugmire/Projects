package edu.byu.cs.tweeter.client.presenter.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagePresenter<FollowingPresenter.View, User> {
    public interface View extends PageView<User> {}

    private final FollowService followService;

    public FollowingPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void loadAction (User user, int pageSize, User lastFollowee) {
        followService.loadMoreFollowees(user, pageSize, lastFollowee, new FollowingObserver());
    }

    public class FollowingObserver extends PagePresenter<FollowingPresenter.View, User>.PgObserver {
        @Override
        protected void getLastItem (List<User> follows) {
            lastItem = (follows.size() > 0) ? follows.get(follows.size() - 1) : null;
        }

        @Override
        public String getFailMessage() {
            return "Failed to get following: ";
        }

        @Override
        protected String getExMessage () {
            return "Failed to get following because of exception: ";
        }
    }
}
