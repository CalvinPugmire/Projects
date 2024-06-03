package services;

import daos.*;
import models.*;
import results.*;

/**
 * A eventID service.
 */
public class EventIDService {
    /**
     * Performs an eventID operation.
     */
    public EventIDResult performService(String eventID, String authtoken) {
        Database db = new Database();
        try {
            db.openConnection();

            EventIDResult result = serviceSub(eventID, authtoken, db);

            db.closeConnection(true);

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();

            try {
                db.closeConnection(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            EventIDResult result = new EventIDResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private EventIDResult serviceSub(String eventID, String authtoken, Database db) throws Exception {
        EventDao eventdao = new EventDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        if (authtokendao.find(authtoken) != null) {
            if (eventdao.find(eventID) != null) {
                if (authtokendao.find(authtoken).getUsername().equals(eventdao.find(eventID).getAssociatedUsername())) {
                    Event event = eventdao.find(eventID);

                    EventIDResult result = new EventIDResult();
                    result.result(event.getAssociatedUsername(), event.getEventID(), event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear(), true);

                    return result;
                } else {
                    throw new Exception("Error: requested event does not belong to this user.");
                }
            } else {
                throw new Exception("Error: invalid eventID.");
            }
        } else {
            throw new Exception("Error: invalid authtoken.");
        }
    }
}