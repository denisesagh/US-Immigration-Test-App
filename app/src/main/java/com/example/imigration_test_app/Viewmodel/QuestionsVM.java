package com.example.imigration_test_app.Viewmodel;

import android.util.Log;

import com.example.imigration_test_app.Model.Questions;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;


public class QuestionsVM {

    private Questions question;
    private Realm realm;

    //load a random question
    public void loadRandomQuestion(TestCallback testActivityCallback, Realm realmInstance, String loadmode) {

        realm = realmInstance;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (loadmode.equals("allQuestionsMode")) {
                    RealmResults<Questions> falseQuestions = realm.where(Questions.class).equalTo("correct", false).findAll();

                    if (falseQuestions == null || falseQuestions.size() == 0) {
                        testActivityCallback.setQuestion(null);
                        return;
                    }
                    Random r = new Random(System.nanoTime());
                    int randomIndex = r.nextInt(falseQuestions.size());
                    question = falseQuestions.get(randomIndex);
                    //Log.v("QuestionsVM", "Question: " + question.getQuestionID());
                    if (question == null) {
                        testActivityCallback.setQuestion(null);
                    } else {
                        testActivityCallback.setQuestion(question);
                    }
                } else {
                    Log.e("Loadmode error", "Loadmode not found");
                }
                if (loadmode.equals("testMode")) {
                    RealmResults<Questions> allQuestions = realm.where(Questions.class).findAll();

                    if (allQuestions == null || allQuestions.size() == 0) {
                        Log.e("Questiondb error", "No Questions in DB");
                        return;
                    }
                    Random r = new Random(System.nanoTime());
                    int randomIndex = r.nextInt(allQuestions.size());
                    question = allQuestions.get(randomIndex);
                    //Log.v("QuestionsVM", "Question: " + question.getQuestionID());
                    if (question == null) {
                        testActivityCallback.setQuestion(null);
                    } else {
                        testActivityCallback.setQuestion(question);
                    }

                } else {
                    Log.e("Loadmode error", "Loadmode not found");
                }
            }
        });


    }


    //sets answer correct
    public void setAnswerCorrect(Questions question, Realm realmInstance) {
        String questionId = question.getQuestionID();

        realm = realmInstance;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Questions currentQuestion = realm.where(Questions.class).equalTo("questionID", questionId).findFirst();
                currentQuestion.setCorrect(true);
            }
        });
    }

}

