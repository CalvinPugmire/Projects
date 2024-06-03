package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

public class UnfollowTask extends AuthenticatedTask {
    private final User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        UnfollowRequest request = new UnfollowRequest(getAuthToken(), followee.getAlias());

        UnfollowResponse response = facade.unfollow(request, "/unfollow");
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to unfollow.");
        }
    }
}
