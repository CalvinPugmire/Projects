package edu.byu.cs.tweeter.client.presenter.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public abstract class BasePresenter<T extends BaseView> {
    protected T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public class ServObserver implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            actionFailure();
            view.displayMessage(getFailMessage() + message);
        }
        protected String getFailMessage() {return null;}

        @Override
        public void handleException(Exception exception) {
            actionFailure();
            String message = exception.getMessage();
            view.displayMessage(getExMessage() + message);
        }
        protected String getExMessage() {return null;}

        public void actionFailure() {}
    }
}
