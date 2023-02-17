package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.messbook.Adapter.CostAdapterClass;
import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Model.CostModel;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Costpage extends AppCompatActivity {

    RecyclerView costRecycler;
    Cost_DB costDb;
    FloatingActionButton costFloatingBtn;
    ImageView costImageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costpage);

        //change action bar color
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1DB7AE"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        getWindow().setStatusBarColor(ContextCompat.getColor(Costpage.this, R.color.appColor));

        costFloatingBtn = findViewById(R.id.costFloatingBtnId);
        costRecycler = (RecyclerView) findViewById(R.id.costRecyclerId);
        costImageMenu = findViewById(R.id.costImageMenuId);
        //costDb = new Cost_DB(Costpage.this);

        //ArrayList<CostModel> list = costDb.getCostData();
        ArrayList<CostModel> list = new ArrayList<>();

        CostAdapterClass costAdapter = new CostAdapterClass(Costpage.this,list);
        costRecycler.setAdapter(costAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Costpage.this);
        costRecycler.setLayoutManager(layoutManager);

        //get username from SignUp activity
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");

        //Get data from Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot itemSnapshot : snapshot.child(currentuser).child("costs").getChildren()){
                    CostModel costModel = itemSnapshot.getValue(CostModel.class);
                    list.add(costModel);
                }
                costAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MemberDB", "Failed to read value.", error.toException());
            }
        });

        costImageMenu.setOnClickListener(view -> {
            startActivity(new Intent(Costpage.this,NavigationActivity.class));
        });

        costFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Costpage.this, AddCost.class);
                startActivity(intent);
            }
        });
    }
}
