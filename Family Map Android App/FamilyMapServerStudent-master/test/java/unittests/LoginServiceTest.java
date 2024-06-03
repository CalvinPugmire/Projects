package unittests;

import daos.*;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import services.LoginService;
import results.LoginResult;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;
    private AuthTokenDao aDao;
    private LoginRequest loginRequest;
    private LoginService loginService;
    private LoginResult loginResult;

    public void getConnection() throws DataAccessException {
        db = new Database();
        Connection conn = db.getConnection();

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "M", "000118");

        uDao = new UserDao(conn);
        aDao = new AuthTokenDao(conn);

        loginRequest = new LoginRequest();
        loginService = new LoginService();
        loginResult = new LoginResult();
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

        User compareTest1 = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest1);

        db.closeConnection(true);

        loginRequest.request(bestUser.getUsername(), bestUser.getPassword());
        loginResult = loginService.performService(loginRequest);
        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());

        String authtoken = loginResult.getAuthtoken();

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

        loginRequest.request("blahblah", bestUser.getPassword());
        loginResult = loginService.performService(loginRequest);
        assertEquals("Error: user does not exist.", loginResult.getMessage());
        assertFalse(loginResult.isSuccess());

        loginRequest.request(bestUser.getUsername(), "blahblah");
        loginResult = loginService.performService(loginRequest);
        assertEquals("Error: user does not match any existing user.", loginResult.getMessage());
        assertFalse(loginResult.isSuccess());

        getConnection();
    }
}
