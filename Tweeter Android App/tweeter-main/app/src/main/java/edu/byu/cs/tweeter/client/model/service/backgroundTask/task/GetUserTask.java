package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

public class GetUserTask extends AuthenticatedTask {
    public static final String USER_KEY = "user";
    private final String alias;
    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);
        this.alias = alias;
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        GetUserRequest request = new GetUserRequest(getAuthToken(), alias);

        GetUserResponse response = facade.getUser(request, "/getuser");
        user = response.getUser();
        if (response.isSuccess()) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to get user.");
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }
}
