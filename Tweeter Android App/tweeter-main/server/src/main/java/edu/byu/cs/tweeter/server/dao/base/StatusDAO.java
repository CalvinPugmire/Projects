package edu.byu.cs.tweeter.server.dao.base;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.PreFeedBatch;

public interface StatusDAO extends BaseDAO {
    List<Status> getPageOfFeed(String alias, int pageSize, String lastDatetime);

    List<Status> getPageOfStory(String alias, int pageSize, String lastDatetime);

    boolean insertStory(Status status);

    void insertFeed(PreFeedBatch preFeedBatch);
}
