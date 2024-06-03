package services;

import daos.*;
import models.*;
import results.*;

/**
 * An event service.
 */
public class EventService {
    /**
     * Performs an event operation.
     */
    public EventResult performService(String authtoken) {
        Database db = new Database();
        try {
            db.openConnection();

            EventResult result = serviceSub(authtoken, db);

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

            EventResult result = new EventResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private EventResult serviceSub(String authtoken, Database db) throws Exception {
        EventDao eventdao = new EventDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (authtokendao.find(authtoken) != null) {
            Event[] data = eventdao.findAll(authtokendao.find(authtoken).getUsername());

            EventResult result = new EventResult();
            result.result(data, true);

            return result;
        } else {
            throw new Exception("Error: invalid authtoken.");
        }
    }
}