package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.PersonIDService;
import results.PersonIDResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonIDServiceTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;
    private AuthToken bestToken;
    private AuthToken worstToken;
    private AuthTokenDao aDao;
    private PersonIDService personIDService;
    private PersonIDResult personIDResult;
    private PersonIDResult bestResult;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestPerson = new Person("000118", "human_118", "Joe",
                "Mama", "M", "000116", "000115", "000119");
        bestToken = new AuthToken("118000", "human_118");
        worstToken = new AuthToken("abcdefu", "joe_mama");

        bestResult = new PersonIDResult();
        bestResult.result("human_118", "000118", "Joe", "Mama",
                "M", "000116", "000115", "000119", true);

        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);

        personIDService = new PersonIDService();
        personIDResult = new PersonIDResult();
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
        pDao.insert(bestPerson);
        aDao.insert(bestToken);

        Person compareTest1 = pDao.find(bestPerson.getPersonID());
        AuthToken compareTest2 = aDao.find(bestToken.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);

        db.closeConnection(true);

        personIDResult = personIDService.performService(bestPerson.getPersonID(), bestToken.getAuthtoken());

        assertNotNull(personIDResult);

        assertEquals(bestResult.getAssociatedUsername(), personIDResult.getAssociatedUsername());
        assertEquals(bestResult.getPersonID(), personIDResult.getPersonID());
        assertEquals(bestResult.getFirstName(), personIDResult.getFirstName());
        assertEquals(bestResult.getLastName(), personIDResult.getLastName());
        assertEquals(bestResult.getGender(), personIDResult.getGender());
        assertEquals(bestResult.getFatherID(), personIDResult.getFatherID());
        assertEquals(bestResult.getMotherID(), personIDResult.getMotherID());
        assertEquals(bestResult.getSpouseID(), personIDResult.getSpouseID());
        assertEquals(bestResult.isSuccess(), personIDResult.isSuccess());

        getConnection();
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        pDao.insert(bestPerson);
        aDao.insert(bestToken);
        aDao.insert(worstToken);

        Person compareTest1 = pDao.find(bestPerson.getPersonID());
        AuthToken compareTest2 = aDao.find(bestToken.getAuthtoken());
        AuthToken compareTest3 = aDao.find(worstToken.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest3);

        db.closeConnection(true);

        personIDResult = personIDService.performService(bestPerson.getPersonID(), "blahblah");
        assertEquals("Error: invalid authtoken.", personIDResult.getMessage());
        assertFalse(personIDResult.isSuccess());

        personIDResult = personIDService.performService("blahblah", bestToken.getAuthtoken());
        assertEquals("Error: invalid personID.", personIDResult.getMessage());
        assertFalse(personIDResult.isSuccess());

        personIDResult = personIDService.performService(bestPerson.getPersonID(), worstToken.getAuthtoken());
        assertEquals("Error: requested person does not belong to this user.", personIDResult.getMessage());
        assertFalse(personIDResult.isSuccess());

        getConnection();
    }
}
