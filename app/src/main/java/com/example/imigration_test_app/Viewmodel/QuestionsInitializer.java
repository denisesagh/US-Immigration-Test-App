package com.example.imigration_test_app.Viewmodel;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.example.imigration_test_app.Model.Questions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import io.realm.Realm;
import io.realm.RealmList;

public class QuestionsInitializer {
    Realm realm;

    //If no questions in DB, init questions by iteration through csv
    public void initializeQuestions(Realm realmInstance, Context context) {

        Log.e("QuestionsInitializer", "QuestionsInitializer called");

        realm = realmInstance;

        realm.executeTransaction(new Realm.Transaction() {
            public void execute(Realm realm) {

                Log.e("QuestionsInitializer", "QuestionsInitializer execute called");
                long questionCount = realm.where(Questions.class).count();
                Log.e("QuestionsInitializer", "Initialized: " + questionCount + " questions");
                if (questionCount == 0) {

                    try {
                        AssetManager assetManager = context.getAssets();
                        InputStream fileStream = assetManager.open("questionsCSV.csv");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
                        String line;
                        reader.readLine(); // Überspringen Sie die Header-Zeile
                        while ((line = reader.readLine()) != null) {
                            RealmList<String> correctAnswers = new RealmList<>();
                            String[] values = line.split(",");
                            String question = values[0];
                            int answerFields = Integer.parseInt(values[2]);
                            int answersNeeded = Integer.parseInt(values[3]);


                            String[] splitAnswers = values[1].split("/");
                            for (String answer : splitAnswers) {
                                correctAnswers.add(answer);
                            }
                            Questions questionToAdd = new Questions();
                            questionToAdd.setQuestion(question);
                            questionToAdd.setCorrect(false);
                            questionToAdd.setInitialized(true);
                            questionToAdd.setAnswerFields(answerFields);
                            questionToAdd.setAnswersNeededToBeCorrect(answersNeeded);
                            questionToAdd.setCorrectAnswers(correctAnswers);

                            realm.copyToRealm(questionToAdd);


                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    Log.e("QuestionsInitializer", "Question 1 initialized");

                    // TODO: 2021-01-18 initialize questions
                } else {
                    Log.e("QuestionsInitializer", "Questions already initialized");

                }

            }
        });

    }

}
