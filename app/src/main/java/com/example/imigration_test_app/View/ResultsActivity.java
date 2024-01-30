package com.example.imigration_test_app.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.imigration_test_app.Model.CurrentRealmConfig;
import com.example.imigration_test_app.Model.Results;
import com.example.imigration_test_app.R;
import com.example.imigration_test_app.Model.PairQuestionAndResult;
import com.example.imigration_test_app.Viewmodel.ResultsCallback;
import com.example.imigration_test_app.Viewmodel.ResultsVM;

import java.text.SimpleDateFormat;

import io.realm.RealmResults;

public class ResultsActivity extends AppCompatActivity implements ResultsCallback {

    ResultsVM resultsVM = new ResultsVM();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d. MMMM yyyy, HH:mm");

    public void getResults() {
        Log.v("ResultsActivity", "getResults called");
        resultsVM.getTestList(this, CurrentRealmConfig.getInstance().getCurrentRealm());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        getResults();

    }


    @Override
    public void setResults(RealmResults results) {
        try {
            RealmResults<Results> realmResults = results;
            for (Results result : realmResults) {
                createCardViewForTest(result);
            }

        } catch (Exception e) {
            Log.e("ResultsActivity", "Error while parsing results");
        }
    }

    //Initialize the CardView with the results
    private void createCardViewForTest(Results result) {


        Context context = this;

        // CardView
        androidx.cardview.widget.CardView cardView = new androidx.cardview.widget.CardView(context);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(30, 0, 30, 30);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(30);
        cardView.setElevation(20);

        // LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(24, 24, 24, 24);
        cardView.addView(linearLayout);

        // Testnumber TextView
        TextView testNumberTextView = new TextView(context);
        testNumberTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testNumberTextView.setText("Testnumber: " + result.getTestNumber());
        testNumberTextView.setTextSize(30);
        testNumberTextView.setTextColor(Color.BLACK);
        testNumberTextView.setTypeface(null, Typeface.BOLD);
        testNumberTextView.setGravity(Gravity.CENTER);
        linearLayout.addView(testNumberTextView);

        // Date TextView
        TextView testDateTextView = new TextView(context);
        testDateTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        testDateTextView.setText(dateFormat.format(result.getDate()));
        testDateTextView.setTextSize(10);
        testDateTextView.setTextColor(Color.GRAY);
        testDateTextView.setGravity(Gravity.CENTER);
        linearLayout.addView(testDateTextView);

        // Line between date and questions
        View separatorView = new View(context);
        LinearLayout.LayoutParams separatorLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        separatorLayoutParams.setMargins(0, 16, 0, 16); // Optional: Fügt etwas Abstand oberhalb und unterhalb der Linie hinzu
        separatorView.setLayoutParams(separatorLayoutParams);
        separatorView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        linearLayout.addView(separatorView);

        // Green or red dots to show if question was answered correct or not
        for (PairQuestionAndResult questionAndResult : result.getQuestionAndResultsList()) {
            // Question
            TextView questionTextView = new TextView(context);
            questionTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Log.v("Resultsactivity", questionAndResult.getQuestion());
            questionTextView.setText(questionAndResult.getQuestion());
            questionTextView.setTextSize(20);
            questionTextView.setTextColor(Color.BLACK);
            linearLayout.addView(questionTextView);


            // Dots
            ImageView circleImageView = new ImageView(context);
            LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(50, 50);
            imageViewLayoutParams.gravity = Gravity.END;
            circleImageView.setLayoutParams(imageViewLayoutParams);
            circleImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (questionAndResult.getResult()) {
                circleImageView.setImageResource(R.drawable.green_circle);
            } else {
                circleImageView.setImageResource(R.drawable.red_circle);
            }

            linearLayout.addView(circleImageView);
        }

        // Add Cardview to Layout
        LinearLayout outerLinearLayout = findViewById(R.id.resultsLayout);
        outerLinearLayout.addView(cardView);
    }

}
