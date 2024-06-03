package edu.byu.cs.tweeter.server.dao.base;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface BaseDAO {
    String getUser(AuthToken token);

    boolean valAuthToken(AuthToken token);

    AuthToken insertAuthToken(String alias);

    boolean deleteAuthToken(AuthToken token);
}
