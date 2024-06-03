package com.example.familymapclient.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.activities.PersonActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.activities.SearchActivity;
import com.example.familymapclient.activities.SettingsActivity;
import com.example.familymapclient.background.VariousTask;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private Map<String,Marker> markers;
    private TextView eventdetails;
    private DataCache instance = DataCache.getInstance();
    private Map<String,Person> persons;
    private Person person;
    private Map<String,Event> events;
    private Event event;
    int iter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventdetails = view.findViewById(R.id.eventDetails);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,  MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        search.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search).colorRes(R.color.white).actionBarSize());

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear).colorRes(R.color.white).actionBarSize());

        if (instance.isMenuVisible()) {
            menu.setGroupVisible(0,true);
        } else {
            menu.setGroupVisible(0,false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        String autoeventid = null;
        if (getArguments() != null) {
            autoeventid = getArguments().getString("EVENT_ID");
        }

        persons = instance.getPersons();

        VariousTask task = new VariousTask();
        events = task.setEvents();

        setMarkers();

        GoogleMap.OnMarkerClickListener markerlistener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                event = (Event) marker.getTag();
                person = instance.getPersons().get(event.getPersonID());

                setDetails();

                map.clear();
                setMarkers();
                setLines();
                map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                return true;
            }
        };
        map.setOnMarkerClickListener(markerlistener);

        GoogleMap.OnMapClickListener maplistener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                eventdetails.setCompoundDrawables(null,null,null,null);
                eventdetails.setText("");

                map.clear();
                setMarkers();
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        };
        map.setOnMapClickListener(maplistener);

        View.OnClickListener detaillistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventdetails.getText().equals("")) {
                    Intent personi = new Intent(getActivity(), PersonActivity.class);
                    personi.putExtra("PERSON_ID",person.getPersonID());
                    personi.putExtra("EVENTS",events.keySet().toArray(new String[0]));
                    startActivity(personi);
                }
            }
        };
        eventdetails.setOnClickListener(detaillistener);

        if (autoeventid != null) {
            Marker marker = markers.get(autoeventid);
            markerlistener.onMarkerClick(marker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.clear();
            onMapLoaded();

            if (event != null) {
                if (events.containsKey(event.getEventID())) {
                    setLines();
                    setDetails();
                } else {
                    eventdetails.setCompoundDrawables(null,null,null,null);
                    eventdetails.setText("");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.search:
                Intent search = new Intent(getActivity(), SearchActivity.class);
                search.putExtra("EVENTS",events.keySet().toArray(new String[0]));
                startActivity(search);
                return true;
            case R.id.settings:
                Intent settings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDetails () {
        Drawable genderIcon;
        if (person.getGender().equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.blue).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.magenta).sizeDp(40);
        }
        eventdetails.setCompoundDrawables(genderIcon,null,null,null);

        eventdetails.setText(person.getFirstName()+" "+person.getLastName()+"\n"+ event.getEventType().toUpperCase()+
                ": "+event.getCity()+", "+event.getCountry()+ " ("+event.getYear()+")");
    }

    private void setMarkers () {
        markers = new TreeMap<>();

        float[] colors = {BitmapDescriptorFactory.HUE_RED,BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_YELLOW,BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_CYAN,BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_VIOLET,
                BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_ROSE};

        Map<String,Float> marks = instance.getMarks();

        for (Event eventi : events.values()) {
            if (!marks.containsKey(eventi.getEventType().toLowerCase())) {
                marks.put(eventi.getEventType().toLowerCase(), colors[iter]);
                if (iter < colors.length-1) {
                    iter++;
                } else {
                    iter = 0;
                }
            }

            Marker marker = map.addMarker(new MarkerOptions().
                    position(new LatLng(eventi.getLatitude(), eventi.getLongitude())).
                    icon(BitmapDescriptorFactory.defaultMarker(marks.get(eventi.getEventType().toLowerCase()))));
            marker.setTag(eventi);

            markers.put(eventi.getEventID(),marker);
        }

        instance.setMarks(marks);
    }

    private void setLines () {
        if (instance.isLifeStoryLines()) {
            storyLine();
        }
        if (instance.isFamilyTreeLines()) {
            treeLines();
        }
        if (instance.isSpouseLines()) {
            spouseLine();
        }
    }

    private void spouseLine () {
        Person personsp = null;
        if (person.getSpouseID() != null) {
            personsp = persons.get(person.getSpouseID());
            if (personsp != null) {
                Event spousebirth = findBirthEarliest(personsp);
                if (spousebirth != null) {
                    drawLine(event, spousebirth, Color.WHITE, 20);
                }
            }
        }
    }

    private void treeLines () {
        int width = 20;

        if (person.getFatherID() != null) {
            Person personf = persons.get(person.getFatherID());
            if (personf != null) {
                Event fatherbirth = findBirthEarliest(personf);
                if (fatherbirth != null) {
                    drawLine(event, fatherbirth, Color.GRAY, width);
                    loopTreeLines(personf, fatherbirth, width);
                }
            }
        }

        if (person.getMotherID() != null) {
            Person personm = persons.get(person.getMotherID());
            if (personm != null) {
                Event motherbirth = findBirthEarliest(personm);
                if (motherbirth != null) {
                    drawLine(event, motherbirth, Color.GRAY, width);
                    loopTreeLines(personm, motherbirth, width);
                }
            }
        }
    }

    private void loopTreeLines (Person personi, Event eventi, int width) {
        width = width/2;

        if (personi.getFatherID() != null) {
            Person personf = persons.get(personi.getFatherID());
            if (personf != null) {
                Event fatherbirth = findBirthEarliest(personf);
                if (fatherbirth != null) {
                    drawLine(eventi, fatherbirth, Color.GRAY, width);
                    loopTreeLines(personf, fatherbirth, width);
                }
            }
        }

        if (personi.getMotherID() != null) {
            Person personm = persons.get(personi.getMotherID());
            if (personm != null) {
                Event motherbirth = findBirthEarliest(personm);
                if (motherbirth != null) {
                    drawLine(eventi, motherbirth, Color.GRAY, width);
                    loopTreeLines(personm, motherbirth, width);
                }
            }
        }
    }

    private void storyLine () {
        VariousTask task = new VariousTask();

        Event[] lifeEvents = task.findLifeEvents(person,events);

        Event previous = null;
        for (int i = 0; i < lifeEvents.length; i++) {
            if (i > 0) {
                drawLine(previous, lifeEvents[i], Color.BLACK, 20);
            }
            previous = lifeEvents[i];
        }
    }

    private Event findBirthEarliest (Person personi) {
        Event earliest = null;
        for (Event event : events.values()) {
            if (event.getPersonID().equals(personi.getPersonID())) {
                if (earliest == null) {
                    earliest = event;
                }
                else if (earliest.getYear() > event.getYear()) {
                    earliest = event;
                }
                else if (earliest.getYear() == event.getYear()) {
                    if (event.getEventType().equalsIgnoreCase(getString(R.string.birth))) {
                        earliest = event;
                    }
                }
            }
        }
        return earliest;
    }

    private void drawLine(Event startEvent, Event endEvent, int color, float width) {
        // Create start and end points for the line
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        // Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);
        Polyline line = map.addPolyline(options);
    }
}

