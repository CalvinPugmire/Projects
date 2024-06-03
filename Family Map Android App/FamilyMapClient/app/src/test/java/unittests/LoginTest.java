package unittests;

import com.example.familymapclient.background.ServerProxy;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import models.User;

import org.junit.Test;

//import org.junit.Assert;
import static org.junit.Assert.*;

public class LoginTest {
    private ServerProxy serverProxy;
    private User bestUser;
    private User worstUser;
    private LoginRequest loginRequest;
    private LoginResult loginResult;

    public void setUp() throws Exception
    {
        serverProxy = new ServerProxy();
        serverProxy.clear("localhost", "8080");

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "m", "any1");

        worstUser = new User("human_118", "victoriassecret", "notjoe@gmail.com",
                "Adam", "Jojo", "m", "any2");

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());
        serverProxy.register(registerRequest, "localhost", "8080");
    }

    @Test
    public void loginPass() throws Exception {
        setUp();

        loginRequest = new LoginRequest();
        loginRequest.request(bestUser.getUsername(), bestUser.getPassword());

        loginResult = serverProxy.login(loginRequest, "localhost", "8080");

        assertNotNull(loginResult);

        assertNotNull(loginResult.getAuthtoken());
        assertNotNull(loginResult.getPersonID());

        assertEquals(loginResult.getUsername(), bestUser.getUsername());
        assertTrue(loginResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }

    @Test
    public void loginFail() throws Exception {
        setUp();

        loginRequest = new LoginRequest();
        loginRequest.request(worstUser.getUsername(), worstUser.getPassword());

        loginResult = serverProxy.login(loginRequest, "localhost", "8080");

        assertNotNull(loginResult);

        assertEquals(loginResult.getMessage(),"Error: user does not match any existing user.");
        assertFalse(loginResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }
}
