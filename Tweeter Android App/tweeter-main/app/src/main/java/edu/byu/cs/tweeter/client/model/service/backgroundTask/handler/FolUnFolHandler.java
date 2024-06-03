package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.observer.FolUnFolObserver;

public abstract class FolUnFolHandler extends BaseHandler<FolUnFolObserver> {
    public FolUnFolHandler(FolUnFolObserver observer) {
        super(observer);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        normalHandleMessage(msg);
        handleAfterMessage(observer);
    }

    protected abstract void handleAfterMessage(FolUnFolObserver observer);
}
