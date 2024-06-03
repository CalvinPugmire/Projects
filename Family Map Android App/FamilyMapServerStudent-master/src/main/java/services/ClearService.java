package services;

import daos.*;
import results.ClearResult;

/**
 * A clear service.
 */
public class ClearService {
    /**
     * Performs a clear operation.
     */
    public ClearResult performService() {
        Database db = new Database();
        try {
            db.openConnection();

            ClearResult result = serviceSub(db);

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

            ClearResult result = new ClearResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private ClearResult serviceSub(Database db) throws Exception {
        UserDao userdao = new UserDao(db.getConnection());
        PersonDao persondao = new PersonDao(db.getConnection());
        EventDao eventdao = new EventDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        userdao.clear();
        persondao.clear();
        eventdao.clear();
        authtokendao.clear();

        ClearResult result = new ClearResult();
        result.result("Clear succeeded.", true);

        return result;
    }
}