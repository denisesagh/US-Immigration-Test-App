package com.example.imigration_test_app.Viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imigration_test_app.Model.User;

import io.realm.Realm;

public class RegisterVM extends AppCompatActivity {
    private Context context;


    //Check if user exists
    public boolean checkIfUserExists(String _registerUsernameToCheck, Context context) {
        this.context = context;
        Realm.init(this.context);
        Realm realm = Realm.getDefaultInstance();
        User userToCheck = realm.where(User.class).equalTo("username", _registerUsernameToCheck).findFirst();
        return userToCheck == null;

    }

    //saves user
    public void saveUser(String _registerUsernameToSave, String _registerPasswordToSave) {

        //Log.e("RegisterVM", _registerUsernameToSave);
        //Log.e("RegisterVM", _registerPasswordToSave);

        Realm.init(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        try {
                            User userToSave = new User();
                            userToSave.setUsername(_registerUsernameToSave);
                            userToSave.setPassword(_registerPasswordToSave);
                            realm.copyToRealm(userToSave);
                            //Log.v("Realm", _registerUsernameToSave + " " + _registerPasswordToSave + " saved");

                        } catch (Exception e) {
                            //Log.e("Realm", "Error saving user");
                        }


                    }
                });

            }
        }).start();

    }
}

