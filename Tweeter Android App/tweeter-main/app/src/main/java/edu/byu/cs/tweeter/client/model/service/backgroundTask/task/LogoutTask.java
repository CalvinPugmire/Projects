package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public class LogoutTask extends AuthenticatedTask {
    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        LogoutRequest request = new LogoutRequest(getAuthToken());

        LogoutResponse response = facade.logout(request, "/logout");
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to logout.");
        }
    }
}
