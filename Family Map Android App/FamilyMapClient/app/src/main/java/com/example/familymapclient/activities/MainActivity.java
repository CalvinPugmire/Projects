package com.example.familymapclient.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.familymapclient.background.DataCache;
import com.example.familymapclient.fragments.LoginFragment;
import com.example.familymapclient.fragments.MapFragment;
import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);

        if (fragment == null) {
            LoginFragment loginfragment = new LoginFragment();
            loginfragment.loginListener(this);
            fragment = loginfragment;

            fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, fragment).commit();
        } else {
            if (fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).loginListener(this);
            }
        }
    }

    @Override
    public void onLogin() {
        DataCache instance = DataCache.getInstance();
        instance.setMenuVisible(true);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
    }
}