package edu.byu.cs.tweeter.server.dao.base;

public interface DAOFactory {
    FollowDAO getFollowDAO();
    StatusDAO getStatusDAO();
    UserDAO getUserDAO();
    S3DAO getS3DAO();
}
