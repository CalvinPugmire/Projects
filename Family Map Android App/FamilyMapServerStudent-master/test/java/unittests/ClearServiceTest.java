package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ClearService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;
    private Person bestPerson;
    private PersonDao pDao;
    private Event bestEvent;
    private EventDao eDao;
    private AuthToken bestToken;
    private AuthTokenDao aDao;
    private ClearService clearService;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "M", "000118");
        bestPerson = new Person("000118", "human_118", "Joe",
                "Mama", "M", "000116", "000115", "000119");
        bestEvent = new Event("Biking_123A", "human-118", "000118",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        bestToken = new AuthToken("118000", "human_118");

        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);

        clearService = new ClearService();
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

        clearService.performService();

        getConnection();

        User compareTest5 = uDao.find(bestUser.getUsername());
        Person compareTest6 = pDao.find(bestPerson.getPersonID());
        Event compareTest7 = eDao.find(bestEvent.getEventID());
        AuthToken compareTest8 = aDao.find(bestToken.getAuthtoken());
        assertNull(compareTest5);
        assertNull(compareTest6);
        assertNull(compareTest7);
        assertNull(compareTest8);
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        performServicePass();
    }
}
