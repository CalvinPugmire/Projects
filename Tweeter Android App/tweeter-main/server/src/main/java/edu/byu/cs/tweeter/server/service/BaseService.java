package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.base.DAOFactory;
import edu.byu.cs.tweeter.server.dao.base.FollowDAO;
import edu.byu.cs.tweeter.server.dao.base.S3DAO;
import edu.byu.cs.tweeter.server.dao.base.StatusDAO;
import edu.byu.cs.tweeter.server.dao.base.UserDAO;

public class BaseService {
    protected FollowDAO followDAO;
    protected StatusDAO statusDAO;
    protected UserDAO userDAO;
    protected S3DAO s3DAO;

    public BaseService (DAOFactory factory) {
        this.followDAO = factory.getFollowDAO();
        this.statusDAO = factory.getStatusDAO();
        this.userDAO = factory.getUserDAO();
        this.s3DAO = factory.getS3DAO();
    }
}
