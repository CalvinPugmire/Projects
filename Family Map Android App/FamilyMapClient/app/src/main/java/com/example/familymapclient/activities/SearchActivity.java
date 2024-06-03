package com.example.familymapclient.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.R;
import com.example.familymapclient.background.VariousTask;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Event;
import models.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_RESULT_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_RESULT_ITEM_VIEW_TYPE = 1;
    private Map<String, Person> persons;
    private Map<String, Event> events;

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
        setContentView(R.layout.activity_search);

        Iconify.with(new FontAwesomeModule());

        setDataView();
    }

    private void setDataView () {
        DataCache instance = DataCache.getInstance();

        persons = instance.getPersons();

        events = new TreeMap<>();
        Map<String, Event> eventsi = instance.getEvents();
        String[] eventsj = ((String[]) getIntent().getSerializableExtra("EVENTS"));
        for (int k = 0; k < eventsj.length; k++) {
            if (eventsi.containsKey(eventsj[k])) {
                events.put(eventsj[k],eventsi.get(eventsj[k]));
            }
        }

        List<Person> personResults = new LinkedList<>();
        List<Event> eventResults = new LinkedList<>();

        setView(personResults, eventResults);
    }

    private void setView (List<Person> personResults, List<Event> eventResults) {
        ImageView searchImage = findViewById(R.id.searchImage);
        Drawable searchIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.black).sizeDp(30);
        searchImage.setImageDrawable(searchIcon);

        EditText searchField = findViewById(R.id.searchField);

        ImageView clearImage = findViewById(R.id.clearImage);
        Drawable clearIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_times)
                .colorRes(R.color.black).sizeDp(30);
        clearImage.setImageDrawable(clearIcon);

        RecyclerView recyclerSearchView = findViewById(R.id.recyclerSearchView);
        recyclerSearchView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personResults.clear();
                eventResults.clear();

                if (!searchField.getText().toString().equals("")) {
                    String query = searchField.getText().toString().toLowerCase();

                    VariousTask task = new VariousTask();
                    personResults.addAll(task.findPersonResults(persons,query));
                    eventResults.addAll(task.findEventResults(events,query));

                    recyclerSearchView.setAdapter(new SearchResultAdapter(personResults, eventResults));
                }
            }
        });

        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setText("");
            }
        });
    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
        private List<Person> personResults;
        private List<Event> eventResults;

        SearchResultAdapter(List<Person> personResults, List<Event> eventResults) {
            this.personResults = personResults;
            this.eventResults = eventResults;
        }

        @Override
        public int getItemViewType(int position) {
            return position < personResults.size() ? PERSON_RESULT_ITEM_VIEW_TYPE : EVENT_RESULT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_RESULT_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_result_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_result_item, parent, false);
            }

            return new SearchResultViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
            if(position < personResults.size()) {
                holder.bind(personResults.get(position));
            } else {
                holder.bind(eventResults.get(position - personResults.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personResults.size() + eventResults.size();
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView resultImage;
        private final TextView resultEvent;
        private final TextView resultPerson;

        private final int viewType;
        private Person person;
        private Event event;

        SearchResultViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_RESULT_ITEM_VIEW_TYPE) {
                resultImage = itemView.findViewById(R.id.resultImage);
                resultEvent = null;
                resultPerson = itemView.findViewById(R.id.resultPerson);
            } else {
                resultImage = itemView.findViewById(R.id.resultImage);
                resultEvent = itemView.findViewById(R.id.resultEvent);
                resultPerson = itemView.findViewById(R.id.resultPerson);
            }
        }

        @SuppressLint("SetTextI18n")
        private void bind(Person person) {
            this.person = person;
            if (person.getGender().equals("m")) {
                resultImage.setImageDrawable(new IconDrawable(SearchActivity.this,
                        FontAwesomeIcons.fa_male).colorRes(R.color.blue).actionBarSize());
            } else {
                resultImage.setImageDrawable(new IconDrawable(SearchActivity.this,
                        FontAwesomeIcons.fa_female).colorRes(R.color.magenta).actionBarSize());
            }
            resultPerson.setText(person.getFirstName()+" "+person.getLastName());
        }

        @SuppressLint("SetTextI18n")
        private void bind(Event event) {
            this.event = event;
            resultImage.setImageDrawable(new IconDrawable(SearchActivity.this,
                    FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).actionBarSize());
            resultEvent.setText(event.getEventType().toUpperCase()+": "+event.getCity()+", "+
                    event.getCountry()+" "+event.getYear());
            resultPerson.setText(persons.get(event.getPersonID()).getFirstName()+" "+persons.get(event.getPersonID()).getLastName());
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON_RESULT_ITEM_VIEW_TYPE) {
                Intent personi = new Intent(SearchActivity.this, PersonActivity.class);
                personi.putExtra("PERSON_ID", person.getPersonID());
                personi.putExtra("EVENTS",events.keySet().toArray(new String[0]));
                startActivity(personi);
            } else {
                Intent eventi = new Intent(SearchActivity.this, EventActivity.class);
                eventi.putExtra("EVENT_ID", event.getEventID());
                startActivity(eventi);
            }
        }
    }
}