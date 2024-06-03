package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

public class LoginTask extends AuthenticateTask {
    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        LoginRequest request = new LoginRequest(username, password);

        LoginResponse response = facade.login(request, "/login");
        User loggedInUser = response.getUser();
        AuthToken authToken = response.getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }
}
