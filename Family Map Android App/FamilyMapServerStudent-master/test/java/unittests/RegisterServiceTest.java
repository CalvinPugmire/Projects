package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import services.RegisterService;
import results.RegisterResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;
    private AuthTokenDao aDao;
    private RegisterRequest registerRequest;
    private RegisterService registerService;
    private RegisterResult registerResult;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "M", "000118");

        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);

        registerRequest = new RegisterRequest();
        registerService = new RegisterService();
        registerResult = new RegisterResult();
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
        db.closeConnection(true);

        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());
        registerResult = registerService.performService(registerRequest);
        assertNotNull(registerResult);
        assertTrue(registerResult.isSuccess());

        String authtoken = registerResult.getAuthtoken();

        getConnection();

        AuthToken compareTest2 = aDao.find(authtoken);
        assertNotNull(compareTest2);
    }

    @Test
    public void performServiceFail() throws DataAccessException {
        uDao.insert(bestUser);

        User compareTest1 = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest1);

        db.closeConnection(true);

        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());
        registerResult = registerService.performService(registerRequest);
        assertEquals("Error: username already taken by existing user.", registerResult.getMessage());
        assertFalse(registerResult.isSuccess());

        getConnection();
    }
}
