package edu.byu.cs.tweeter.client.presenter.view;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface LogRegView extends BaseView {
    void logRegSuccessful(User user, AuthToken authToken);
}
