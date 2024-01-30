package com.example.imigration_test_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imigration_test_app.R;
import com.example.imigration_test_app.Viewmodel.RegisterVM;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    TextView registerUsernameTextview, registerPasswordTextview, registerPasswordRepeatTextview;
    String registerUsernameText, registerPasswordText, registerPasswordRepeatText;

    public void registerbutton_clicked(View v) {
        Log.e("E", "Registergeht");
    }

    //Checks if passwords are equal
    public boolean passwordRepeatCheck(String _register_passwordToCheck, String _register_repeatedPasswordToCheck) {
        if (_register_passwordToCheck.equals(_register_repeatedPasswordToCheck)) {
            return true;
        }
        return false;
    }


    //Initialize listener and text-fields
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        registerButton = findViewById(R.id.registerButton);
        registerUsernameTextview = findViewById(R.id.registration_usernameTextfield);
        registerPasswordTextview = findViewById(R.id.registration_passwordTextfield);
        registerPasswordRepeatTextview = findViewById(R.id.registration_passwordRepeatTextfield);


        //If clicked, first it wil check if passwords are the same, then it will check if user already
        //exists, if not, user will be added and again checked if exists to verify it
        registerButton.setOnClickListener(
                v -> {
                    registerUsernameText = registerUsernameTextview.getText().toString();
                    registerPasswordText = registerPasswordTextview.getText().toString();
                    registerPasswordRepeatText = registerPasswordRepeatTextview.getText().toString();
                    RegisterVM registervm = new RegisterVM();

                    //Log.e("Registerinfo-Username", registerUsernameText);
                    //Log.e("Registerinfo-Passwort", registerPasswordText);
                    //Log.e("Registerinfo-Passwortwiederholung", registerPasswordRepeatText);

                    boolean passwordSame = passwordRepeatCheck(registerPasswordText, registerPasswordRepeatText);
                    //Log.v("Registerinfo-IsPasswordSame?", String.valueOf(passwordSame));


                    if (!passwordSame) {
                        Toast.makeText(this, "The passwords are not the same", Toast.LENGTH_SHORT).show();
                    }

                    if (registerUsernameText.trim().length() != 0 & registerPasswordText.trim().length() != 0
                            & passwordSame) {

                        if (registervm.checkIfUserExists(registerUsernameText, this)) {
                            registervm.saveUser(registerUsernameText, registerPasswordText);
                            if (registervm.checkIfUserExists(registerUsernameText, this)) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                                Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Registration failed, try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("Register", "User already exists");
                            Toast.makeText(this, "The username is already taken", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }


}

