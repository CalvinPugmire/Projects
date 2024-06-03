package results;

/**
 * A personID result.
 */
public class PersonIDResult {
    /**
     * Associated username.
     */
    private String associatedUsername;
    /**
     * Person's ID.
     */
    private String personID;
    /**
     * First name.
     */
    private String firstName;
    /**
     * Last name.
     */
    private String lastName;
    /**
     * Gender.
     */
    private String gender;
    /**
     * Father's ID.
     */
    private String fatherID;
    /**
     * Mother's ID.
     */
    private String motherID;
    /**
     * Spouse's ID.
     */
    private String spouseID;
    /**
     * Success state.
     */
    private boolean success;
    /**
     * Error message.
     */
    private String message;

    /**
     * Creates a successful personID result.
     *
     * @param associatedUsername associated username.
     * @param personID person's ID.
     * @param firstName first name.
     * @param lastName last name.
     * @param gender gender.
     * @param fatherID father's ID.
     * @param motherID mother's ID.
     * @param spouseID spouse's ID.
     * @param success success state.
     */
    public void result(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, boolean success) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }

    /**
     * Creates an unsuccessful personID result.
     *
     * @param message error message.
     * @param success success state.
     */
    public void result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
