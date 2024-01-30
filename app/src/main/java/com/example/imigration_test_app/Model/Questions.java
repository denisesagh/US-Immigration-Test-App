package com.example.imigration_test_app.Model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Questions extends RealmObject {


    @PrimaryKey
    @Required
    private String questionID;

    private String question;
    private RealmList<String> correctAnswers = new RealmList<>();

    private String userAnswer;

    private boolean correct;

    private boolean initialized;

    private int answerFields;

    private int answersNeededToBeCorrect;


    public Questions() {
        questionID = UUID.randomUUID().toString();
    }

    public Questions(String question, RealmList correctAnswers, String userAnswer, boolean correct,
                     boolean initialized, int answerFields, int answersNeededToBeCorrect) {
        this.question = question;
        this.correctAnswers = correctAnswers;
        this.userAnswer = userAnswer;
        this.correct = correct;
        this.answerFields = answerFields;
        this.answersNeededToBeCorrect = answersNeededToBeCorrect;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public RealmList<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(RealmList<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getQuestionID() {
        return questionID;
    }

    public int getAnswerFields() {
        return answerFields;
    }

    public void setAnswerFields(int answerFields) {
        this.answerFields = answerFields;
    }

    public int getAnswersNeededToBeCorrect() {
        return answersNeededToBeCorrect;
    }

    public void setAnswersNeededToBeCorrect(int answersNeededToBeCorrect) {
        this.answersNeededToBeCorrect = answersNeededToBeCorrect;
    }
}
