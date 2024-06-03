package edu.byu.cs.tweeter.client.model.service.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.model.service.observer.LogRegObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;

public class UserService extends BaseService {
    public void login(String username, String password, LogRegObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        execute(loginTask);
    }

    public void logout(LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        execute(logoutTask);
    }

    public void register(String username, String password, String firstName, String lastName,
                         String imageBytesBase64, LogRegObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password,
                imageBytesBase64, new RegisterHandler(observer));
        execute(registerTask);
    }

    public void getUser (String subject, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                subject, new GetUserHandler(observer));
        execute(getUserTask);
    }
}
