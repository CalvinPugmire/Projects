package results;

import models.Event;

/**
 * A event result.
 */
public class EventResult {
    /**
     * Events.
     */
    private Event[] data;
    /**
     * Success state.
     */
    private boolean success;
    /**
     * Error message.
     */
    private String message;

    /**
     * Creates a successful event result.
     *
     * @param data events.
     * @param success success state.
     */
    public void result(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Creates an unsuccessful event result.
     *
     * @param message error message.
     * @param success success state.
     */
    public void result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
