package models;

import java.util.Objects;

/**
 * An authentication token.
 */
public class AuthToken {
    /**
     * Authentication token.
     */
    private String authtoken;
    /**
     * Associated username.
     */
    private String username;

    /**
     * Creates an authentication token.
     *
     * @param authtoken authentication token.
     * @param username associated username.
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken token = (AuthToken) o;
        return Objects.equals(getAuthtoken(), token.getAuthtoken()) &&
                Objects.equals(getUsername(), token.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthtoken(), getUsername());
    }
}
