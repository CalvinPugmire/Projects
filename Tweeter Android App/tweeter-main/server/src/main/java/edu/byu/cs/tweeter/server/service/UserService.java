package edu.byu.cs.tweeter.server.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.base.DAOFactory;

public class UserService extends BaseService {
    public UserService (DAOFactory factory) {
        super(factory);
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        String reqHashedPassword = hashPassword(request.getPassword());
        User user = userDAO.getUser(request.getUsername(), reqHashedPassword);

        AuthToken authToken = userDAO.insertAuthToken(user.getAlias());

        return new LoginResponse(user, authToken);
    }

    public GetUserResponse getUser (GetUserRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Missing an alias");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!userDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        User user = userDAO.getUser(request.getUserAlias());

        return new GetUserResponse(true, user);
    }

    public LogoutResponse logout (LogoutRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        boolean success = userDAO.deleteAuthToken(request.getAuthToken());

        return new LogoutResponse(success);
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getFirstName() == null){
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing a profile image");
        }

        User existingUser = userDAO.getUser(request.getUsername());
        if (existingUser != null) {
            return new RegisterResponse("A user already exists with that username");
        }

        String hashedPassword = hashPassword(request.getPassword());
        String imageUrl = s3DAO.uploadImage(request.getUsername(), request.getImage());
        User user = userDAO.insertUser(request.getFirstName(), request.getLastName(),
                request.getUsername(), hashedPassword, imageUrl);

        AuthToken authToken = userDAO.insertAuthToken(user.getAlias());

        return new RegisterResponse(user, authToken);
    }

    private static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }
}
