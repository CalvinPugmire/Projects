package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PageObserver<T extends Object> extends ServiceObserver {
    void handleAddItems(List<T> items, boolean hasMorePages);
}
