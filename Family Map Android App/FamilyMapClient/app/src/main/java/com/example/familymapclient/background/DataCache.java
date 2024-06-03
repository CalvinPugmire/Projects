package com.example.familymapclient.background;

import java.util.Map;
import java.util.TreeMap;

import models.*;

public class DataCache {
    private static DataCache instance = new DataCache();

    private User user;
    private AuthToken authtoken;
    private Map<String,Person> persons;
    private Map<String,Person> personsFather;
    private Map<String,Person> personsMother;
    private Map<String,Person> personsMale;
    private Map<String,Person> personsFemale;
    private Map<String,Event> events;
    private Map<String,Event> eventsFather;
    private Map<String,Event> eventsMother;
    private Map<String,Event> eventsMale;
    private Map<String,Event> eventsFemale;

    private Map<String,Float> marks = new TreeMap<>();

    private boolean menuVisible;
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSideEvents;
    private boolean mothersSideEvents;
    private boolean maleEvents;
    private boolean femaleEvents;

    private DataCache() {
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        fathersSideEvents = true;
        mothersSideEvents = true;
        maleEvents = true;
        femaleEvents = true;
    }
    public static DataCache getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }
    public void setUser (User user) {
        this.user = user;
    }

    public AuthToken getAuthToken() {
        return authtoken;
    }
    public void setAuthToken (AuthToken authtoken) {
        this.authtoken = authtoken;
    }

    public Map<String, Person> getPersons() {
        return persons;
    }
    public void setPersons (Map<String,Person> persons) {
        this.persons = persons;
    }

    public Map<String, Event> getEvents() {
        return events;
    }
    public void setEvents (Map<String,Event> events) {
        this.events = events;
    }

    public void setSubsets () {
        setGenderPersons();
        setSidePersons();
        setGenderEvents();
        setSideEvents();
    }

    private void setGenderPersons () {
        personsMale = new TreeMap<>();
        personsFemale = new TreeMap<>();

        for (Person person : persons.values()) {
            if (person.getGender().equals("m")) {
                personsMale.put(person.getPersonID(),person);
            } else {
                personsFemale.put(person.getPersonID(),person);
            }
        }
    }

    private void setSidePersons () {
        personsFather = new TreeMap<>();
        personsMother = new TreeMap<>();

        Person person = persons.get(user.getPersonID());

        Person personf = persons.get(person.getFatherID());
        if (personf != null) {
            personsFather.put(personf.getPersonID(),personf);
            Map<String,Person> persontemp = new TreeMap<>();
            loopSidePersons(personf, persontemp);
            personsFather.putAll(persontemp);
        }

        Person personm = persons.get(person.getMotherID());
        if (personm != null) {
            personsMother.put(personm.getPersonID(),personm);
            Map<String,Person> persontemp = new TreeMap<>();
            loopSidePersons(personm, persontemp);
            personsMother.putAll(persontemp);
        }
    }
    private void loopSidePersons (Person person, Map<String,Person> persontemp) {
        if (person.getFatherID() != null) {
            Person personf = persons.get(person.getFatherID());
            if (personf != null) {
                persontemp.put(personf.getPersonID(),personf);
                loopSidePersons(personf, persontemp);
            }
        }

        if (person.getMotherID() != null) {
            Person personm = persons.get(person.getMotherID());
            if (personm != null) {
                persontemp.put(personm.getPersonID(), personm);
                loopSidePersons(personm, persontemp);
            }
        }
    }

    public Map<String, Event> getMaleEvents() {
        return eventsMale;
    }
    public Map<String, Event> getFemaleEvents() {
        return eventsFemale;
    }
    private void setGenderEvents () {
        eventsMale = new TreeMap<>();
        for (Person person : personsMale.values()) {
            for (Event event : events.values()) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    eventsMale.put(event.getEventID(),event);
                }
            }
        }

        eventsFemale = new TreeMap<>();
        for (Person person : personsFemale.values()) {
            for (Event event : events.values()) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    eventsFemale.put(event.getEventID(),event);
                }
            }
        }
    }

    public Map<String, Event> getFatherEvents() {
        return eventsFather;
    }
    public Map<String, Event> getMotherEvents() {
        return eventsMother;
    }
    private void setSideEvents () {
        eventsFather = new TreeMap<>();
        for (Person person : personsFather.values()) {
            for (Event event : events.values()) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    eventsFather.put(event.getEventID(),event);
                }
            }
        }

        eventsMother = new TreeMap<>();
        for (Person person : personsMother.values()) {
            for (Event event : events.values()) {
                if (event.getPersonID().equals(person.getPersonID())) {
                    eventsMother.put(event.getEventID(),event);
                }
            }
        }
    }

    public Map<String, Float> getMarks() {
        return marks;
    }

    public void setMarks(Map<String, Float> marks) {
        this.marks = marks;
    }

    public boolean isMenuVisible() {
        return menuVisible;
    }
    public void setMenuVisible(boolean menuVisible) {
        this.menuVisible = menuVisible;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }
    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }
    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }
    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }
    public boolean isSpouseLines() {
        return spouseLines;
    }
    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }
    public boolean isFathersSideEvents() {
        return fathersSideEvents;
    }
    public void setFathersSideEvents(boolean fathersSideEvents) {
        this.fathersSideEvents = fathersSideEvents;
    }
    public boolean isMothersSideEvents() {
        return mothersSideEvents;
    }
    public void setMothersSideEvents(boolean mothersSideEvents) {
        this.mothersSideEvents = mothersSideEvents;
    }
    public boolean isMaleEvents() {
        return maleEvents;
    }
    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }
    public boolean isFemaleEvents() {
        return femaleEvents;
    }
    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }
}
