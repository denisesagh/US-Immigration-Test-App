package com.example.imigration_test_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.imigration_test_app.R;

import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    //Initialize listener and views to open the sections
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        Button learnAllQuestionsButton = findViewById(R.id.learnAllQuestionsButton);
        Button showResultsButton = findViewById(R.id.showResultsButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button startTestButton = findViewById(R.id.startTestButton);

        startTestButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, TestActivity.class);
                    intent.putExtra("userID", getIntent().getStringExtra("userID"));
                    startActivity(intent);
                }
        );

        learnAllQuestionsButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, AllQuestionsActivity.class);
                    intent.putExtra("userID", getIntent().getStringExtra("userID"));

                    startActivity(intent);
                }
        );
        showResultsButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, ResultsActivity.class);
                    intent.putExtra("userID", getIntent().getStringExtra("userID"));
                    startActivity(intent);
                }
        );
        logoutButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
        );

    }
}
