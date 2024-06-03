package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

public class FollowTask extends AuthenticatedTask {
    private final User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        FollowRequest request = new FollowRequest(getAuthToken(), followee.getAlias());

        FollowResponse response = facade.follow(request, "/follow");
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to follow.");
        }
    }

}
