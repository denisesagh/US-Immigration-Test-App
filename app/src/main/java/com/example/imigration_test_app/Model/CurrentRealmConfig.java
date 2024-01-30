package com.example.imigration_test_app.Model;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CurrentRealmConfig {
    private RealmConfiguration currentUserConfig;
    private static CurrentRealmConfig instance;

    private CurrentRealmConfig() {
    }

    public static CurrentRealmConfig getInstance() {
        if (instance == null) {
            instance = new CurrentRealmConfig();
        }
        return instance;
    }

    public void setCurrentRealm(String currentUserId) {
        currentUserConfig = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .name(currentUserId + ".realm")
                .build();


    }

    public Realm getCurrentRealm() {
        return (Realm.getInstance(currentUserConfig));
    }
}
