package edu.byu.cs.tweeter.model.net.response;

public class UnfollowResponse extends Response {
    public UnfollowResponse() {
        super(true);
    }

    public UnfollowResponse(boolean success) {
        super(success);
    }
}