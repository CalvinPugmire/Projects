package models;

import java.util.Objects;

/**
 * An event.
 */
public class Event {
    /**
     * Event ID.
     */
    private String eventID;
    /**
     * Associated username.
     */
    private String associatedUsername;
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
     * Creates an event.
     *
     * @param eventID event ID.
     * @param associatedUsername associated username.
     * @param personID person's ID.
     * @param latitude latitude.
     * @param longitude longitude.
     * @param country country.
     * @param city city.
     * @param eventType event type.
     * @param year year.
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(getEventID(), event.getEventID()) &&
                Objects.equals(getAssociatedUsername(), event.getAssociatedUsername()) &&
                Objects.equals(getPersonID(), event.getPersonID()) &&
                getLatitude() == event.getLatitude() &&
                getLongitude() == event.getLongitude() &&
                Objects.equals(getCountry(), event.getCountry()) &&
                Objects.equals(getCity(), event.getCity()) &&
                Objects.equals(getEventType(), event.getEventType()) &&
                getYear() == event.getYear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventID(), getAssociatedUsername(), getPersonID(), getLatitude(), getLongitude(), getCountry(), getCity(), getEventType(), getYear());
    }
}
