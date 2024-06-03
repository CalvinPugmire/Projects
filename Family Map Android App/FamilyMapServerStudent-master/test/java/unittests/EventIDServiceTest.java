package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.EventIDService;
import results.EventIDResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventIDServiceTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;
    private AuthToken bestToken;
    private AuthToken worstToken;
    private AuthTokenDao aDao;
    private EventIDService eventIDService;
    private EventIDResult eventIDResult;
    private EventIDResult bestResult;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestEvent = new Event("Biking_123A", "human_118", "000118",
                35.9f, 140.1f, "Japan", "Ushiku", "Biking_Around", 2016);
        bestToken = new AuthToken("118000", "human_118");
        worstToken = new AuthToken("abcdefu", "joe_mama");

        bestResult = new EventIDResult();
        bestResult.result("human_118", "Biking_123A", "000118", 35.9f,
                140.1f, "Japan", "Ushiku", "Biking_Around", 2016, true);

        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);

        eventIDService = new EventIDService();
        eventIDResult = new EventIDResult();
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
        eDao.insert(bestEvent);
        aDao.insert(bestToken);

        Event compareTest1 = eDao.find(bestEvent.getEventID());
        AuthToken compareTest2 = aDao.find(bestToken.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);

        db.closeConnection(true);

        eventIDResult = eventIDService.performService(bestEvent.getEventID(), bestToken.getAuthtoken());

        assertNotNull(eventIDResult);

        assertEquals(bestResult.getAssociatedUsername(), eventIDResult.getAssociatedUsername());
        assertEquals(bestResult.getEventID(), eventIDResult.getEventID());
        assertEquals(bestResult.getPersonID(), eventIDResult.getPersonID());
        assertEquals(bestResult.getLatitude(), eventIDResult.getLatitude());
        assertEquals(bestResult.getLongitude(), eventIDResult.getLongitude());
        assertEquals(bestResult.getCountry(), eventIDResult.getCountry());
        assertEquals(bestResult.getCity(), eventIDResult.getCity());
        assertEquals(bestResult.getEventType(), eventIDResult.getEventType());
        assertEquals(bestResult.getYear(), eventIDResult.getYear());
        assertEquals(bestResult.isSuccess(), eventIDResult.isSuccess());

        getConnection();
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        eDao.insert(bestEvent);
        aDao.insert(bestToken);
        aDao.insert(worstToken);

        Event compareTest1 = eDao.find(bestEvent.getEventID());
        AuthToken compareTest2 = aDao.find(bestToken.getAuthtoken());
        AuthToken compareTest3 = aDao.find(worstToken.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest3);

        db.closeConnection(true);

        eventIDResult = eventIDService.performService(bestEvent.getEventID(), "blahblah");
        assertEquals("Error: invalid authtoken.", eventIDResult.getMessage());
        assertFalse(eventIDResult.isSuccess());

        eventIDResult = eventIDService.performService("blahblah", bestToken.getAuthtoken());
        assertEquals("Error: invalid eventID.", eventIDResult.getMessage());
        assertFalse(eventIDResult.isSuccess());

        eventIDResult = eventIDService.performService(bestEvent.getEventID(), worstToken.getAuthtoken());
        assertEquals("Error: requested event does not belong to this user.", eventIDResult.getMessage());
        assertFalse(eventIDResult.isSuccess());

        getConnection();
    }
}
