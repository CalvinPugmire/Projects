package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeTests {
    ServerFacade server;

    @BeforeEach
    public void setup() {
        server = new ServerFacade();
    }

    @Test
    public void register() {
        RegisterRequest request = new RegisterRequest("Jake", "Dustin", "@jakeydus", "password1234", "image");
        try {
            RegisterResponse response = server.register(request, "register");
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals("super_secret_token", response.getAuthToken().getToken());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFollowers() {
        FollowersRequest request = new FollowersRequest(new AuthToken(), "@isabel", 5, "@elliott");
        try {
            FollowersResponse response = server.getFollowers(request, "getfollowers");
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(5, response.getFollowers().size());
            Assertions.assertEquals("@elizabeth", response.getFollowers().get(0).getAlias());
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFollowerCount() {
        FollowersCountRequest request = new FollowersCountRequest(new AuthToken(), new User("allen", "bobert", "imageURL"));
        try {
            FollowersCountResponse response = server.getFollowersCount(request, "getfollowerscount");
            Assertions.assertEquals(20, response.getFollowersCount());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TweeterRemoteException e) {
            e.printStackTrace();
        }
    }
}
