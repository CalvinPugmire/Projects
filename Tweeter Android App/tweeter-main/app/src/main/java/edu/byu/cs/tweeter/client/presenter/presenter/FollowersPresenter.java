package edu.byu.cs.tweeter.client.presenter.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagePresenter<FollowersPresenter.View, User> {
    public interface View extends PageView<User> {}

    private final FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void loadAction (User user, int pageSize, User lastFollower) {
        followService.loadMoreFollowers(user, pageSize, lastFollower, new FollowersObserver());
    }

    public class FollowersObserver extends PagePresenter<FollowersPresenter.View, User>.PgObserver {
        @Override
        protected void getLastItem (List<User> follows) {
            lastItem = (follows.size() > 0) ? follows.get(follows.size() - 1) : null;
        }

        @Override
        public String getFailMessage() {
            return "Failed to get followers: ";
        }

        @Override
        protected String getExMessage () {
            return "Failed to get followers because of exception: ";
        }
    }
}
