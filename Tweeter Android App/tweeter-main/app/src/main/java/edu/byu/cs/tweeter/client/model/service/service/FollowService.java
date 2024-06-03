package edu.byu.cs.tweeter.client.model.service.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.model.service.observer.FolUnFolObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowersObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowingObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowingObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {
    public void loadMoreFollowees(User user, int pageSize, User lastFollowee, PageObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        execute(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollower, PageObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        execute(getFollowersTask);
    }

    public void isFollower (User selectedUser, IsFollowingObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        execute(isFollowerTask);
    }

    public void unfollow (User selectedUser, FolUnFolObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        execute(unfollowTask);
    }

    public void follow (User selectedUser, FolUnFolObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        execute(followTask);
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser, GotFollowersObserver observer1, GotFollowingObserver observer2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer1));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer2));
        executor.execute(followingCountTask);
    }
}
