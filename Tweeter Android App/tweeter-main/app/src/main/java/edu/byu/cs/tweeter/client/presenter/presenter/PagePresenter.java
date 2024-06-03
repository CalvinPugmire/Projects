package edu.byu.cs.tweeter.client.presenter.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.client.model.service.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.PageView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagePresenter<T extends PageView, U extends Object> extends BasePresenter<T> {
    private static final int PAGE_SIZE = 10;
    private final UserService userService;
    protected U lastItem;
    private boolean hasMorePages;
    protected boolean isLoading = false;

    public PagePresenter(T view) {
        super(view);
        userService = new UserService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            loadAction(user, PAGE_SIZE, lastItem);
        }
    }
    protected abstract void loadAction (User user, int pageSize, U lastItem);

    public void getUser(String subject) {
        userService.getUser(subject, new UsrObserver());
        view.displayMessage("Getting user's profile...");
    }

    public class PgObserver extends BasePresenter<PageView<U>>.ServObserver implements PageObserver<U> {
        @Override
        public void handleAddItems(List<U> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            getLastItem(items);
            setHasMorePages(hasMorePages);
            view.addMoreItems(items);
        }
        protected void getLastItem (List<U> items) {}

        @Override
        public void actionFailure() {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }
    }

    public class UsrObserver extends BasePresenter<PageView<U>>.ServObserver implements UserObserver {
        @Override
        public void handleGetUser(User user) {
            view.userSuccessful(user);
        }

        @Override
        public String getFailMessage() {
            return "Failed to get user's profile: ";
        }

        @Override
        protected String getExMessage() {
            return "Failed to get user's profile because of exception: ";
        }
    }
}
