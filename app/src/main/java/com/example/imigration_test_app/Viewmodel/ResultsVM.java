package com.example.imigration_test_app.Viewmodel;

import android.util.Log;

import com.example.imigration_test_app.Model.PairQuestionAndResult;
import com.example.imigration_test_app.Model.Results;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ResultsVM {

    private Results results = new Results();
    private Realm realm;
    private int testNumber;
    RealmList<PairQuestionAndResult> pairQuestionAndResultList = new RealmList<PairQuestionAndResult>();

    //Add questions to List
    public void addQuestionToTestList(String testQuestion, boolean result) {
        PairQuestionAndResult pairQuestionAndResult = new PairQuestionAndResult();
        Log.v("ResultsVM", "Adde " + testQuestion + result + " zu pairQuestionsListe");
        pairQuestionAndResult.setQuestion(testQuestion);
        pairQuestionAndResult.setResult(result);
        pairQuestionAndResultList.add(pairQuestionAndResult);

    }


    //get last test number, to set correct test number next time
    public void getLastTestNumber(Realm realmInstance) {
        realm = realmInstance;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Results lastTestResults = realm.where(Results.class).findAll().last();
                    if (lastTestResults != null) {
                        Log.v("ResultsVM", "found last results");
                        testNumber = lastTestResults.getTestNumber() + 1;

                    } else {
                        Log.v("ResultsVM", "testnumber = 1, no results");
                        testNumber = 1;
                    }
                } catch (Exception e) {
                    testNumber = 1;
                    Log.e("ResultsVM", String.valueOf(e));
                }
            }
        });
    }

    //saves tests
    public void saveTestList(Realm realmInstace) {
        realm = realmInstace;

        Log.v("ResultsVM", "SaveTest");

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {


                try {
                    results.setQuestionAndResultsList(pairQuestionAndResultList);
                    results.setDate(Calendar.getInstance().getTime());
                    results.setTestNumber(testNumber);
                    //Log.v("ResultsVM", "Copy " + results.toString());
                    realm.copyToRealmOrUpdate(results);
                } catch (Exception e) {
                    Log.e("ResultsVM", String.valueOf(e));
                }


            }
        });


    }


    public void getTestList(ResultsCallback resultsCallback, Realm realmInstace) {
        realm = realmInstace;

        Log.v("RealmVM", "gette Liste");
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RealmResults<Results> savedResults = realm.where(Results.class).findAll();
                Log.v("ResultsVM", savedResults.asJSON());
                if (savedResults == null) {
                    resultsCallback.setResults(null);
                } else {
                    resultsCallback.setResults(savedResults);

                }
            }
        });

    }

}
