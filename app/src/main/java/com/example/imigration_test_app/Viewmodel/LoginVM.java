package com.example.imigration_test_app.Viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.imigration_test_app.Model.User;
import io.realm.Realm;

public class LoginVM extends AppCompatActivity {


    private Context context;

    public LoginVM(Context context) {
        this.context = context;
    }


    private User userDatabase;



    //Login the user
    public void userLogin(CharSequence _loginUsername, CharSequence _loginPassword, LoginCallback callback) {

        Realm.init(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                userDatabase = realm.where(User.class).equalTo("username", _loginUsername.toString()).findFirst();
                //Log.e("Login", String.valueOf(userDatabase));
                if (userDatabase != null) {
                    //Log.v("Login", "Checking password");
                    if (userDatabase.getPassword().equals(_loginPassword.toString())) {
                        Log.v("LoginVM", "Login Successful");
                        User currentUser = userDatabase;
                        callback.onLoginSuccess(currentUser);

                    } else {
                        //Log.v("Login", "Login Failed, password incorrect");
                        callback.onLoginFailed();

                    }
                } else {
                    //Log.v("Login", "Login Failed, username not found");
                    callback.onLoginFailed();
                }
                realm.close();
            }
        }).start();

    }


}
