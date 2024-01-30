package com.example.imigration_test_app.Model;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import java.util.UUID;

public class User extends RealmObject {

    @PrimaryKey
    @Required
    private String userID;


    private String username, password;

    public User(String _username, String _password) {

        this.username = _username;
        this.password = _password;
    }

    public User() {
        userID = UUID.randomUUID().toString();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Log.v("Datanaseinfo", String.valueOf(username));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        Log.v("Datanaseinfo", String.valueOf(password));
    }

    public String getUserID() {
        return userID;
    }
}
