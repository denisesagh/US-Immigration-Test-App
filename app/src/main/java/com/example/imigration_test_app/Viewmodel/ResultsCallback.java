package com.example.imigration_test_app.Viewmodel;


import com.example.imigration_test_app.Model.Results;

import io.realm.RealmResults;

public interface ResultsCallback {
    void setResults(RealmResults<Results> results);
}
