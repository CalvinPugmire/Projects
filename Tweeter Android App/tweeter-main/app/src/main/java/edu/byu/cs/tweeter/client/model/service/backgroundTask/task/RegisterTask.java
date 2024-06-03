package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.Pair;

public class RegisterTask extends AuthenticateTask {
    private final String firstName;
    private final String lastName;
    private final String image;

    public RegisterTask(String firstName, String lastName, String username, String password, String image, Handler messageHandler) {
        super(messageHandler, username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() throws Exception {
        ServerFacade facade = new ServerFacade();

        RegisterRequest request = new RegisterRequest(firstName, lastName, username, password, image);

        RegisterResponse response = facade.register(request, "/register");
        User registeredUser = response.getUser();
        AuthToken authToken = response.getAuthToken();
        return new Pair<>(registeredUser, authToken);
    }
}
