package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetFeedTask extends PagedStatusTask {
    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus, Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws Exception {
        ServerFacade facade = new ServerFacade();

        String lastDatetime = null;
        Status last = getLastItem();
        if (last != null) {
            lastDatetime = last.getDatetime();
        }
        FeedRequest request = new FeedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastDatetime);

        FeedResponse response = facade.getFeed(request, "/getfeed");
        List<Status> feed = response.getFeed();
        Boolean hasMorePages = response.getHasMorePages();
        return new Pair<>(feed, hasMorePages);
    }
}
