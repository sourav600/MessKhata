package com.example.messbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText logInEmail, logInPass;
    Button logInBtn;
    TextView noAccount;
    public static String preferenceName = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logInEmail = findViewById(R.id.logInMailId);
        logInPass = findViewById(R.id.logInPassId);
        logInBtn = findViewById(R.id.logInBtnId);
        noAccount = findViewById(R.id.noAccountId);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validMail() | !validPass()){

                }
                else {
                    checkUser();
                }
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean validMail(){
        String mail = logInEmail.getText().toString();
        if(mail.isEmpty()){
            logInEmail.setError("Mail can't be empty");
            return false;
        }
        else {
            logInEmail.setError(null);
            return true;
        }
    }
    public boolean validPass(){
        String password = logInPass.getText().toString();
        if(password.isEmpty()){
            logInPass.setError("Password can't be empty");
            return false;
        }
        else {
            logInPass.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String mail = logInEmail.getText().toString().trim();
        String pass = logInPass.getText().toString().trim();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserData = myRef.orderByChild("email").equalTo(mail);
        // Read from the database
        checkUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    logInEmail.setError(null);
                    String passFromDB = dataSnapshot.child(mail).child("password").getValue(String.class);

                    if(passFromDB.equals(pass)){
                        logInEmail.setError(null);
                        Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                        SharedPreferences preferences = getSharedPreferences(LoginActivity.preferenceName,0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("hasLoggedIn",true);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        logInPass.setError("Wrong Password");
                        logInPass.requestFocus();
                    }
                }
                else {
                    logInEmail.setError("User not found");
                    logInEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
}