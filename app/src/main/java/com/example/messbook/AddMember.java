package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#54C0F1"));
        actionBar.setBackgroundDrawable(colorDrawable);

        member_db = new Member_DB(AddMember.this);

        name_et = (EditText) findViewById(R.id.newMemberId);
        initialDeposit_et = (EditText) findViewById(R.id.initialDepositId);
        meal_et = (EditText) findViewById(R.id.addMealId);
        addNewMemberBtn = (Button) findViewById(R.id.addNewMemberBtnId);

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
                        MemberModel model = new MemberModel(newName, amount, meal);
                        member_db.insertMemberData(model);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("currentUser");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String currentUserMail = snapshot.getValue(String.class);
                                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("users").child(currentUserMail).child("members").child(newName);
                                myref.setValue(model);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });
                        Toast.makeText(AddMember.this, "Member Added Succesfully!", Toast.LENGTH_SHORT).show();


                        //go to first page
                        Intent intent2 = new Intent(AddMember.this, MainActivity.class);
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