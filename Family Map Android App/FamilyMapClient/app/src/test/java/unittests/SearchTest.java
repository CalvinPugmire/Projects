package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.familymapclient.background.VariousTask;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class SearchTest {
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
    private Event event8;
    private Map<String,Event> events;

    public void setUp() throws Exception
    {
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
        event8 = new Event("death8", "sheila", "Bob_Goner", 68.9f,
                14.1f, "United States", "Alabama", "death", 2050);

        events = new TreeMap<>();
        events.put(event1.getEventID(),event1);
        events.put(event2.getEventID(),event2);
        events.put(event3.getEventID(),event3);
        events.put(event4.getEventID(),event4);
        events.put(event5.getEventID(),event5);
        events.put(event6.getEventID(),event6);
        events.put(event7.getEventID(),event7);
        events.put(event8.getEventID(),event8);
    }

    @Test
    public void searchPass() throws Exception {
        setUp();

        VariousTask task = new VariousTask();
        List<Person> personResults = task.findPersonResults(persons,"la");
        List<Event> eventResults = task.findEventResults(events,"la");

        assertNotNull(personResults);
        assertEquals(personResults.size(),2);
        assertTrue(personResults.contains(person3));
        assertTrue(personResults.contains(person4));

        assertNotNull(eventResults);
        assertEquals(eventResults.size(),3);
        assertTrue(eventResults.contains(event2));
        assertTrue(eventResults.contains(event5));
        assertTrue(eventResults.contains(event8));

        personResults = task.findPersonResults(persons,"97");
        eventResults = task.findEventResults(events,"97");

        assertNotNull(personResults);
        assertEquals(personResults.size(),0);

        assertNotNull(eventResults);
        assertEquals(eventResults.size(),2);
        assertTrue(eventResults.contains(event1));
        assertTrue(eventResults.contains(event2));
    }

    @Test
    public void searchFail() throws Exception {
        setUp();

        VariousTask task = new VariousTask();
        List<Person> personResults = task.findPersonResults(persons,"absolutenonsense");
        List<Event> eventResults = task.findEventResults(events,"absolutenonsense");

        assertNotNull(personResults);
        assertEquals(personResults.size(),0);

        assertNotNull(eventResults);
        assertEquals(eventResults.size(),0);

        personResults = task.findPersonResults(persons,"d s");
        eventResults = task.findEventResults(events,"d s");

        assertNotNull(personResults);
        assertEquals(personResults.size(),0);

        assertNotNull(eventResults);
        assertEquals(eventResults.size(),3);
        assertTrue(eventResults.contains(event2));
        assertTrue(eventResults.contains(event5));
        assertTrue(eventResults.contains(event8));
    }
}
