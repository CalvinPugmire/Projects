package results;

/**
 * A login result.
 */
public class LoginResult {
    /**
     * Authentication token.
     */
    private String authtoken;
    /**
     * Username.
     */
    private String username;
    /**
     * Person's ID.
     */
    private String personID;
    /**
     * Success state.
     */
    private boolean success;
    /**
     * Error message.
     */
    private String message;

    /**
     * Creates a successful login result.
     *
     * @param authtoken authentication token.
     * @param username  username.
     * @param personID  person's ID.
     * @param success   success state.
     */
    public void result(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    /**
     * Creates an unsuccessful login result.
     *
     * @param message error message.
     * @param success success state.
     */
    public void result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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