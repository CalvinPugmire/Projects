package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.familymapclient.background.ServerProxy;

import org.junit.Test;

import models.User;
import requests.RegisterRequest;
import results.RegisterResult;

public class RegisterTest {
    private ServerProxy serverProxy;
    private User bestUser;
    private User worstUser;
    private RegisterRequest registerRequest;
    private RegisterResult registerResult;

    public void setUp() throws Exception
    {
        serverProxy = new ServerProxy();
        serverProxy.clear("localhost", "8080");

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "m", "any1");

        worstUser = new User("human_118", "victoriassecret", "notjoe@gmail.com",
                "Adam", "Jojo", "m", "any2");
    }

    @Test
    public void registerPass() throws Exception {
        setUp();

        registerRequest = new RegisterRequest();
        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());

        registerResult = serverProxy.register(registerRequest, "localhost", "8080");

        assertNotNull(registerResult);

        assertNotNull(registerResult.getAuthtoken());
        assertNotNull(registerResult.getPersonID());

        assertEquals(registerResult.getUsername(),bestUser.getUsername());
        assertTrue(registerResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }

    @Test
    public void registerFail() throws Exception {
        setUp();

        registerRequest = new RegisterRequest();
        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());

        serverProxy.register(registerRequest, "localhost", "8080");

        registerRequest = new RegisterRequest();
        registerRequest.request(worstUser.getUsername(), worstUser.getPassword(), worstUser.getEmail(),
                worstUser.getFirstName(), worstUser.getLastName(), worstUser.getGender());

        registerResult = serverProxy.register(registerRequest, "localhost", "8080");

        assertNotNull(registerResult);

        assertEquals(registerResult.getMessage(), "Error: username already taken by existing user.");
        assertFalse(registerResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }
}
