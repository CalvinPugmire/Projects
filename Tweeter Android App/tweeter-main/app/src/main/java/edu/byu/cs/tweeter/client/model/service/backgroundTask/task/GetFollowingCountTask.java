package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

public class GetFollowingCountTask extends GetCountTask {
    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        FollowingCountRequest request = new FollowingCountRequest(getAuthToken(), getTargetUser());

        FollowingCountResponse response = facade.getFollowingCount(request, "/getfollowingcount");
        int followingCount = response.getFollowingCount();
        return followingCount;
    }
}
