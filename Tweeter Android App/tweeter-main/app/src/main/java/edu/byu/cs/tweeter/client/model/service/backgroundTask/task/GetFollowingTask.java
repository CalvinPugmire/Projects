package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetFollowingTask extends PagedUserTask {
    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee, Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws Exception {
        ServerFacade facade = new ServerFacade();

        String lastAlias = null;
        User last = getLastItem();
        if (last != null) {
            lastAlias = last.getAlias();
        }
        FollowingRequest request = new FollowingRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastAlias);

        FollowingResponse response = facade.getFollowing(request, "/getfollowing");
        List<User> followees = response.getFollowees();
        Boolean hasMorePages = response.getHasMorePages();
        return new Pair<>(followees, hasMorePages);
    }
}
