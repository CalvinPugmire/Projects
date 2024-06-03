package services;

import java.util.UUID;

import daos.*;
import models.*;
import requests.LoginRequest;
import results.LoginResult;

/**
 * A login service.
 */
public class LoginService {
    /**
     * Performs a login operation.
     */
    public LoginResult performService(LoginRequest request) {
        Database db = new Database();
        try {
            db.openConnection();

            LoginResult result = serviceSub(request, db);

            db.closeConnection(true);

            return result;
        }
        catch (Exception ex) {
            ex.printStackTrace();

            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            LoginResult result = new LoginResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private LoginResult serviceSub(LoginRequest request, Database db) throws Exception {
        UserDao userdao = new UserDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (userdao.find(request.getUsername()) == null) {
            throw new Exception("Error: user does not exist.");
        }

        if (userdao.find(request.getUsername()).getUsername().equals(request.getUsername()) &&
                userdao.find(request.getUsername()).getPassword().equals(request.getPassword())) {
            String authtoken = UUID.randomUUID().toString();
            AuthToken token = new AuthToken(authtoken, request.getUsername());
            authtokendao.insert(token);

            LoginResult result = new LoginResult();
            result.result(authtoken, request.getUsername(), userdao.find(request.getUsername()).getPersonID(), true);

            return result;
        } else {
            throw new Exception("Error: user does not match any existing user.");
        }
    }
}
