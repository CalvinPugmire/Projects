package com.example.familymapclient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.R;
import com.example.familymapclient.background.VariousTask;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class PersonActivity extends AppCompatActivity {
    Map<String, Person> persons;
    Map<String, Event> events;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Iconify.with(new FontAwesomeModule());

        setDataView();
    }

    private void setDataView () {
        DataCache instance = DataCache.getInstance();

        persons = instance.getPersons();
        Person person = persons.get((String) getIntent().getSerializableExtra("PERSON_ID"));

        events = new TreeMap<>();
        Map<String, Event> eventsi = instance.getEvents();
        String[] eventsj = ((String[]) getIntent().getSerializableExtra("EVENTS"));
        for (int k = 0; k < eventsj.length; k++) {
            if (eventsi.containsKey(eventsj[k])) {
                events.put(eventsj[k],eventsi.get(eventsj[k]));
            }
        }

        VariousTask task = new VariousTask();
        Event[] lifeEvents = task.findLifeEvents(person,events);
        Person[] familyMembers = task.findFamilyMembers(person,persons,eventsi);

        setView(person, lifeEvents, familyMembers);
    }

    private void setView (Person person, Event[] lifeEvents, Person[] familyMembers) {
        TextView firstNameContent = findViewById(R.id.firstNameContent);
        TextView lastNameContent = findViewById(R.id.lastNameContent);
        TextView genderContent = findViewById(R.id.genderContent);
        ExpandableListView expandablePersonView = findViewById(R.id.expandablePersonView);

        firstNameContent.setText(person.getFirstName());
        lastNameContent.setText(person.getLastName());
        if (person.getGender().equals("m")) {
            genderContent.setText(R.string.male); }
        else { genderContent.setText(R.string.female); }

        expandablePersonView.setAdapter(new ExpandableListAdapter(lifeEvents, familyMembers));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int LIFE_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private Event[] lifeEvents;
        private Person[] familyMembers;

        ExpandableListAdapter(Event[] lifeEvents, Person[] familyMembers) {
            this.lifeEvents = lifeEvents;
            this.familyMembers = familyMembers;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_GROUP_POSITION:
                    return lifeEvents.length;
                case FAMILY_GROUP_POSITION:
                    return familyMembers.length;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_GROUP_POSITION:
                    return getString(R.string.lifeEventsTitle);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.familyMembersTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_GROUP_POSITION:
                    return lifeEvents[childPosition];
                case FAMILY_GROUP_POSITION:
                    return familyMembers[childPosition];
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_GROUP_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.familyMembersTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case LIFE_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.life_event_item, parent, false);
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family_member_item, parent, false);
                    initializeFamilyMemberView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        @SuppressLint("SetTextI18n")
        private void initializeFamilyMemberView(View familyMemberItemView, final int childPosition) {
            ImageView familyMemberGenderView = familyMemberItemView.findViewById(R.id.familyMemberGender);
            if (familyMembers[childPosition] != null) {
                if (familyMembers[childPosition].getGender().equals("m")) {
                    familyMemberGenderView.setImageDrawable(new IconDrawable(PersonActivity.this,
                            FontAwesomeIcons.fa_male).colorRes(R.color.blue).actionBarSize());
                } else {
                    familyMemberGenderView.setImageDrawable(new IconDrawable(PersonActivity.this,
                            FontAwesomeIcons.fa_female).colorRes(R.color.magenta).actionBarSize());
                }
            } else {
                familyMemberGenderView.setImageDrawable(new IconDrawable(PersonActivity.this,
                        FontAwesomeIcons.fa_question).colorRes(R.color.black).actionBarSize());
            }

            TextView familyMemberNameView = familyMemberItemView.findViewById(R.id.familyMemberName);
            if (familyMembers[childPosition] != null) {
                familyMemberNameView.setText(familyMembers[childPosition].getFirstName()+" "+ familyMembers[childPosition].getLastName());
            } else {
                familyMemberNameView.setText(R.string.NA);
            }

            TextView familyMemberTitleView = familyMemberItemView.findViewById(R.id.familyMemberTitle);
            if (childPosition == 0) {
                familyMemberTitleView.setText(R.string.father);
            } else if (childPosition == 1) {
                familyMemberTitleView.setText(R.string.mother);
            } else if (childPosition == 2) {
                familyMemberTitleView.setText(R.string.spouse);
            } else {
                familyMemberTitleView.setText(R.string.child);
            }

            familyMemberItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!familyMemberNameView.getText().equals(getString(R.string.NA))) {
                        Intent personi = new Intent(PersonActivity.this, PersonActivity.class);
                        personi.putExtra("PERSON_ID", familyMembers[childPosition].getPersonID());
                        personi.putExtra("EVENTS",events.keySet().toArray(new String[0]));
                        startActivity(personi);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        private void initializeLifeEventView(View lifeEventItemView, final int childPosition) {
            ImageView lifeEventMarkerView = lifeEventItemView.findViewById(R.id.lifeEventMarker);
            lifeEventMarkerView.setImageDrawable(new IconDrawable(PersonActivity.this,
                    FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).actionBarSize());

            TextView lifeEventTitleView = lifeEventItemView.findViewById(R.id.lifeEventTitle);
            lifeEventTitleView.setText(lifeEvents[childPosition].getEventType().toUpperCase());

            TextView lifeEventLocationView = lifeEventItemView.findViewById(R.id.lifeEventLocation);
            lifeEventLocationView.setText(lifeEvents[childPosition].getCity()+", "+ lifeEvents[childPosition].getCountry()+" ");

            TextView lifeEventDateView = lifeEventItemView.findViewById(R.id.lifeEventDate);
            lifeEventDateView.setText("("+ lifeEvents[childPosition].getYear()+")");

            lifeEventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eventi = new Intent(PersonActivity.this, EventActivity.class);
                    eventi.putExtra("EVENT_ID", lifeEvents[childPosition].getEventID());
                    startActivity(eventi);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}