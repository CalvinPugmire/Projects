package edu.byu.cs.tweeter.client.cache;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class Cache {
    private static Cache instance = new Cache();

    public static Cache getInstance() {
        return instance;
    }

    public static void setInstance(Cache instance) {
        Cache.instance = instance;
    }

    private User currUser;
    private AuthToken currUserAuthToken;

    private Cache() {
        initialize();
    }

    private void initialize() {
        currUser = new User(null, null, null);
        currUserAuthToken = null;
    }

    public void clearCache() {
        initialize();
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public AuthToken getCurrUserAuthToken() {
        return currUserAuthToken;
    }

    public void setCurrUserAuthToken(AuthToken currUserAuthToken) {
        this.currUserAuthToken = currUserAuthToken;
    }
}
