package com.example.messbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Update_Info extends AppCompatActivity {

    private float meal = 0.5f;
    private FloatingActionButton incrementMeal, decrementMeal;
    private TextView updateMeal_tv;
    private Spinner spinner;
    private Button updateBtn;
    private TextView updateAmount_tv;
    Member_DB member_db = new Member_DB(this);

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
               String  selected = spinner.getSelectedItem().toString();
               if(selected.equals("Select")){
                   Toast.makeText(Update_Info.this, "Please select a person", Toast.LENGTH_SHORT).show();
               }
               else {
                   Toast.makeText(Update_Info.this, selected, Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    //fill spinner from database
    public void fillSpinner() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Select");
        arrayList.addAll(member_db.getMemberName()) ;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}