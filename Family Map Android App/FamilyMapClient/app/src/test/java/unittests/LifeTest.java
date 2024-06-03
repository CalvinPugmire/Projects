package unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.familymapclient.background.VariousTask;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class LifeTest {
    private Person person;
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;
    private Event event5;
    private Event event6;
    private Event event7;
    private Event event8;
    private Map<String,Event> eventsi;
    private Map<String,Event> eventsj;

    public void setUp() throws Exception
    {
        person = new Person("Sheila_Parker", "sheila", "Sheila",
                "Parker", "f", "Davis_Parker", "Beth_White", "Blaine_McGary");

        event1 = new Event("birth1", "sheila", "Sheila_Parker", 35.9f,
                140.1f, "Japan", "Ushiku", "birth", 1970);
        event2 = new Event("birth2", "sheila", "Sheila_Parker", 68.9f,
                14.1f, "United States", "Alabama", "birth", 1970);
        event3 = new Event("highschool3", "sheila", "Sheila_Parker", 56.8f,
                100.5f, "Russia", "Slovenia", "high school", 1985);
        event4 = new Event("college4", "sheila", "Sheila_Parker", 35.9f,
                140.1f, "Japan", "Ushiku", "college", 1990);
        event5 = new Event("marriage5", "sheila", "Sheila_Parker", 68.9f,
                14.1f, "United States", "Alabama", "marriage", 1990);
        event6 = new Event("pregnancy6", "sheila", "Sheila_Parker", 56.8f,
                100.5f, "Russia", "Slovenia", "first pregnancy", 1993);
        event7 = new Event("death7", "sheila", "Sheila_Parker", 56.8f,
                100.5f, "Russia", "Slovenia", "death", 2050);
        event8 = new Event("death8", "sheila", "Sheila_Parker", 68.9f,
                14.1f, "United States", "Alabama", "death", 2050);

        eventsi = new TreeMap<>();
        eventsi.put(event1.getEventID(),event1);
        eventsi.put(event3.getEventID(),event3);
        eventsi.put(event5.getEventID(),event5);
        eventsi.put(event6.getEventID(),event6);
        eventsi.put(event8.getEventID(),event8);

        eventsj = new TreeMap<>();
        eventsj.put(event1.getEventID(),event1);
        eventsj.put(event2.getEventID(),event2);
        eventsj.put(event3.getEventID(),event3);
        eventsj.put(event4.getEventID(),event4);
        eventsj.put(event5.getEventID(),event5);
        eventsj.put(event6.getEventID(),event6);
        eventsj.put(event7.getEventID(),event7);
        eventsj.put(event8.getEventID(),event8);
    }

    @Test
    public void lifePass() throws Exception {
        setUp();

        VariousTask task = new VariousTask();
        Event[] life = task.findLifeEvents(person,eventsi);

        assertNotNull(life);
        assertEquals(life.length,5);

        assertEquals(life[0],event1);
        assertEquals(life[1],event3);
        assertEquals(life[2],event5);
        assertEquals(life[3],event6);
        assertEquals(life[4],event8);
    }

    @Test
    public void lifeFail() throws Exception {
        setUp();

        VariousTask task = new VariousTask();
        Event[] life = task.findLifeEvents(person,eventsj);

        assertNotNull(life);
        assertEquals(life.length,8);

        assertEquals(life[0],event1);
        assertEquals(life[1],event2);
        assertEquals(life[2],event3);
        assertEquals(life[3],event4);
        assertEquals(life[4],event5);
        assertEquals(life[5],event6);
        assertEquals(life[6],event7);
        assertEquals(life[7],event8);
    }
}
