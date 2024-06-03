package edu.byu.cs.tweeter.server.dao.base;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.Follows;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.User_;

public interface FollowDAO extends BaseDAO {
    public List<String> getFollowers(String alias);

    List<String> getPageOfFollowers(String followeeAlias, int pageSize, String lastFollowerAlias);

    List<String> getPageOfFollowees(String followerAlias, int pageSize, String lastFolloweeAlias);

    boolean getFollow(User follower, User followee);

    boolean insertFollow(String followerAlias, String followeeAlias);

    boolean deleteFollow(String followerAlias, String followeeAlias);

    void addFollowersBatch(List<String> followers, String followee);
}
