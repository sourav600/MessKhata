package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAsMemberActivity extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere,tvForgotPass,loginAsmember;
    Button btnLogin;
    FirebaseAuth mAuth;

    public static String preferenceName2 = "MyPrefs2";
    TextView loginAsAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as_member);
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginAsMemberActivity.this, R.color.appColor));

        etLoginEmail = findViewById(R.id.etLoginEmail2);
        etLoginPassword = findViewById(R.id.etLoginPass2);
        btnLogin = findViewById(R.id.btnLoginId2);
        loginAsAdmin = findViewById(R.id.loginAsAdminId);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });

        loginAsAdmin.setOnClickListener(view -> {
            startActivity(new Intent(LoginAsMemberActivity.this, LoginActivity.class));
        });
    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginAsMemberActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences(LoginAsMemberActivity.preferenceName2,0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("hasLoggedIn",true);
                        editor.commit();
                        startActivity(new Intent(LoginAsMemberActivity.this, NavigationActivityForMember.class));
                    }else{
                        Toast.makeText(LoginAsMemberActivity.this, "Login Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}