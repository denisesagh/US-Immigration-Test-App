package com.example.imigration_test_app.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imigration_test_app.Model.CurrentRealmConfig;
import com.example.imigration_test_app.Model.Questions;
import com.example.imigration_test_app.R;
import com.example.imigration_test_app.Viewmodel.QuestionsInitializer;
import com.example.imigration_test_app.Viewmodel.QuestionsVM;
import com.example.imigration_test_app.Viewmodel.TestCallback;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;


public class AllQuestionsActivity extends AppCompatActivity implements TestCallback {
    private int inputFieldCounter = 2;
    private Questions currentQuestion;

    private RealmConfiguration currentUserConfig;

    private Realm realm;

    private String currentUserId;

    QuestionsVM questionsVM = new QuestionsVM();


    private Answerhandler answerHandler = new Answerhandler();

    //This method generates a copy of input fields from the original input fields
    public void inputFields() {

        LinearLayout parentLayout = findViewById(R.id.answerFieldLayout);
        EditText originalInputField = findViewById(R.id.answerField1);
        EditText newInputField = new EditText(this);

        newInputField.setLayoutParams(originalInputField.getLayoutParams());
        newInputField.setHint("Answer " + inputFieldCounter);
        newInputField.setTransformationMethod(originalInputField.getTransformationMethod());
        newInputField.setPadding(originalInputField.getPaddingLeft(),
                originalInputField.getPaddingTop(),
                originalInputField.getPaddingRight(),
                originalInputField.getPaddingBottom());
        newInputField.setBackground(originalInputField.getBackground());
        newInputField.setId(inputFieldCounter);
        inputFieldCounter++;
        Log.v("Test", "InputFieldCounter: " + newInputField.getId());

        parentLayout.addView(newInputField);

    }


    //Oncreate sets UserID, sets realm config, initialize one question and sets listener for clickable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_questions_activity);


        currentUserId = getIntent().getStringExtra("userID");

        realm =  CurrentRealmConfig.getInstance().getCurrentRealm();


        initializeQuestions();

        questionsVM = new QuestionsVM();

        loadNextQuestion();

        Button submitAnswerButton = findViewById(R.id.submitAnswerButton);

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndUpdateAnswer();
            }
        });

        TextView skipQuestion = findViewById(R.id.skipQuestion);
        skipQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loadNextQuestion();
            }
        });


    }


    //Initialize Questions
    private void initializeQuestions() {
        QuestionsInitializer questionsInitializer = new QuestionsInitializer();
        questionsInitializer.initializeQuestions(realm, this);
    }

    //Creates a list with the user answers and get list with the correct answers of the current questions
    //and checks them in the answerHandler
    private void checkAndUpdateAnswer() {
        if (currentQuestion == null) {
            return;
        }

        ArrayList<String> userAnswers = new ArrayList<>();


        EditText answerField1 = findViewById(R.id.answerField1);
        String answer1 = answerField1.getText().toString();


        userAnswers.add(answer1);
        for (int i = 2; i < inputFieldCounter; i++) {
            EditText answerField = findViewById(i);
            String answer = answerField.getText().toString();
            userAnswers.add(answer);
        }

        RealmList<String> correctAnswersRealmList = currentQuestion.getCorrectAnswers();

        ArrayList<String> correctAnswers = new ArrayList<>(correctAnswersRealmList);

        if (answerHandler.checkAnswers(userAnswers, correctAnswers, currentQuestion.getAnswersNeededToBeCorrect())) {
            questionsVM.setAnswerCorrect(currentQuestion, realm);
            Toast.makeText(this, "Answer is correct", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<String> correctAnswerList = new ArrayList<String>();
            for (String correctAnswer : currentQuestion.getCorrectAnswers()) {
                correctAnswerList.add(correctAnswer);
            }
            Toast.makeText(this, "Answer was wrong. Right answer: " + correctAnswerList, Toast.LENGTH_LONG).show();
        }

        for (int i = 2; i < inputFieldCounter; i++) {
            EditText answerField = findViewById(i);
            ViewGroup answerFieldParent = (ViewGroup) answerField.getParent();
            answerFieldParent.removeView(answerField);
        }

        answerField1.setText("");
        inputFieldCounter = 2;


        loadNextQuestion();
    }

    //Loads next question
    private void loadNextQuestion() {
        questionsVM.loadRandomQuestion(this, realm, "allQuestionsMode");
    }

    //Sets new question
    @Override
    public void setQuestion(Questions question) {
        currentQuestion = question;

        if (currentQuestion == null) {
            TextView questionField = findViewById(R.id.questionField);
            questionField.setText("You answered all questions");
            Log.v("Test", "No more Questions");
        } else {
            TextView questionField = findViewById(R.id.questionField);
            questionField.setText(currentQuestion.getQuestion());

            if (currentQuestion.getAnswerFields() == 0) {
                Log.v("Test", "No Answerfields");
                return;
            }
            if (currentQuestion.getAnswerFields() == 1) {
                Log.v("Test", "One Answerfield");
                return;
            }


            int numberOfAnswerFields = currentQuestion.getAnswerFields() + 1;
            Log.v("Test", "Number of Answerfields: " + numberOfAnswerFields);

            for (int i = 1; i < numberOfAnswerFields; i++) {
                inputFields();
            }


            //Log.v("Test", "The Questions has been answered correct? " + currentQuestion.isCorrect());
            //Log.v("Test", "Question: " + currentQuestion.getQuestion());
        }
    }

    //Close realm instance onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (realm != null) {
            realm.close();
        }
    }
}