package com.example.imigration_test_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imigration_test_app.Model.CurrentRealmConfig;
import com.example.imigration_test_app.Model.Questions;
import com.example.imigration_test_app.R;
import com.example.imigration_test_app.Viewmodel.QuestionsInitializer;
import com.example.imigration_test_app.Viewmodel.QuestionsVM;
import com.example.imigration_test_app.Viewmodel.ResultsVM;
import com.example.imigration_test_app.Viewmodel.TestCallback;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;


public class TestActivity extends AppCompatActivity implements TestCallback {

    private int testQuestionsCounter;

    private int inputFieldCounter = 2;
    private Questions currentQuestion;

    private RealmConfiguration currentUserConfig;

    private Realm realm;

    private String currentUserId;

    private TextView questionField;
    private TextView questionsCountText;
    private Button submitAnswerButton;
    private EditText originalInputField;
    QuestionsVM questionsVM = new QuestionsVM();

    private TextView questionHeader;

    private Answerhandler answerHandler = new Answerhandler();
    private ResultsVM resultsVM = new ResultsVM();

    //This method generates a copy of input fields from the original input fields
    public void inputFields() {

        LinearLayout parentLayout = findViewById(R.id.answerFieldLayout);

        EditText newInputField = new EditText(this);

        newInputField.setLayoutParams(originalInputField.getLayoutParams());
        newInputField.setHint("Answer" + inputFieldCounter);
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

    //Initialize Questions
    private void initializeQuestions() {
        QuestionsInitializer questionsInitializer = new QuestionsInitializer();
        questionsInitializer.initializeQuestions(realm, this);
    }

    //Creates a list with the user answers and get list with the correct answers of the current questions,
    //checks them in the answerHandler and save answers in the resultsDB
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

        Log.v("Test", "UserAnswers: " + userAnswers.toString());

        RealmList<String> correctAnswersRealmList = currentQuestion.getCorrectAnswers();

        ArrayList<String> correctAnswers = new ArrayList<>(correctAnswersRealmList);

        //Log.v("Test", "CorrectAnswers: " + correctAnswers);
        //Log.v("Test", "CorrectAnswersRealmList: " + userAnswers);


        if (answerHandler.checkAnswers(userAnswers, correctAnswers, currentQuestion.getAnswersNeededToBeCorrect())) {
            Toast.makeText(this, "Answer is correct", Toast.LENGTH_SHORT).show();
            resultsVM.addQuestionToTestList(currentQuestion.getQuestion(), true);


        } else {
            Toast.makeText(this, "Answer was wrong", Toast.LENGTH_SHORT).show();
            resultsVM.addQuestionToTestList(currentQuestion.getQuestion(), false);
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

    //Loads next question or saves test if all questions are answered
    private void loadNextQuestion() {
        if (testQuestionsCounter > 9) {
            resultsVM.getLastTestNumber(CurrentRealmConfig.getInstance().getCurrentRealm());
            resultsVM.saveTestList(CurrentRealmConfig.getInstance().getCurrentRealm());
            questionHeader.setText("Good job!");
            for (int i = 2; i < inputFieldCounter; i++) {
                EditText answerField = findViewById(i);
                ViewGroup answerFieldParent = (ViewGroup) answerField.getParent();
                answerFieldParent.removeView(answerField);
            }
            originalInputField.setVisibility(View.GONE);
            questionField.setText("You finished the test. View your results in the menu under 'Show Results' ");
            submitAnswerButton.setText("Go to menu");
            questionsCountText.setVisibility(View.GONE);

            submitAnswerButton.setOnClickListener(
                    v -> {
                        Intent intent = new Intent(this, MenuActivity.class);
                        intent.putExtra("userID", getIntent().getStringExtra("userID"));

                        startActivity(intent);
                    }
            );


        } else {
            questionsVM.loadRandomQuestion(this, realm, "testMode");
            testQuestionsCounter++;
            questionsCountText.setText("Question " + testQuestionsCounter + "/10");
        }

    }

    //Sets new question
    @Override
    public void setQuestion(Questions question) {
        currentQuestion = question;

        if (currentQuestion == null) {

            questionField.setText("You answered all questions");
        } else {

            questionField.setText(currentQuestion.getQuestion());

            if (currentQuestion.getAnswerFields() == 0) {
                return;
            }
            if (currentQuestion.getAnswerFields() == 1) {
                return;
            }


            int numberOfAnswerFields = currentQuestion.getAnswerFields() + 1;

            for (int i = 1; i < numberOfAnswerFields; i++) {
                inputFields();
            }


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

    //Oncreate sets UserID, sets realm config, initialize one question and sets listener for clickable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);


        testQuestionsCounter = 0;
        questionField = findViewById(R.id.questionField);

        currentUserId = getIntent().getStringExtra("userID");
        originalInputField = findViewById(R.id.answerField1);
        questionHeader = findViewById(R.id.questionHeaderText);
        questionsCountText = findViewById(R.id.questionsCountText);


        //Log.v("Test", "CurrentUserID: " + currentUserId);

        currentUserConfig = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .name(currentUserId + ".realm")
                .build();

        realm = Realm.getInstance(currentUserConfig);

        initializeQuestions();

        questionsVM = new QuestionsVM();


        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndUpdateAnswer();
            }
        });


        loadNextQuestion();

    }


}
