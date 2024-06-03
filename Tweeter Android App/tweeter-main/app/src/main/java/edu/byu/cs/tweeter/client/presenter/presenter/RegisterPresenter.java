package edu.byu.cs.tweeter.client.presenter.presenter;

import edu.byu.cs.tweeter.client.model.service.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.LogRegView;

public class RegisterPresenter extends LogRegPresenter<RegisterPresenter.View> {
    public interface View extends LogRegView {}

    public RegisterPresenter(View view) {
        super(view);
    }

    public void initiateRegister(String username, String password, String firstName, String lastName, String imageBytesBase64) {
        String validationMessage = validateRegistration(username, password, firstName, lastName, imageBytesBase64);
        view.displayMessage("Registering...");
        if (validationMessage == null) {
            UserService userService = new UserService();
            userService.register(username, password, firstName, lastName, imageBytesBase64, new RegisterObserver());
        } else {
            view.displayError(validationMessage);
        }
    }

    public String validateRegistration(String username, String password, String firstName, String lastName, String imageBytesBase64) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (username.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (username.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (username.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (imageBytesBase64 == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    public class RegisterObserver extends LogRegPresenter<RegisterPresenter.View>.LgRgOtObserver {
        @Override
        public String getFailMessage() {
            return "Failed to register: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to register because of exception: ";
        }
    }
}
