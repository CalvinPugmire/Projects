package requests;

import models.Event;
import models.Person;
import models.User;

/**
 * A load request.
 */
public class LoadRequest {
    /**
     * Users.
     */
    private User[] users;
    /**
     * Persons.
     */
    private Person[] persons;
    /**
     * Events.
     */
    private Event[] events;

    /**
     * Creates a load request.
     *
     * @param users users.
     * @param persons persons.
     * @param events events.
     */
    public void request(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
