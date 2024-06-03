package edu.byu.cs.tweeter.client.presenter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class PostStatusTests {
    ServerFacade server;

    private MainPresenter.View mockView;
    private MainPresenter mainPresenter;

    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        server = new ServerFacade();

        mockView = Mockito.mock(MainPresenter.View.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                countDownLatch.countDown();
                return null;
            }
        }).when(mockView).displayMessage("Successfully Posted!");

        mainPresenter = new MainPresenter(mockView);

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void postStatus() throws InterruptedException {
        try {
            LoginRequest request = new LoginRequest("@guy1", "password");
            LoginResponse response = server.login(request, "login");
            Assertions.assertTrue(response.isSuccess());
            Cache.getInstance().setCurrUser(response.getUser());
            Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());

            mainPresenter.onStatusPosted("Hello world!");
            awaitCountDownLatch();
            Mockito.verify(mockView).displayMessage("Posting Status...");
            Mockito.verify(mockView).displayMessage("Successfully Posted!");
            Mockito.verify(mockView, Mockito.times(2)).displayMessage(Mockito.any());

            StoryRequest request2 = new StoryRequest(response.getAuthToken(), response.getUser().getAlias(), 10, null);
            StoryResponse response2 = server.getStory(request2, "getfollowerscount");
            Assertions.assertEquals(response.getUser().getAlias(), response2.getStory().get(0).getUser().getAlias());
            Assertions.assertEquals("@Hello world!", response2.getStory().get(0).getPost());
        } catch (IOException | TweeterRemoteException e) {
            e.printStackTrace();
        }
    }
}
