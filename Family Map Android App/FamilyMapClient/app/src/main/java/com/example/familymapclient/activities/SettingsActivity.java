package com.example.familymapclient.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class SettingsActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Iconify.with(new FontAwesomeModule());

        setView();
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private void setView () {
        Switch lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        Switch spouseSwitch = findViewById(R.id.spouseSwitch);
        Switch fatherSwitch = findViewById(R.id.fatherSwitch);
        Switch motherSwitch = findViewById(R.id.motherSwitch);
        Switch maleSwitch = findViewById(R.id.maleSwitch);
        Switch femaleSwitch = findViewById(R.id.femaleSwitch);
        View logoutButton = findViewById(R.id.logoutButton);

        DataCache instance = DataCache.getInstance();

        lifeStorySwitch.setChecked(instance.isLifeStoryLines());
        familyTreeSwitch.setChecked(instance.isFamilyTreeLines());
        spouseSwitch.setChecked(instance.isSpouseLines());
        fatherSwitch.setChecked(instance.isFathersSideEvents());
        motherSwitch.setChecked(instance.isMothersSideEvents());
        maleSwitch.setChecked(instance.isMaleEvents());
        femaleSwitch.setChecked(instance.isFemaleEvents());

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setLifeStoryLines(isChecked);
            }
        });
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setFamilyTreeLines(isChecked);
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setSpouseLines(isChecked);
            }
        });
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setFathersSideEvents(isChecked);
            }
        });
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setMothersSideEvents(isChecked);
            }
        });
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setMaleEvents(isChecked);
            }
        });
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                instance.setFemaleEvents(isChecked);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutReset();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void logOutReset() {
        DataCache instance = DataCache.getInstance();

        instance.setLifeStoryLines(true);
        instance.setFamilyTreeLines(true);
        instance.setSpouseLines(true);
        instance.setFathersSideEvents(true);
        instance.setMothersSideEvents(true);
        instance.setMaleEvents(true);
        instance.setFemaleEvents(true);
    }
}