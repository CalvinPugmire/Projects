package results;

/**
 * A eventID result.
 */
public class EventIDResult {
    /**
     * Associated username.
     */
    private String associatedUsername;
    /**
     * Event ID.
     */
    private String eventID;
    /**
     * Person's ID.
     */
    private String personID;
    /**
     * Latitude.
     */
    private float latitude;
    /**
     * Longitude.
     */
    private float longitude;
    /**
     * Country.
     */
    private String country;
    /**
     * City.
     */
    private String city;
    /**
     * Event type.
     */
    private String eventType;
    /**
     * Year.
     */
    private int year;
    /**
     * Success state.
     */
    private boolean success;
    /**
     * Error message.
     */
    private String message;

    /**
     * Creates a successful eventID result.
     *
     * @param associatedUsername associated username.
     * @param eventID event ID.
     * @param personID person's ID.
     * @param latitude latitude.
     * @param longitude longitude.
     * @param country country.
     * @param city city.
     * @param eventType event type.
     * @param year year.
     * @param success success state.
     */
    public void result(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    /**
     * Creates an unsuccessful eventID result.
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

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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