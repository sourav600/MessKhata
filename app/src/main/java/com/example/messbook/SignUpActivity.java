package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText etRegEmail,etRegPassword,etUsername;
    TextView tvLoginHere;
    Button btnRegister;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.titleBar));


        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        etUsername = findViewById(R.id.etUsername);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegisterId);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }
    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String username = etUsername.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        LogInHelper helper = new LogInHelper(username,email,password);
                        reference.child(username).setValue(helper);
                        Toast.makeText(SignUpActivity.this, "Resistration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(SignUpActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
//    EditText signUpName, signUpMail, signUpPass;
//    Button signUpBtn;
//    TextView alreadyAccount;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference reference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//        signUpName = findViewById(R.id.signUpNameId);
//        signUpMail = findViewById(R.id.signUpEmailId);
//        signUpPass = findViewById(R.id.signUpPassId);
//        signUpBtn = findViewById(R.id.signUpBtnId);
//        alreadyAccount = findViewById(R.id.alreadyAccountId);
//
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseDatabase = FirebaseDatabase.getInstance();
//                reference = firebaseDatabase.getReference("users");
//
//                String name = signUpName.getText().toString();
//                String email = signUpMail.getText().toString();
//                String password = signUpPass.getText().toString();
//                if(name.isEmpty() | email.isEmpty() | password.isEmpty()){
//                    Toast.makeText(SignUpActivity.this, "Fill all option!", Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//                    LogInHelper helper = new LogInHelper(name, email, password);
//                    reference.child(email).setValue(helper);
//                    Toast.makeText(SignUpActivity.this, "Sign Up successfully!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
//        alreadyAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}