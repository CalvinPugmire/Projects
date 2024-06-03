package edu.byu.cs.tweeter.server.dao.base;

public interface S3DAO {
    String uploadImage(String username, String imageString);
}
