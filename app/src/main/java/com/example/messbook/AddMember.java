package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMember extends AppCompatActivity {
    EditText name_et, initialDeposit_et,meal_et;
    Button addNewMemberBtn;
    Member_DB member_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        //change action bar color
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1DB7AE"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddMember.this, R.color.appColor));

        member_db = new Member_DB(AddMember.this);

        name_et = (EditText) findViewById(R.id.newMemberId);
        initialDeposit_et = (EditText) findViewById(R.id.initialDepositId);
        meal_et = (EditText) findViewById(R.id.addMealId);
        addNewMemberBtn = (Button) findViewById(R.id.addNewMemberBtnId);

        //get username from SignUp activity
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");

        addNewMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String newName = name_et.getText().toString();
                    int amount = Integer.parseInt(initialDeposit_et.getText().toString());
                    float meal = Float.parseFloat(meal_et.getText().toString());

                    if(newName.isEmpty()){
                        Toast.makeText(AddMember.this,"Please enter member name!",Toast.LENGTH_SHORT).show();
                    }

                    //pass parameter to model
                    else {
                        //SQLite
                        MemberModel model = new MemberModel(newName, amount, meal);
                        member_db.insertMemberData(model);

                        //Firebase
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("users").child(currentuser).child("members").child(newName);
                                myref.setValue(model);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });
                        Toast.makeText(AddMember.this, "Member Added Succesfully!", Toast.LENGTH_SHORT).show();


                        //go to first page
                        Intent intent2 = new Intent(AddMember.this, NavigationActivity.class);
                        startActivity(intent2);
                    }
                }
                catch (Exception e){
                    Toast.makeText(AddMember.this,"Fill the all option!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}