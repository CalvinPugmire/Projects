package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetFollowersTask extends PagedUserTask {
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower, Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws Exception {
        ServerFacade facade = new ServerFacade();

        String lastAlias = null;
        User last = getLastItem();
        if (last != null) {
            lastAlias = last.getAlias();
        }
        FollowersRequest request = new FollowersRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastAlias);

        FollowersResponse response = facade.getFollowers(request, "/getfollowers");
        List<User> followers = response.getFollowers();
        Boolean hasMorePages = response.getHasMorePages();
        return new Pair<>(followers, hasMorePages);
    }
}
