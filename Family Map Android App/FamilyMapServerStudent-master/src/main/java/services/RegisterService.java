package services;

import java.util.UUID;

import daos.*;
import models.*;
import requests.*;
import results.*;

/**
 * A register service.
 */
public class RegisterService {
    /**
     * Performs a register operation.
     */
    public RegisterResult performService(RegisterRequest request) {
        Database db = new Database();
        try {
            db.openConnection();

            RegisterResult result = serviceSub(request, db);

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

            RegisterResult result = new RegisterResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private RegisterResult serviceSub(RegisterRequest request, Database db) throws Exception {
        UserDao userdao = new UserDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (userdao.find(request.getUsername()) == null) {
            String personID = UUID.randomUUID().toString();
            User user = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), personID);
            userdao.insert(user);

            FillService service = new FillService();
            service.serviceSub(user.getUsername(), 4, db);

            String authtoken = UUID.randomUUID().toString();
            AuthToken token = new AuthToken(authtoken, request.getUsername());
            authtokendao.insert(token);

            RegisterResult result = new RegisterResult();
            result.result(authtoken, request.getUsername(), personID, true);

            return result;
        } else {
            throw new Exception("Error: username already taken by existing user.");
        }
    }
}
