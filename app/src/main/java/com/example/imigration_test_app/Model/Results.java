package com.example.imigration_test_app.Model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Results extends RealmObject {

    private RealmList<PairQuestionAndResult> questionAndResultsList;


    @PrimaryKey
    private int testNumber;
    private Date date;

    public Results() {

    }

    public Results(RealmList<PairQuestionAndResult> questionAndResultsList, int testNumber, Date date) {
        this.questionAndResultsList = questionAndResultsList;
        this.testNumber = testNumber;
        this.date = date;
    }

    public RealmList<PairQuestionAndResult> getQuestionAndResultsList() {
        return questionAndResultsList;
    }

    public void setQuestionAndResultsList(RealmList<PairQuestionAndResult> questionAndResultsList) {
        this.questionAndResultsList = questionAndResultsList;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
