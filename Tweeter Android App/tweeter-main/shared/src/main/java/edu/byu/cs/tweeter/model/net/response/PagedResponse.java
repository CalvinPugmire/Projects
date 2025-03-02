package edu.byu.cs.tweeter.model.net.response;

public class PagedResponse extends Response {

    private final boolean hasMorePages;

    PagedResponse(boolean success, boolean hasMorePages) {
        super(success);
        this.hasMorePages = hasMorePages;
    }

    PagedResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message);
        this.hasMorePages = hasMorePages;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }
}
