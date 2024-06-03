package services;

import java.io.FileReader;
import java.io.Reader;
import java.util.UUID;
import java.lang.Math;

import com.google.gson.Gson;
import daos.*;
import models.*;
import results.*;

/**
 * A fill service.
 */
public class FillService {
    /**
     * Performs a fill operation.
     */
    public FillResult performService(String username, int generations) {
        Database db = new Database();
        try {
            db.openConnection();

            FillResult result = serviceSub(username, generations, db);

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

            FillResult result = new FillResult();
            result.result(ex.getMessage(), false);

            return result;
        }
    }

    public FillResult serviceSub(String username, int generations, Database db) throws Exception {
        UserDao userdao = new UserDao(db.getConnection());
        PersonDao persondao = new PersonDao(db.getConnection());
        EventDao eventdao = new EventDao(db.getConnection());

        if (userdao.find(username) == null) {
            throw new Exception("Error: user does not exist.");
        }
        if (generations < 0) {
            throw new Exception("Error: invalid generation count.");
        }

        persondao.clear(username);
        eventdao.clear(username);

        Gson gson = new Gson();
        Reader reader = new FileReader("json/locations.json");
        LocationData locdata = (LocationData)gson.fromJson(reader, LocationData.class);
        reader = new FileReader("json/snames.json");
        StringData surdata = (StringData)gson.fromJson(reader, StringData.class);
        reader = new FileReader("json/mnames.json");
        StringData mendata = (StringData)gson.fromJson(reader, StringData.class);
        reader = new FileReader("json/fnames.json");
        StringData femdata = (StringData)gson.fromJson(reader, StringData.class);

        generatePerson(generations+1, 2000, username, userdao, persondao, eventdao, locdata, surdata, mendata, femdata);

        int personcount = 1;
        for (int i = 1; i <= generations; i++) {
            personcount = personcount + (int)Math.pow(2,i);
        }
        int eventcount = personcount*3-2;

        FillResult result = new FillResult();
        result.result("Successfully added " + personcount + " persons and " + eventcount + " events to the database.", true);

        return result;
    }

    private void generatePerson(int generations, int birthyear, String username, UserDao userdao, PersonDao persondao, EventDao eventdao, LocationData locdata, StringData surdata, StringData mendata, StringData femdata) throws DataAccessException {
        User user = userdao.find(username);

        Person mother = null;
        Person father = null;

        if (generations > 1) {
            mother = generateTree(generations-1, "f", user.getLastName(), birthyear-30, username, userdao, persondao, eventdao, locdata, surdata, mendata, femdata);
            father = generateTree(generations-1, "m", user.getLastName(), birthyear-30, username, userdao, persondao, eventdao, locdata, surdata, mendata, femdata);

            mother.setSpouseID(father.getPersonID());
            persondao.insert(mother);
            father.setSpouseID(mother.getPersonID());
            persondao.insert(father);

            Location location = locdata.getRandom();
            String eventmID = UUID.randomUUID().toString();
            Event eventm = new Event(eventmID, mother.getAssociatedUsername(), mother.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", birthyear-5);
            eventdao.insert(eventm);
            String eventfID = UUID.randomUUID().toString();
            Event eventf = new Event(eventfID, father.getAssociatedUsername(), father.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", birthyear-5);
            eventdao.insert(eventf);
        }

        Person person = null;
        if (mother != null) {
            person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender(), father.getPersonID(), mother.getPersonID(), null);
        } else {
            person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender(), null, null, null);
        }
        persondao.insert(person);

        String eventID = UUID.randomUUID().toString();
        Location location = locdata.getRandom();
        Event event = new Event(eventID, person.getAssociatedUsername(), person.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "birth", birthyear);
        eventdao.insert(event);
    }

    private Person generateTree(int generations, String gender, String lastname, int birthyear, String username, UserDao userdao, PersonDao persondao, EventDao eventdao, LocationData locdata, StringData surdata, StringData mendata, StringData femdata) throws DataAccessException {
        Person mother = null;
        Person father = null;

        if (gender.equals("m")) {
            lastname = lastname;
        } else {
            lastname = surdata.getRandom();
        }

        if (generations > 1) {
            mother = generateTree(generations-1, "f", lastname, birthyear-30, username, userdao, persondao, eventdao, locdata, surdata, mendata, femdata);
            father = generateTree(generations-1, "m", lastname, birthyear-30, username, userdao, persondao, eventdao, locdata, surdata, mendata, femdata);

            mother.setSpouseID(father.getPersonID());
            persondao.insert(mother);
            father.setSpouseID(mother.getPersonID());
            persondao.insert(father);

            Location location = locdata.getRandom();
            String eventmID = UUID.randomUUID().toString();
            Event eventm = new Event(eventmID, mother.getAssociatedUsername(), mother.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", birthyear-5);
            eventdao.insert(eventm);
            String eventfID = UUID.randomUUID().toString();
            Event eventf = new Event(eventfID, father.getAssociatedUsername(), father.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", birthyear-5);
            eventdao.insert(eventf);
        }

        String personID = UUID.randomUUID().toString();
        String firstname = null;
        if (gender.equals("m")) {
            firstname = mendata.getRandom();
        } else {
            firstname = femdata.getRandom();
        }
        Person person = null;
        if (mother != null) {
            person = new Person(personID, username, firstname, lastname, gender, father.getPersonID(), mother.getPersonID(), null);
        } else {
            person = new Person(personID, username, firstname, lastname, gender, null, null, null);
        }

        String eventbID = UUID.randomUUID().toString();
        Location locationb = locdata.getRandom();
        Event eventb = new Event(eventbID, person.getAssociatedUsername(), person.getPersonID(), locationb.getLatitude(), locationb.getLongitude(), locationb.getCountry(), locationb.getCity(), "birth", birthyear);
        eventdao.insert(eventb);

        String eventdID = UUID.randomUUID().toString();
        Location locationd = locdata.getRandom();
        Event eventd = new Event(eventdID, person.getAssociatedUsername(), person.getPersonID(), locationd.getLatitude(), locationd.getLongitude(), locationd.getCountry(), locationd.getCity(), "death", birthyear+80);
        eventdao.insert(eventd);

        return person;
    }
}
