package edu.byu.cs.tweeter.client.presenter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterTests {
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private MainPresenter spyMainPresenter;

    Status TEST_STATUS = new Status("Hello world!", null, null, null, null);

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        spyMainPresenter = Mockito.spy(new MainPresenter(mockView));

        Mockito.when(spyMainPresenter.getStatusService()).thenReturn(mockStatusService);
    }

    @Test
    public void testPostStatus_postSuccessful() {
        Answer<Void> answer = invocation -> {
            MainPresenter.StatObserver observer = setupMockObserver(invocation);
            verifyParameters(invocation);
            observer.handlePostStatus();
            return null;
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        callPostStatus();

        confirmResults("Successfully Posted!");
    }

    @Test
    public void testPostStatus_postFailedWithMessage() {
        Answer<Void> answer = invocation -> {
            MainPresenter.StatObserver observer = setupMockObserver(invocation);
            verifyParameters(invocation);
            observer.handleFailure("No wifi connection");
            return null;
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        callPostStatus();

        confirmResults("Failed to post status: No wifi connection");
    }

    @Test
    public void testPostStatus_postFailedWithException() {
        Answer<Void> answer = invocation -> {
            MainPresenter.StatObserver observer = setupMockObserver(invocation);
            verifyParameters(invocation);
            observer.handleException(new RuntimeException("Runtime exception has occurred"));
            return null;
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any());
        callPostStatus();

        confirmResults("Failed to post status because of exception: Runtime exception has occurred");
    }

    private MainPresenter.StatObserver setupMockObserver(InvocationOnMock invocation) {
        return invocation.getArgument(1, MainPresenter.StatObserver.class);
    }

    private void callPostStatus() {
        spyMainPresenter.onStatusPosted(TEST_STATUS.getPost());
    }

    private void verifyParameters(InvocationOnMock invocation) {
        Assertions.assertEquals(invocation.getArgument(0, Status.class).getPost(), TEST_STATUS.getPost());
        Assertions.assertEquals(invocation.getArgument(1, MainPresenter.StatObserver.class).getFailMessage(), "Failed to post status: ");
    }

    private void confirmResults(String msg) {
        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage(msg);
        Mockito.verify(mockView, Mockito.times(2)).displayMessage(Mockito.any());
    }
}