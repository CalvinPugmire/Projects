package edu.byu.cs.tweeter.client.presenter.view;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface PageView<U> extends BaseView {
    void setLoadingFooter(boolean status);
    void addMoreItems(List<U> items);
    void userSuccessful(User user);
}