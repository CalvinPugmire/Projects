package com.example.familymapclient.background;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Map;
import java.util.TreeMap;

import models.*;
import requests.*;
import results.*;

public class LoginTask implements Runnable {

    private Handler handler;
    private String serverHost;
    private String serverPort;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    public LoginTask (Handler handler, String serverHost, String serverPort, String username, String password) {
        this.handler = handler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.username = username;
        this.password = password;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.gender = null;
    }

    public LoginTask (Handler handler, String serverHost, String serverPort, String username,
                      String password, String firstName, String lastName, String email, String gender) {
        this.handler = handler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    @Override
    public void run() {
        if (firstName == null) {
            login();
        } else {
            register();
        }
    }

    public void login () {
        ServerProxy server = new ServerProxy();

        LoginRequest request = new LoginRequest();
        request.request(username,password);
        LoginResult result = server.login(request, serverHost, serverPort);

        if (result.isSuccess()) {
            User user = logData(result, null, server);

            sendMessage(true, user.getFirstName()+" "+user.getLastName());
        } else {
            sendMessage(false, result.getMessage());
        }
    }

    public void register () {
        ServerProxy server = new ServerProxy();

        RegisterRequest request = new RegisterRequest();
        request.request(username, password, email, firstName, lastName, gender);
        RegisterResult result = server.register(request, serverHost, serverPort);

        if (result.isSuccess()) {
            User user = logData(null, result, server);

            sendMessage(true, user.getFirstName()+" "+user.getLastName());
        } else {
            sendMessage(false, result.getMessage());
        }
    }

    private User logData (LoginResult resultL, RegisterResult resultR, ServerProxy server) {
        DataCache instance = DataCache.getInstance();

        AuthToken authtoken;
        if (resultL != null) {
            authtoken = new AuthToken(resultL.getAuthtoken(), username);
        } else {
            authtoken = new AuthToken(resultR.getAuthtoken(), username);
        }
        instance.setAuthToken(authtoken);

        Map<String,Person> persons = new TreeMap<>();
        PersonResult resultp = server.getPersons(authtoken, serverHost, serverPort);
        for (int i = 0; i < resultp.getData().length; i++) {
            persons.put(resultp.getData()[i].getPersonID(), resultp.getData()[i]);
        }
        instance.setPersons(persons);

        User user;
        if (resultL != null) {
            user = new User(username, password, null, persons.get(resultL.getPersonID()).getFirstName(),
                    persons.get(resultL.getPersonID()).getLastName(), persons.get(resultL.getPersonID()).getGender(),
                    resultL.getPersonID());
        } else {
            user = new User(username, password, email, firstName, lastName, gender, resultR.getPersonID());
        }
        instance.setUser(user);

        Map<String,Event> events = new TreeMap<>();
        EventResult resulte = server.getEvents(authtoken, serverHost, serverPort);
        for (int i = 0; i < resulte.getData().length; i++) {
            events.put(resulte.getData()[i].getEventID(), resulte.getData()[i]);
        }
        instance.setEvents(events);

        instance.setSubsets();

        return user;
    }

    private void sendMessage(boolean resultB, String resultS) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("loginResult", resultB);
        messageBundle.putString("loginMessage", resultS);
        message.setData(messageBundle);

        handler.sendMessage(message);
    }
}
