package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoadRequest;
import services.LoadService;
import results.LoadResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private Database db;
    private User bestUser;
    private User newUser1;
    private User newUser2;
    private User newUser3;
    private UserDao uDao;
    private Person bestPerson;
    private Person newPerson1;
    private Person newPerson2;
    private Person newPerson3;
    private PersonDao pDao;
    private Event bestEvent;
    private Event newEvent1;
    private Event newEvent2;
    private Event newEvent3;
    private EventDao eDao;
    private AuthToken bestToken;
    private AuthTokenDao aDao;
    private LoadRequest loadRequest;
    private LoadService loadService;
    private LoadResult loadResult;
    private User[] newUsers;
    private Person[] newPersons;
    private Event[] newEvents;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "M", "000118");
        bestPerson = new Person("000118", "human_118", "Joe",
                "Mama", "M", "000116", "000115", "000119");
        bestEvent = new Event("Biking_1230", "human-118", "000118",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestToken = new AuthToken("118000", "human_118");

        newUser1 = new User("human_100", "abcd", "bateman@gmail.com",
                "John", "Smith", "M", "000100");
        newPerson1 = new Person("000100", "human_100", "John",
                "Smith", "M", "000103", "000104", "000105");
        newEvent1 = new Event("Biking_123A", "human_100", "000100",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        newUser2 = new User("human_101", "efgh", "patrick@gmail.com",
                "Patrick", "Morose", "M", "000101");
        newPerson2 = new Person("000101", "human_101", "Patrick",
                "Morose", "M", "000106", "000107", "000108");
        newEvent2 = new Event("Cycling_123B", "human_101", "000101",
                68.9f, 14.1f, "United States", "Alabama",
                "Cycling_Around", 2020);
        newUser3 = new User("human_102", "ijkl", "killer@gmail.com",
                "Joe", "Bateman", "M", "000102");
        newPerson3 = new Person("000102", "human_102", "Joe",
                "Bateman", "M", "000109", "000110", "000111");
        newEvent3 = new Event("Walking_123C", "human_102", "000102",
                56.8f, 100.5f, "Russia", "Slovenia",
                "Walking_Around", 1999);

        newUsers = new User[] {newUser1,newUser2,newUser3};
        newPersons = new Person[] {newPerson1,newPerson2,newPerson3};
        newEvents = new Event[] {newEvent1,newEvent2,newEvent3};

        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);

        loadRequest = new LoadRequest();
        loadService = new LoadService();
        loadResult = new LoadResult();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        getConnection();
        db.clearTables();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void performServicePass() throws DataAccessException {
        uDao.insert(bestUser);
        pDao.insert(bestPerson);
        eDao.insert(bestEvent);
        aDao.insert(bestToken);

        User compareTest1 = uDao.find(bestUser.getUsername());
        Person compareTest2 = pDao.find(bestPerson.getPersonID());
        Event compareTest3 = eDao.find(bestEvent.getEventID());
        AuthToken compareTest4 = aDao.find(bestToken.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest3);
        assertNotNull(compareTest4);

        db.closeConnection(true);

        loadRequest.request(newUsers, newPersons, newEvents);
        loadResult = loadService.performService(loadRequest);
        assertNotNull(loadResult);
        assertEquals("Successfully added 3 users, 3 persons, and 3 events to the database.", loadResult.getMessage());
        assertTrue(loadResult.isSuccess());

        getConnection();

        User compareTest5 = uDao.find(bestUser.getUsername());
        Person compareTest6 = pDao.find(bestPerson.getPersonID());
        Event compareTest7 = eDao.find(bestEvent.getEventID());
        AuthToken compareTest8 = aDao.find(bestToken.getAuthtoken());
        assertNull(compareTest5);
        assertNull(compareTest6);
        assertNull(compareTest7);
        assertNull(compareTest8);

        User compareTest9 = uDao.find(newUser1.getUsername());
        User compareTest10 = uDao.find(newUser2.getUsername());
        User compareTest11 = uDao.find(newUser3.getUsername());
        Person compareTest12 = pDao.find(newPerson1.getPersonID());
        Person compareTest13 = pDao.find(newPerson2.getPersonID());
        Person compareTest14 = pDao.find(newPerson3.getPersonID());
        Event compareTest15 = eDao.find(newEvent1.getEventID());
        Event compareTest16 = eDao.find(newEvent2.getEventID());
        Event compareTest17 = eDao.find(newEvent3.getEventID());
        assertNotNull(compareTest9);
        assertNotNull(compareTest10);
        assertNotNull(compareTest11);
        assertNotNull(compareTest11);
        assertNotNull(compareTest12);
        assertNotNull(compareTest13);
        assertNotNull(compareTest14);
        assertNotNull(compareTest15);
        assertNotNull(compareTest16);
        assertNotNull(compareTest17);
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        performServicePass();
    }
}
