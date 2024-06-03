package services;

import models.*;
import daos.*;
import results.*;

/**
 * A personID service.
 */
public class PersonIDService {
    /**
     * Performs a personID operation.
     */
    public PersonIDResult performService(String personID, String authtoken) {
        Database db = new Database();
        try {
            db.openConnection();

            PersonIDResult result = serviceSub(personID, authtoken, db);

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

            PersonIDResult result = new PersonIDResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private PersonIDResult serviceSub(String personID, String authtoken, Database db) throws Exception {
        PersonDao persondao = new PersonDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (authtokendao.find(authtoken) != null) {
            if (persondao.find(personID) != null) {
                if (authtokendao.find(authtoken).getUsername().equals(persondao.find(personID).getAssociatedUsername())) {
                    Person person = persondao.find(personID);

                    PersonIDResult result = new PersonIDResult();
                    result.result(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);

                    return result;
                } else {
                    throw new Exception("Error: requested person does not belong to this user.");
                }
            } else {
                throw new Exception("Error: invalid personID.");
            }
        } else {
            throw new Exception("Error: invalid authtoken.");
        }
    }
}