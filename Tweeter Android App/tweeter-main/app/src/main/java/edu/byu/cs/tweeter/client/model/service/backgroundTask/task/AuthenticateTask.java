package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    private User authenticatedUser;
    private AuthToken authToken;
    protected final String username;
    protected final String password;

    protected AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected final void runTask() throws Exception {
        Pair<User, AuthToken> loginResult = runAuthenticationTask();

        authenticatedUser = loginResult.getFirst();
        authToken = loginResult.getSecond();

        if (authenticatedUser != null && authToken != null) {
            sendSuccessMessage();
        } else {
            sendFailedMessage("Failed to authenticate user.");
        }
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask() throws Exception;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }
}
