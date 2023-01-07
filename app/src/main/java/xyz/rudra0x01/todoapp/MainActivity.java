package xyz.rudra0x01.todoapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import xyz.rudra0x01.todoapp.database.databaseConnect;
import xyz.rudra0x01.todoapp.session.LoginPreferences;

public class MainActivity extends AppCompatActivity {

    TextInputLayout emailTxt, passwdTxt;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailTxt = findViewById(R.id.emailTxt);
        passwdTxt = findViewById(R.id.passwdTxt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        databaseConnect dbHelper = new databaseConnect(this);
        LoginPreferences loginPreferences = new LoginPreferences(getApplicationContext());

        // TODO: check session if the user logged in
        if (loginPreferences.isLoggedIn()) {
            Intent dashboardActivity = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(dashboardActivity);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Check user if valid
                if (dbHelper.checkLogin(emailTxt.getEditText().getText().toString(), passwdTxt.getEditText().getText().toString())) {


                    // TODO: add sharedpref for future use to check if the session is expired
                    long create_expires_unix_time = System.currentTimeMillis() + 3600 * 1000;
                    loginPreferences.setUsername(emailTxt.getEditText().getText().toString());
                    loginPreferences.setExpirationTime(create_expires_unix_time); // expires in 1 hour

                    // TODO: send username to dashboard activity
                    String username = emailTxt.getEditText().getText().toString();

                    Intent dashboardActivity = new Intent(getApplicationContext(), DashboardActivity.class);
                    dashboardActivity.putExtra(DashboardActivity.USERNAME_KEY, username);
                    startActivity(dashboardActivity);
                } else {
                    Toast.makeText(getApplicationContext(), "wrong email/password", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

    }
}