package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

public class PostStatusTask extends AuthenticatedTask {
    private final Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        PostStatusRequest request = new PostStatusRequest(getAuthToken(), status);

        PostStatusResponse response = facade.postStatus(request, "/poststatus");
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to post status.");
        }
    }
}
