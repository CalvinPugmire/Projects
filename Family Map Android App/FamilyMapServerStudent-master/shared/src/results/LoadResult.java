package results;

/**
 * A load result.
 */
public class LoadResult {
    /**
     * Output message.
     */
    private String message;
    /**
     * Success state.
     */
    private boolean success;

    /**
     * Creates a load result.
     *
     * @param message output message.
     * @param success success state.
     */
    public void result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
