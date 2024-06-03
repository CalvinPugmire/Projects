package com.example.familymapclient.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.familymapclient.background.LoginTask;
import com.example.familymapclient.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {
    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private CheckBox male;
    private CheckBox female;
    private Button loginButton;
    private Button registerButton;

    private Listener listener;

    public interface Listener {
        void onLogin();
    }

    public void loginListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);
        username = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailAddressField);
        male = view.findViewById(R.id.mButton);
        female = view.findViewById(R.id.fButton);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        serverHost.addTextChangedListener(watcher);
        serverPort.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        firstName.addTextChangedListener(watcher);
        lastName.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setChecked(false);
                updateButtonEnable();
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setChecked(false);
                updateButtonEnable();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serverHost.getText().toString().equals("10.0.2.2") && serverPort.getText().toString().equals("8080")) {
                    Handler handler = createHandler();

                    LoginTask login = new LoginTask(handler, serverHost.getText().toString(), serverPort.getText().toString(),
                            username.getText().toString(), password.getText().toString());

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(login);
                } else {
                    Toast.makeText(getActivity(), R.string.incorrectServer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender;
                if (male.isChecked()) {gender = "m";} else {gender = "f";}

                if (serverHost.getText().toString().equals("10.0.2.2") && serverPort.getText().toString().equals("8080")) {
                    Handler handler = createHandler();

                    LoginTask login = new LoginTask(handler, serverHost.getText().toString(), serverPort.getText().toString(),
                            username.getText().toString(), password.getText().toString(), firstName.getText().toString(),
                            lastName.getText().toString(), email.getText().toString(), gender);

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(login);
                } else {
                    Toast.makeText(getActivity(), R.string.incorrectServer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void updateButtonEnable () {
        if (!serverHost.getText().toString().equals("") && !serverPort.getText().toString().equals("") &&
                !username.getText().toString().equals("") && !password.getText().toString().equals("")) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }

        if (!serverHost.getText().toString().equals("") && !serverPort.getText().toString().equals("") &&
                !username.getText().toString().equals("") && !password.getText().toString().equals("") &&
                !firstName.getText().toString().equals("") && !lastName.getText().toString().equals("") &&
                !email.getText().toString().equals("") && (male.isChecked() || female.isChecked())) {
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }

    private Handler createHandler () {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Bundle bundle = message.getData();
                boolean resultB = bundle.getBoolean("loginResult",false);
                String resultS = bundle.getString("loginMessage",null);

                Toast.makeText(getActivity(), resultS, Toast.LENGTH_SHORT).show();

                if (resultB && listener != null) {
                    listener.onLogin();
                }
            }
        };

        return handler;
    }
}