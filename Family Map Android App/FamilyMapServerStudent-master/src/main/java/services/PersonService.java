package services;

import daos.*;
import models.*;
import results.*;

/**
 * A person service.
 */
public class PersonService {
    /**
     * Performs a person operation.
     */
    public PersonResult performService(String authtoken) {
        Database db = new Database();
        try {
            db.openConnection();

            PersonResult result = serviceSub(authtoken, db);

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

            PersonResult result = new PersonResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private PersonResult serviceSub(String authtoken, Database db) throws Exception {
        PersonDao persondao = new PersonDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (authtokendao.find(authtoken) != null) {
            Person[] data = persondao.findAll(authtokendao.find(authtoken).getUsername());

            PersonResult result = new PersonResult();
            result.result(data, true);

            return result;
        } else {
            throw new Exception("Error: invalid authtoken.");
        }
    }
}