package com.example.messbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signUpName, signUpMail, signUpPass;
    Button signUpBtn;
    TextView alreadyAccount;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpName = findViewById(R.id.signUpNameId);
        signUpMail = findViewById(R.id.signUpEmailId);
        signUpPass = findViewById(R.id.signUpPassId);
        signUpBtn = findViewById(R.id.signUpBtnId);
        alreadyAccount = findViewById(R.id.alreadyAccountId);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("users");

                String name = signUpName.getText().toString();
                String email = signUpMail.getText().toString();
                String password = signUpPass.getText().toString();
                if(name.isEmpty() | email.isEmpty() | password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Fill all option!", Toast.LENGTH_SHORT).show();
                }

                else {
                    LogInHelper helper = new LogInHelper(name, email, password);
                    reference.child(email).setValue(helper);

                    Toast.makeText(SignUpActivity.this, "Sign Up successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}