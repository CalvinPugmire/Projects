package edu.byu.cs.tweeter.model.net.response;

public class LogoutResponse extends Response {
    public LogoutResponse() {
        super(true);
    }

    public LogoutResponse(boolean success) {
        super(success);
    }
}
