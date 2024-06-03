package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

public class GetFollowersCountTask extends GetCountTask {
    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        FollowersCountRequest request = new FollowersCountRequest(getAuthToken(), getTargetUser());

        FollowersCountResponse response = facade.getFollowersCount(request, "/getfollowerscount");
        int followersCount = response.getFollowersCount();
        return followersCount;
    }
}
