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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Update_Info extends AppCompatActivity {

    private float meal = 0.5f;
    private FloatingActionButton incrementMeal, decrementMeal;
    private TextView updateMeal_tv;
    private Spinner spinner;
    private Button updateBtn;
    private TextView updateAmount_tv;
    DatabaseReference reference;
    Member_DB member_db = new Member_DB(this);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        //change action bar color
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#54C0F1"));
        actionBar.setBackgroundDrawable(colorDrawable);


        incrementMeal = findViewById(R.id.incrementMealId);
        decrementMeal = findViewById(R.id.decrementMealID);
        updateMeal_tv = (TextView) findViewById(R.id.updateMealId);
        spinner = (Spinner) findViewById(R.id.memberSpinner);
        updateBtn = (Button) findViewById(R.id.updateBtnId);
        updateAmount_tv = findViewById(R.id.updateAmountId);
        reference = FirebaseDatabase.getInstance().getReference("users");

        //Spinner
        fillSpinner();

       incrementMeal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               meal += 0.5;
               updateMeal_tv.setText(meal+"");
           }
       });
       decrementMeal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               meal -= 0.5;
               updateMeal_tv.setText(meal+"");
           }
       });
       updateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String  selectedPerson = spinner.getSelectedItem().toString();
               if(selectedPerson.equals("Select")){
                   Toast.makeText(Update_Info.this, "Please select a person", Toast.LENGTH_SHORT).show();
               }
               else {
                    float mealAdd = 0.0f+Float.parseFloat(updateMeal_tv.getText().toString());
                    int amountAdd = Integer.parseInt("0"+updateAmount_tv.getText().toString());

                   HashMap updateData = new HashMap();

                   reference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(flag==true) {
                               String currentUser = (String) snapshot.child("currentUser").getValue(String.class);
                               int amountFromDB = snapshot.child(currentUser).child("members").child(selectedPerson).child("money").getValue(Integer.class);
                               float mealFromDB = snapshot.child(currentUser).child("members").child(selectedPerson).child("meal").getValue(Float.class);
                               updateData.put("meal", (mealAdd + mealFromDB));
                               updateData.put("money", (amountAdd + amountFromDB));
                               reference.child(currentUser).child("members").child(selectedPerson).updateChildren(updateData);
                               Toast.makeText(Update_Info.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                               flag=false;
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Log.w("MemberDB", "Failed to read value.", error.toException());
                       }
                   });
               }
           }
       });
    }

    //fill spinner from database
    public void fillSpinner() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Select");
        //arrayList.addAll(member_db.getMemberName()) ;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserMail = snapshot.child("currentUser").getValue(String.class);
                for(DataSnapshot itemSanpshot : snapshot.child(currentUserMail).child("members").getChildren()){
                    arrayList.add(itemSanpshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}