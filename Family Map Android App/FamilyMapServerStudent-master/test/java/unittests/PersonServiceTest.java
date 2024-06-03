package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.PersonService;
import results.PersonResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    private Database db;
    private Person bestPerson1;
    private Person bestPerson2;
    private Person bestPerson3;
    private PersonDao pDao;
    private AuthToken bestToken1;
    private AuthToken bestToken2;
    private AuthTokenDao aDao;
    private PersonService personService;
    private PersonResult personResult;
    private PersonResult bestResult1;
    private PersonResult bestResult2;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestPerson1 = new Person("000100", "human_118", "John",
                "Smith", "M", "000103", "000104", "000105");
        bestPerson2 = new Person("000101", "human_118", "Patrick",
                "Morose", "M", "000106", "000107", "000108");
        bestPerson3 = new Person("000102", "human_117", "Joe",
                "Bateman", "M", "000109", "000110", "000111");
        bestToken1 = new AuthToken("118000", "human_118");
        bestToken2 = new AuthToken("117000", "human_117");

        Person[] bestList1 = new Person[] {bestPerson1, bestPerson2};
        bestResult1 = new PersonResult();
        bestResult1.result(bestList1, true);

        Person[] bestList2 = new Person[] {bestPerson3};
        bestResult2 = new PersonResult();
        bestResult2.result(bestList2, true);

        pDao = new PersonDao(conn);
        aDao = new AuthTokenDao(conn);

        personService = new PersonService();
        personResult = new PersonResult();
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
        pDao.insert(bestPerson1);
        pDao.insert(bestPerson2);
        pDao.insert(bestPerson3);
        aDao.insert(bestToken1);
        aDao.insert(bestToken2);

        Person compareTest1 = pDao.find(bestPerson1.getPersonID());
        Person compareTest2 = pDao.find(bestPerson2.getPersonID());
        Person compareTest3 = pDao.find(bestPerson3.getPersonID());
        AuthToken compareTest4 = aDao.find(bestToken1.getAuthtoken());
        AuthToken compareTest5 = aDao.find(bestToken2.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest3);
        assertNotNull(compareTest4);
        assertNotNull(compareTest5);

        db.closeConnection(true);

        personResult = personService.performService(bestToken1.getAuthtoken());
        assertNotNull(personResult);
        assertEquals(bestResult1.getData().length, personResult.getData().length);
        assertEquals(bestResult1.isSuccess(), personResult.isSuccess());

        personResult = personService.performService(bestToken2.getAuthtoken());
        assertNotNull(personResult);
        assertEquals(bestResult2.getData().length, personResult.getData().length);
        assertEquals(bestResult2.isSuccess(), personResult.isSuccess());

        getConnection();
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        pDao.insert(bestPerson1);
        pDao.insert(bestPerson2);
        aDao.insert(bestToken1);

        Person compareTest1 = pDao.find(bestPerson1.getPersonID());
        Person compareTest2 = pDao.find(bestPerson2.getPersonID());
        AuthToken compareTest4 = aDao.find(bestToken1.getAuthtoken());
        assertNotNull(compareTest1);
        assertNotNull(compareTest2);
        assertNotNull(compareTest4);

        db.closeConnection(true);

        personResult = personService.performService("blahblah");
        assertEquals("Error: invalid authtoken.", personResult.getMessage());
        assertFalse(personResult.isSuccess());

        getConnection();
    }
}
