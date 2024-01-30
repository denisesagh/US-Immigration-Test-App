package com.example.imigration_test_app.View;


import android.util.Log;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;


public class Answerhandler {
    //Takes two lists and compare them. If one string equals to one string in the correctAnswers
    //numCorrect++, if numCorrect is the answersNeededToBeCorrect, the question is answered
    //correct
    public boolean checkAnswers(ArrayList<String> answers, ArrayList<String> correctAnswers, int answersNeededToBeCorrect) {
        int numCorrect = 0;
        for (String answer : answers) {
            for (String correctAnswer : correctAnswers) {
                double similarity = compareStrings(answer, correctAnswer);
                if (similarity > 0.8) {
                    numCorrect++;
                    correctAnswers.remove(correctAnswer);
                    break;
                }
            }
        }
        Log.v("Answerhandler", "Number of correct answers: " + numCorrect);
        return numCorrect >= answersNeededToBeCorrect;
    }

    public double compareStrings(String stringA, String stringB) {
        return StringUtils.getJaroWinklerDistance(stringA, stringB);
    }
}
