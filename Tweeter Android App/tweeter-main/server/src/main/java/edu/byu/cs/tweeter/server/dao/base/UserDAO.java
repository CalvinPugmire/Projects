package edu.byu.cs.tweeter.server.dao.base;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.User_;

public interface UserDAO extends BaseDAO {
    User getUser(String alias);

    User getUser(String alias, String password);

    User insertUser(String firstName, String lastName, String alias, String password, String imageURL);

    int getFollowersCount(String alias);

    int getFollowingCount(String alias);

    public void updateUserCounts(String alias, int followersChange, int followeesChange);

    void addUserBatch(List<User> users, String password);
}
