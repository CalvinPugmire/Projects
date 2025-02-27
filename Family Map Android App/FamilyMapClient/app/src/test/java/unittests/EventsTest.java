package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.background.VariousTask;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;
import models.User;

public class EventsTest {
    private User user;
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;
    private Person person5;
    private Person person6;
    private Person person7;
    private Person person8;
    private Map<String,Person> persons;
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;
    private Event event5;
    private Event event6;
    private Event event7;
    private Map<String,Event> events;

    public void setUp() throws Exception
    {
        user = new User("sheila", "parker", "sheila@parker.com", "Sheila",
                "Parker", "f", "Sheila_Parker");

        person1 = new Person("Davis_Parker", "sheila", "Davis",
                "Parker", "m", null, null, "Beth_White");
        person2 = new Person("Beth_White", "sheila", "Beth",
                "White", "f", null, null, "Davis_Parker");
        person3 = new Person("Sheila_Parker", "sheila", "Sheila",
                "Parker", "f", "Davis_Parker", "Beth_White", "Blaine_McGary");
        person4 = new Person("Blaine_McGary", "sheila", "Blaine",
                "McGary", "m", null, null, "Sheila_Parker");
        person5 = new Person("Ken_McGary", "sheila", "Ken",
                "McGary", "m", "Blaine_McGary", "Sheila_Parker", null);
        person6 = new Person("Kess_McGary", "sheila", "Kess",
                "McGary", "f", "Blaine_McGary", "Sheila_Parker", null);
        person7 = new Person("Frank_McGary", "sheila", "Frank",
                "McGary", "m", "Blaine_McGary", "Sheila_Parker", null);
        person8 = new Person("Bob_Goner", "sheila", "Bob",
                "Goner", "m", null, null, null);

        persons = new TreeMap<>();
        persons.put(person1.getPersonID(),person1);
        persons.put(person2.getPersonID(),person2);
        persons.put(person3.getPersonID(),person3);
        persons.put(person4.getPersonID(),person4);
        persons.put(person5.getPersonID(),person5);
        persons.put(person6.getPersonID(),person6);
        persons.put(person7.getPersonID(),person7);
        persons.put(person8.getPersonID(),person8);

        event1 = new Event("birth1", "sheila", "Davis_Parker", 35.9f,
                140.1f, "Japan", "Ushiku", "birth", 1970);
        event2 = new Event("birth2", "sheila", "Beth_White", 68.9f,
                14.1f, "United States", "Alabama", "birth", 1973);
        event3 = new Event("birth3", "sheila", "Sheila_Parker", 56.8f,
                100.5f, "Russia", "Slovenia", "birth", 2000);
        event4 = new Event("walk4", "sheila", "Blaine_McGary", 35.9f,
                140.1f, "Japan", "Ushiku", "walking", 1995);
        event5 = new Event("christening5", "sheila", "Ken_McGary", 68.9f,
                14.1f, "United States", "Alabama", "christening", 2020);
        event6 = new Event("birth6", "sheila", "Kess_McGary", 56.8f,
                100.5f, "Russia", "Slovenia", "birth", 2025);
        event7 = new Event("2ndbday7", "sheila", "Frank_McGary", 56.8f,
                100.5f, "Russia", "Slovenia", "2nd birthday", 2023);

        events = new TreeMap<>();
        events.put(event1.getEventID(),event1);
        events.put(event2.getEventID(),event2);
        events.put(event3.getEventID(),event3);
        events.put(event4.getEventID(),event4);
        events.put(event5.getEventID(),event5);
        events.put(event6.getEventID(),event6);
        events.put(event7.getEventID(),event7);

    }

    @Test
    public void eventsPass() throws Exception {
        setUp();

        DataCache instance = DataCache.getInstance();
        instance.setUser(user);
        instance.setPersons(persons);
        instance.setEvents(events);
        instance.setSubsets();

        VariousTask task = new VariousTask();
        Map<String,Event> eventsi;

        eventsi = task.setEvents();
        assertEquals(eventsi,events);

        instance.setFathersSideEvents(false);
        eventsi = task.setEvents();
        events.remove(event1.getEventID());
        assertEquals(eventsi,events);

        instance.setFathersSideEvents(true);
        instance.setMothersSideEvents(false);
        eventsi = task.setEvents();
        events.remove(event2.getEventID());
        events.put(event1.getEventID(),event1);
        assertEquals(eventsi,events);

        instance.setMothersSideEvents(true);
        instance.setMaleEvents(false);
        eventsi = task.setEvents();
        events.remove(event1.getEventID());
        events.remove(event4.getEventID());
        events.remove(event5.getEventID());
        events.remove(event7.getEventID());
        events.put(event2.getEventID(),event2);
        assertEquals(eventsi,events);

        instance.setMaleEvents(true);
        instance.setFemaleEvents(false);
        eventsi = task.setEvents();
        events.remove(event2.getEventID());
        events.remove(event3.getEventID());
        events.remove(event6.getEventID());
        events.put(event1.getEventID(),event1);
        events.put(event4.getEventID(),event4);
        events.put(event5.getEventID(),event5);
        events.put(event7.getEventID(),event7);
        assertEquals(eventsi,events);

        instance.setFemaleEvents(true);
    }

    @Test
    public void eventsFail() throws Exception {
        setUp();

        DataCache instance = DataCache.getInstance();
        instance.setUser(user);
        instance.setPersons(persons);
        instance.setEvents(events);
        instance.setSubsets();

        VariousTask task = new VariousTask();
        Map<String,Event> eventsi;
        Map<String,Event> eventsj = new TreeMap<>();

        instance.setMaleEvents(false);
        instance.setFemaleEvents(false);
        eventsi = task.setEvents();
        assertEquals(eventsi,eventsj);

        instance.setMaleEvents(true);
        instance.setFemaleEvents(true);
    }
}
