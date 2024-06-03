package results;

import models.Person;

/**
 * A person result.
 */
public class PersonResult {
    /**
     * Persons.
     */
    private Person[] data;
    /**
     * Success state.
     */
    private boolean success;
    /**
     * Error message.
     */
    private String message;

    /**
     * Creates a successful person result.
     *
     * @param data persons.
     * @param success success state.
     */
    public void result(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Creates an unsuccessful person result.
     *
     * @param message error message.
     * @param success success state.
     */
    public void result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
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
