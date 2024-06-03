package requests;

/**
 * A login request.
 */
public class LoginRequest {
    /**
     * Username.
     */
    private String username;
    /**
     * Password.
     */
    private String password;

    /**
     * Creates a login request.
     *
     * @param username username.
     * @param password password.
     */
    public void request(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

