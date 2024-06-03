package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

public class IsFollowerTask extends AuthenticatedTask {
    public static final String IS_FOLLOWER_KEY = "is-follower";
    private final User follower;
    private final User followee;
    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        IsFollowerRequest request = new IsFollowerRequest(getAuthToken(), follower, followee);

        IsFollowerResponse response = facade.isFollower(request, "/isfollower");
        isFollower = response.getIsFollower();
        if (response.isSuccess()) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to get follower status.");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
