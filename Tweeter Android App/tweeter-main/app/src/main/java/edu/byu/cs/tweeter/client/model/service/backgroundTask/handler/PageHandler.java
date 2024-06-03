package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PageObserver;

public abstract class PageHandler<T extends PageObserver, U extends PagedTask, V extends Object> extends BaseHandler<T> {
    public PageHandler(T observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Bundle data) {
        List<V> items = (List<V>) data.getSerializable(U.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(U.MORE_PAGES_KEY);
        observer.handleAddItems(items, hasMorePages);
    }
}
