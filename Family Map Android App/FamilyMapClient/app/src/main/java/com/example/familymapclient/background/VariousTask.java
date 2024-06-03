package com.example.familymapclient.background;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class VariousTask {
    public Person[] findFamilyMembers(Person person, Map<String, Person> persons, Map<String, Event> events) {
        Map<Integer, Person> children = new TreeMap<>();

        if (person.getGender().equals("m")) {
            for (Person personc : persons.values()) {
                if (personc.getFatherID() != null) {
                    if (personc.getFatherID().equals(person.getPersonID())) {
                        int birth = findLifeEvents(personc,events)[0].getYear();
                        children.put(birth,personc);
                    }
                }
            }
        } else {
            for (Person personc : persons.values()) {
                if (personc.getMotherID() != null) {
                    if (personc.getMotherID().equals(person.getPersonID())) {
                        int birth = findLifeEvents(personc,events)[0].getYear();
                        children.put(birth,personc);
                    }
                }
            }
        }

        Person[] familyMembers = new Person[3+children.size()];

        if (person.getFatherID() != null) {
            familyMembers[0] = persons.get(person.getFatherID());
        }
        if (person.getMotherID() != null) {
            familyMembers[1] = persons.get(person.getMotherID());
        }
        if (person.getSpouseID() != null) {
            familyMembers[2] = persons.get(person.getSpouseID());
        }

        int i = 3;
        for (Person personi : children.values()) {
            familyMembers[i] = personi;
            i++;
        }

        return familyMembers;
    }

    public Event[] findLifeEvents(Person person, Map<String, Event> events) {
        Map<String, List<Event>> eventorder = new TreeMap<>();

        for (Event event : events.values()) {
            String evaluator;
            if (event.getPersonID().equals(person.getPersonID())) {
                if (event.getEventType().equalsIgnoreCase("birth")) {
                    evaluator = String.valueOf(event.getYear());
                    if (eventorder.containsKey(evaluator)) {
                        eventorder.get(evaluator).add(event);
                    } else {
                        List<Event> list = new LinkedList<>();
                        list.add(event);
                        eventorder.put(evaluator,list);
                    }
                } else if (event.getEventType().equalsIgnoreCase("death")) {
                    evaluator = event.getYear()+"~~~~~~~~~~";
                    if (eventorder.containsKey(evaluator)) {
                        eventorder.get(evaluator).add(event);
                    } else {
                        List<Event> list = new LinkedList<>();
                        list.add(event);
                        eventorder.put(evaluator,list);
                    }
                } else {
                    evaluator = event.getYear()+event.getEventType().toLowerCase();
                    if (eventorder.containsKey(evaluator)) {
                        eventorder.get(evaluator).add(event);
                    } else {
                        List<Event> list = new LinkedList<>();
                        list.add(event);
                        eventorder.put(evaluator,list);
                    }
                }
            }
        }

        List<Event> lifeEvents = new ArrayList<>();
        for (List<Event> list : eventorder.values()) {
            lifeEvents.addAll(list);
        }

        return lifeEvents.toArray(new Event[0]);
    }

    public Map<String,Event> setEvents () {
        DataCache instance = DataCache.getInstance();
        Map<String,Event> events = new TreeMap<>();

        if (instance.isFathersSideEvents()) {
            events.putAll(instance.getFatherEvents());
        }
        if (instance.isMothersSideEvents()) {
            events.putAll(instance.getMotherEvents());
        }
        if (instance.isMaleEvents()) {
            events.putAll(instance.getMaleEvents());
        }
        if (instance.isFemaleEvents()) {
            events.putAll(instance.getFemaleEvents());
        }
        if (!instance.isFathersSideEvents()) {
            events.keySet().removeAll(instance.getFatherEvents().keySet());
        }
        if (!instance.isMothersSideEvents()) {
            events.keySet().removeAll(instance.getMotherEvents().keySet());
        }
        if (!instance.isMaleEvents()) {
            events.keySet().removeAll(instance.getMaleEvents().keySet());
        }
        if (!instance.isFemaleEvents()) {
            events.keySet().removeAll(instance.getFemaleEvents().keySet());
        }

        return events;
    }

    public List<Person> findPersonResults (Map<String,Person> persons, String query) {
        List<Person> pResults = new LinkedList<>();

        for (Person personi : persons.values()) {
            if (personi.getFirstName().toLowerCase().contains(query) ||
                    personi.getLastName().toLowerCase().contains(query)) {
                pResults.add(personi);
            }
        }

        return pResults;
    }

    public List<Event> findEventResults (Map<String,Event> events, String query) {
        List<Event> eResults = new LinkedList<>();

        for (Event eventi : events.values()) {
            if (eventi.getCountry().toLowerCase().contains(query) ||
                    eventi.getCity().toLowerCase().contains(query) ||
                    eventi.getEventType().toLowerCase().contains(query) ||
                    String.valueOf(eventi.getYear()).toLowerCase().contains(query)) {
                eResults.add(eventi);
            }
        }

        return eResults;
    }
}
