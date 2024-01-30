package com.example.imigration_test_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imigration_test_app.Model.CurrentRealmConfig;
import com.example.imigration_test_app.Model.User;
import com.example.imigration_test_app.R;
import com.example.imigration_test_app.Viewmodel.LoginCallback;
import com.example.imigration_test_app.Viewmodel.LoginVM;

public class LoginActivity extends AppCompatActivity implements LoginCallback {


    Button loginButton;
    TextView loginUsernameText, loginPasswordText;


    //Oben registration View
    public void openRegistrationView(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    //If login success, the user will be directed to the MenuActivity
    @Override
    public void onLoginSuccess(User currentUser) {
        Looper.prepare();
        Toast.makeText(this, "Your Personal ID" + currentUser.getUserID(), Toast.LENGTH_SHORT).show();
        CurrentRealmConfig.getInstance().setCurrentRealm(currentUser.getUserID());
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("userID", currentUser.getUserID());
        LoginActivity.this.startActivity(intent);
        Looper.loop();

    }

    //If the login failed, the user will get a short message
    @Override
    public void onLoginFailed() {
        Looper.prepare();
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    //Initialize buttons and sets listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        loginButton = findViewById(R.id.loginButton);
        loginUsernameText = findViewById(R.id.login_usernameTextfield);
        loginPasswordText = findViewById(R.id.login_passwordTextfield);

        loginButton.setOnClickListener(
                v -> {
                    LoginVM loginvm = new LoginVM(LoginActivity.this);
                    if (loginUsernameText.getText().toString().trim().length() != 0 & loginPasswordText.getText().toString().trim().length() != 0) {
                        loginvm.userLogin(loginUsernameText.getText(), loginPasswordText.getText(), this);
                    } else {
                        Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


}
