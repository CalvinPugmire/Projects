package models;

import java.util.Objects;

/**
 * A person.
 */
public class Person {
    /**
     * Person's ID.
     */
    private String personID;
    /**
     * Associated username.
     */
    private String associatedUsername;
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
     * Creates a person.
     *
     * @param personID person's ID.
     * @param associatedUsername associated username.
     * @param firstName first name.
     * @param lastName last name.
     * @param gender gender.
     * @param fatherID father's ID.
     * @param motherID mother's ID.
     * @param spouseID spouse's ID.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getPersonID(), person.getPersonID()) &&
                Objects.equals(getAssociatedUsername(), person.getAssociatedUsername()) &&
                Objects.equals(getFirstName(), person.getFirstName()) &&
                Objects.equals(getLastName(), person.getLastName()) &&
                Objects.equals(getGender(), person.getGender()) &&
                Objects.equals(getFatherID(), person.getFatherID()) &&
                Objects.equals(getMotherID(), person.getMotherID()) &&
                Objects.equals(getSpouseID(), person.getSpouseID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonID(), getAssociatedUsername(), getFirstName(), getLastName(), getGender(), getFatherID(), getMotherID(), getSpouseID());
    }
}
