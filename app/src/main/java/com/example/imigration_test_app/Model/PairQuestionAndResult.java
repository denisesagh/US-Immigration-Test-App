package com.example.imigration_test_app.Model;

import io.realm.RealmObject;

public class PairQuestionAndResult extends RealmObject {
    private String question;
    private boolean result;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }


    public PairQuestionAndResult(String question, boolean result) {
        this.question = question;
        this.result = result;
    }

    public PairQuestionAndResult() {
    }
}
