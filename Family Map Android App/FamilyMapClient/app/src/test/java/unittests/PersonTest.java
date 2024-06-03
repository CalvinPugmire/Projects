package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.familymapclient.background.ServerProxy;

import org.junit.Test;

import models.AuthToken;
import models.User;
import requests.RegisterRequest;
import results.PersonResult;
import results.RegisterResult;

public class PersonTest {
    private ServerProxy serverProxy;
    private User bestUser;
    private AuthToken bestToken;
    private AuthToken worstToken;

    public void setUp() throws Exception
    {
        serverProxy = new ServerProxy();
        serverProxy.clear("localhost", "8080");

        bestUser = new User("human_118", "secretstuff", "joemama@gmail.com",
                "Joe", "Mama", "m", "any1");

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.request(bestUser.getUsername(), bestUser.getPassword(), bestUser.getEmail(),
                bestUser.getFirstName(), bestUser.getLastName(), bestUser.getGender());

        RegisterResult registerResult = serverProxy.register(registerRequest, "localhost", "8080");

        bestToken = new AuthToken(registerResult.getAuthtoken(), bestUser.getUsername());
        worstToken = new AuthToken("~~~~~~~~~~", "the_imposter");
    }

    @Test
    public void personPass() throws Exception {
        setUp();

        PersonResult personResult = serverProxy.getPersons(bestToken, "localhost", "8080");

        assertNotNull(personResult);

        assertEquals(personResult.getData().length, 31);
        assertTrue(personResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }

    @Test
    public void personFail() throws Exception {
        setUp();

        PersonResult personResult = serverProxy.getPersons(worstToken, "localhost", "8080");

        assertNotNull(personResult);

        assertEquals(personResult.getMessage(), "Error: invalid authtoken.");
        assertFalse(personResult.isSuccess());

        serverProxy.clear("localhost", "8080");
    }
}
