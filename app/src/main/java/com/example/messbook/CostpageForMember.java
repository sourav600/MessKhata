package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.messbook.Adapter.CostAdapterClass;
import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Model.CostModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CostpageForMember extends AppCompatActivity {

    RecyclerView costRecycler;
    ImageView costImageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costpage_for_member);
        getWindow().setStatusBarColor(ContextCompat.getColor(CostpageForMember.this, R.color.appColor));

        costRecycler = (RecyclerView) findViewById(R.id.costRecyclerId);
        costImageMenu = findViewById(R.id.costImageMenuId);

        ArrayList<CostModel> list = new ArrayList<>();

        CostAdapterClass costAdapter = new CostAdapterClass(CostpageForMember.this,list);
        costRecycler.setAdapter(costAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CostpageForMember.this);
        costRecycler.setLayoutManager(layoutManager);

        //get username from Login activity
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.preferenceName,0);
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
            startActivity(new Intent(CostpageForMember.this,NavigationActivityForMember.class));
        });

    }
}