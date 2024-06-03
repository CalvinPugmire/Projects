package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

public class GetStoryTask extends PagedStatusTask {
    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus, Handler messageHandler) {
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
        StoryRequest request = new StoryRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), lastDatetime);

        StoryResponse response = facade.getStory(request, "/getstory");
        List<Status> story = response.getStory();
        Boolean hasMorePages = response.getHasMorePages();
        return new Pair<>(story, hasMorePages);
    }
}
