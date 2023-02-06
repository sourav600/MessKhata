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
import android.widget.Toast;

import com.example.messbook.Adapter.MemberAdapterClass;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MemberPage extends AppCompatActivity {
    RecyclerView recyclerView;
    Member_DB member_db;
    FloatingActionButton  memberFloatingBtn;
    ImageView memberImageMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);

        //change action bar color
//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1DB7AE"));
//        actionBar.setBackgroundDrawable(colorDrawable);
        getWindow().setStatusBarColor(ContextCompat.getColor(MemberPage.this, R.color.appColor));

        memberFloatingBtn = findViewById(R.id.memberFloatingBtnId);
        memberImageMenu = findViewById(R.id.memberImageMenuId);

        member_db = new Member_DB(MemberPage.this);
        recyclerView = (RecyclerView) findViewById(R.id.members_rv);

        //ArrayList<MemberModel> list = member_db.getMemberData();
        ArrayList<MemberModel> list = new ArrayList<>();

        MemberAdapterClass memberAdapter = new MemberAdapterClass(MemberPage.this,list);
        recyclerView.setAdapter(memberAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MemberPage.this);
        recyclerView.setLayoutManager(layoutManager);

        //get username from SignUp activity
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");

        //Get data from Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot itemSnapshot : snapshot.child(currentuser).child("members").getChildren()){
                    MemberModel memberModel = itemSnapshot.getValue(MemberModel.class);
                    list.add(memberModel);
                }
                memberAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MemberDB", "Failed to read value.", error.toException());
            }
        });



        memberImageMenu.setOnClickListener(view -> {
            startActivity(new Intent(MemberPage.this,NavigationActivity.class));
        });


        memberFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberPage.this, AddMember.class);
                startActivity(intent);
            }
        });
    }

}