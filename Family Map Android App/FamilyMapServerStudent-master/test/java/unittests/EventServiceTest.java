package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.EventService;
import results.EventResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private Database db;
    private Event bestEvent1;
    private Event bestEvent2;
    private Event bestEvent3;
    private EventDao eDao;
    private AuthToken bestToken1;
    private AuthToken bestToken2;
    private AuthTokenDao aDao;
    private EventService eventService;
    private EventResult eventResult;
    private EventResult bestResult1;
    private EventResult bestResult2;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestEvent1 = new Event("Biking_123A", "human_118", "000118",
                35.9f, 140.1f, "Japan", "Ushiku", "Biking_Around", 2016);
        bestEvent2 = new Event("Cycling_123B", "human_118", "000115",
                68.9f, 14.1f, "United States", "Alabama", "Cycling_Around", 2020);
        bestEvent3 = new Event("Walking_123C", "human_117", "000113",
                56.8f, 100.5f, "Russia", "Slovenia", "Walking_Around", 1999);
        bestToken1 = new AuthToken("118000", "human_118");
        bestToken2 = new AuthToken("117000", "human_117");

        Event[] bestList1 = new Event[] {bestEvent1, bestEvent2};
        bestResult1 = new EventResult();
        bestResult1.result(bestList1, true);

        Event[] bestList2 = new Event[] {bestEvent3};
        bestResult2 = new EventResult();
        bestResult2.result(bestList2, true);

        eDao = new EventDao(conn);
        aDao = new AuthTokenDao(conn);

        eventService = new EventService();
        eventResult = new EventResult();
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
        eDao.insert(bestEvent1);
        eDao.insert(bestEvent2);
        eDao.insert(bestEvent3);
        aDao.insert(bestToken1);
        aDao.insert(bestToken2);

        Event compareTest1 = eDao.find(bestEvent1.getEventID());
        Event compareTest2 = eDao.find(bestEvent2.getEventID());
        Event compareTest3 = eDao.find(bestEvent3.getEventID());
        AuthToken compareTest4 = aDao.find(bestToken1.getAuthtoken());
        AuthToken compareTest5 = aDao.find(bestToken2.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest3);
        assertNotNull(compareTest4);
        assertNotNull(compareTest5);

        db.closeConnection(true);

        eventResult = eventService.performService(bestToken1.getAuthtoken());
        assertNotNull(eventResult);
        assertEquals(bestResult1.getData().length, eventResult.getData().length);
        assertEquals(bestResult1.isSuccess(), eventResult.isSuccess());

        eventResult = eventService.performService(bestToken2.getAuthtoken());
        assertNotNull(eventResult);
        assertEquals(bestResult2.getData().length, eventResult.getData().length);
        assertEquals(bestResult2.isSuccess(), eventResult.isSuccess());

        getConnection();
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        eDao.insert(bestEvent1);
        eDao.insert(bestEvent2);
        aDao.insert(bestToken1);

        Event compareTest1 = eDao.find(bestEvent1.getEventID());
        Event compareTest2 = eDao.find(bestEvent2.getEventID());
        AuthToken compareTest4 = aDao.find(bestToken1.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest4);

        db.closeConnection(true);

        eventResult = eventService.performService("blahblah");
        assertEquals("Error: invalid authtoken.", eventResult.getMessage());
        assertFalse(eventResult.isSuccess());

        getConnection();
    }
}
