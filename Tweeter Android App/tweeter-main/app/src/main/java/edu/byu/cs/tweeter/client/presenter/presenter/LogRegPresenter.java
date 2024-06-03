package edu.byu.cs.tweeter.client.presenter.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.LogRegObserver;
import edu.byu.cs.tweeter.client.presenter.view.LogRegView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class LogRegPresenter<T extends LogRegView> extends BasePresenter<T> {
    public LogRegPresenter(T view) {
        super(view);
    }

    public class LgRgOtObserver extends BasePresenter<LogRegView>.ServObserver implements LogRegObserver {
        @Override
        public void handleLogReg(User user, AuthToken authToken) {
            view.logRegSuccessful(user, authToken);
        }
    }
}
