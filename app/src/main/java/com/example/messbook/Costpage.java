package com.example.messbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.messbook.Adapter.CostAdapterClass;
import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Model.CostModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Costpage extends AppCompatActivity {

    RecyclerView costRecycler;
    Cost_DB costDb;
    FloatingActionButton costFloatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costpage);

        //change action bar color
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#54C0F1"));
        actionBar.setBackgroundDrawable(colorDrawable);

        costFloatingBtn = findViewById(R.id.costFloatingBtnId);
        costRecycler = (RecyclerView) findViewById(R.id.costRecyclerId);
        costDb = new Cost_DB(Costpage.this);

        ArrayList<CostModel> list = costDb.getCostData();

        CostAdapterClass costAdapter = new CostAdapterClass(Costpage.this,list);
        costRecycler.setAdapter(costAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Costpage.this);
        costRecycler.setLayoutManager(layoutManager);

        costFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Costpage.this, AddCost.class);
                startActivity(intent);
            }
        });
    }
}
