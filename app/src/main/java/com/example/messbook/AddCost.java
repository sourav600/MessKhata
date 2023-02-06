package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.CostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCost extends AppCompatActivity {
    EditText costAmount,costDesc;
    TextView costDate,calander;
    Button addCostBtn;
    Member_DB member_db;
    Cost_DB cost_db;
    Spinner spinner;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        //change action bar color
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1DB7AE"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddCost.this, R.color.appColor));

        costDate = findViewById(R.id.costDate_Id);
        costAmount = findViewById(R.id.costAmountId);
        addCostBtn = findViewById(R.id.addCost_BtnId);
        calander = findViewById(R.id.calanderId);
        cost_db = new Cost_DB(AddCost.this);
        costDesc = findViewById(R.id.costDescriptionId);
        spinner = (Spinner) findViewById(R.id.memberSpinner2);

        //Spinner fill
        fillSpinner();

        //current date
        DatePicker datePicker = new DatePicker(AddCost.this);
        int currentDay = datePicker.getDayOfMonth();
        int currentMonth = datePicker.getMonth()+1;
        int currentYear = datePicker.getYear();
        costDate.setText(" "+currentDay+"-"+currentMonth+"-"+currentYear);

        //Date peaker dialog box open
        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contractor with 4 parameters
                datePickerDialog = new DatePickerDialog(AddCost.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                costDate.setText(" "+day+"-"+(month+1)+"-"+year);
                            }
                        },currentYear,currentMonth,currentDay);
                datePickerDialog.show();
            }
        });

        //get username from SignUp activity
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");

        //send user data to cost model
        addCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String Name = spinner.getSelectedItem().toString();
                    String Date = costDate.getText().toString();
                    String Amount = costAmount.getText().toString();
                    String Description = costDesc.getText().toString().trim();

                    if(Name.equals("Select")){
                        Toast.makeText(AddCost.this,"Please select a member!",Toast.LENGTH_SHORT).show();
                    }
                    else if(Amount.isEmpty()){
                        Toast.makeText(AddCost.this,"Please enter amount!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //SQLite
                        CostModel model = new CostModel(Name,Date,Amount,Description);
                        cost_db.insertCostData(model);

                        //Firebase
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DatabaseReference myref = FirebaseDatabase.getInstance().getReference("users").child(currentuser).child("costs").child(costDate.getText().toString());
                                myref.setValue(model);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });
                        Toast.makeText(AddCost.this, "Cost Added Successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddCost.this, NavigationActivity.class);
                        startActivity(intent);
                    }
                }
                catch (Exception e){
                    Toast.makeText(AddCost.this,"Please enter cost amount!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //fill spinner from database
    public void fillSpinner() {
        //get username from SignUp activity
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");
        member_db = new Member_DB(this);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Select");
        //arrayList.addAll(member_db.getMemberName()) ;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSanpshot : snapshot.child(currentuser).child("members").getChildren()){
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