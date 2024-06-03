package com.example.familymapclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.fragments.MapFragment;
import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_event);

        Iconify.with(new FontAwesomeModule());

        DataCache instance = DataCache.getInstance();
        instance.setMenuVisible(false);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        String eventid = (String) getIntent().getSerializableExtra("EVENT_ID");
        Bundle bundle = new Bundle();
        bundle.putString("EVENT_ID",eventid);
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
    }
}