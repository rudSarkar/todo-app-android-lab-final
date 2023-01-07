package xyz.rudra0x01.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import xyz.rudra0x01.todoapp.database.databaseConnect;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout nameTxt, emailTxt, passwdTxt;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTxt = findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        passwdTxt = findViewById(R.id.passwdTxt);
        registerBtn = findViewById(R.id.registerBtn);

        databaseConnect dbHelper = new databaseConnect(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dbHelper.insertUser(nameTxt.getEditText().getText().toString(),
                            emailTxt.getEditText().getText().toString(),
                            passwdTxt.getEditText().getText().toString());

                    Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG).show();

                    // TODO: send user to login page for login
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}