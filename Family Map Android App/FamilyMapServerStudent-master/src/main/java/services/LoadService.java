package services;

import daos.*;
import requests.*;
import results.*;

/**
 * A load service.
 */
public class LoadService {
    /**
     * Performs a load operation.
     */
    public LoadResult performService(LoadRequest request) {
        Database db = new Database();
        try {
            db.openConnection();

            LoadResult result = serviceSub(request, db);

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

            LoadResult result = new LoadResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    private LoadResult serviceSub(LoadRequest request, Database db) throws Exception {
        UserDao userdao = new UserDao(db.getConnection());
        PersonDao persondao = new PersonDao(db.getConnection());
        EventDao eventdao = new EventDao(db.getConnection());
        AuthTokenDao authtokendao = new AuthTokenDao(db.getConnection());

        int i = request.getUsers().length;
        int j = request.getPersons().length;
        int k = request.getEvents().length;

        userdao.clear();
        persondao.clear();
        eventdao.clear();
        authtokendao.clear();

        for (int c = 0; c < i; c++) {
            userdao.insert(request.getUsers()[c]);
        }
        for (int c = 0; c < j; c++) {
            persondao.insert(request.getPersons()[c]);
        }
        for (int c = 0; c < k; c++) {
            eventdao.insert(request.getEvents()[c]);
        }

        LoadResult result = new LoadResult();
        result.result("Successfully added " + i + " users, " + j + " persons, and " + k + " events to the database.", true);

        return result;
    }
}
