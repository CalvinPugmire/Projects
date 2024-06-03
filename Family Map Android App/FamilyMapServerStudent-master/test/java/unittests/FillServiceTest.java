package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.FillService;
import results.FillResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;
    private PersonDao pDao;
    private EventDao eDao;
    private FillService fillService;
    private FillResult fillResult;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "M", "000118");

        uDao = new UserDao(conn);
        pDao = new PersonDao(conn);
        eDao = new EventDao(conn);

        fillService = new FillService();
        fillResult = new FillResult();
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

        db.closeConnection(true);

        fillResult = fillService.performService(bestUser.getUsername(), 0);
        assertNotNull(fillResult);
        assertEquals("Successfully added 1 persons and 1 events to the database.", fillResult.getMessage());
        assertTrue(fillResult.isSuccess());

        getConnection();

        assertEquals(1, pDao.findAll(bestUser.getUsername()).length);
        assertEquals(1, eDao.findAll(bestUser.getUsername()).length);

        db.closeConnection(true);

        fillResult = fillService.performService(bestUser.getUsername(), 1);
        assertNotNull(fillResult);
        assertEquals("Successfully added 3 persons and 7 events to the database.", fillResult.getMessage());
        assertTrue(fillResult.isSuccess());

        getConnection();

        assertEquals(3, pDao.findAll(bestUser.getUsername()).length);
        assertEquals(7, eDao.findAll(bestUser.getUsername()).length);

        db.closeConnection(true);

        fillResult = fillService.performService(bestUser.getUsername(), 2);
        assertNotNull(fillResult);
        assertEquals("Successfully added 7 persons and 19 events to the database.", fillResult.getMessage());
        assertTrue(fillResult.isSuccess());

        getConnection();

        assertEquals(7, pDao.findAll(bestUser.getUsername()).length);
        assertEquals(19, eDao.findAll(bestUser.getUsername()).length);

        db.closeConnection(true);

        fillResult = fillService.performService(bestUser.getUsername(), 3);
        assertNotNull(fillResult);
        assertEquals("Successfully added 15 persons and 43 events to the database.", fillResult.getMessage());
        assertTrue(fillResult.isSuccess());

        getConnection();

        assertEquals(15, pDao.findAll(bestUser.getUsername()).length);
        assertEquals(43, eDao.findAll(bestUser.getUsername()).length);

        db.closeConnection(true);

        fillResult = fillService.performService(bestUser.getUsername(), 4);
        assertNotNull(fillResult);
        assertEquals("Successfully added 31 persons and 91 events to the database.", fillResult.getMessage());
        assertTrue(fillResult.isSuccess());

        getConnection();

        assertEquals(31, pDao.findAll(bestUser.getUsername()).length);
        assertEquals(91, eDao.findAll(bestUser.getUsername()).length);
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        uDao.insert(bestUser);

        db.closeConnection(true);

        fillResult = fillService.performService("blahblah", 3);
        assertEquals("Error: user does not exist.", fillResult.getMessage());
        assertFalse(fillResult.isSuccess());

        fillResult = fillService.performService(bestUser.getUsername(), -1);
        assertEquals("Error: invalid generation count.", fillResult.getMessage());
        assertFalse(fillResult.isSuccess());

        getConnection();
    }
}
