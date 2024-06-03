package edu.byu.cs.tweeter.client.presenter.presenter;

import edu.byu.cs.tweeter.client.model.service.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.LogRegView;

public class LoginPresenter extends LogRegPresenter<LoginPresenter.View> {
    public interface View extends LogRegView {}

    public LoginPresenter(View view) {
        super(view);
    }

    public void initiateLogin(String username, String password) {
        String validationMessage = validateLogin(username,password);
        view.displayMessage("Logging in...");
        if (validationMessage == null) {
            UserService userService = new UserService();
            userService.login(username, password, new LoginObserver());
        } else {
            view.displayError(validationMessage);
        }
    }

    public String validateLogin(String username, String password) {
        if (username.length() > 0 && username.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (username.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return null;
    }

    public class LoginObserver extends LogRegPresenter<LoginPresenter.View>.LgRgOtObserver {
        @Override
        public String getFailMessage() {
            return "Failed to login: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to login because of exception: ";
        }
    }
}
