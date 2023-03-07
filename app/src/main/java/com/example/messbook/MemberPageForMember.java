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
import android.view.View;
import android.widget.ImageView;

import com.example.messbook.Adapter.FirebaseAdapter;
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

public class MemberPageForMember extends AppCompatActivity {

    RecyclerView recyclerView;
    Member_DB member_db;
    FloatingActionButton memberFloatingBtn;
    ImageView memberImageMenu;
    FirebaseAdapter firebaseAdapter;
    MemberAdapterClass memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page_for_member);
        getWindow().setStatusBarColor(ContextCompat.getColor(MemberPageForMember.this, R.color.appColor));

        memberFloatingBtn = findViewById(R.id.memberFloatingBtnId);
        memberImageMenu = findViewById(R.id.memberImageMenuId);

        member_db = new Member_DB(MemberPageForMember.this);
        recyclerView = (RecyclerView) findViewById(R.id.members_rv);

        ArrayList<MemberModel> list = new ArrayList<>();

        //get username from Login activity
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.preferenceName,0);
        String currentuser = sharedPreferences.getString("user", "default_value");

        memberAdapter = new MemberAdapterClass(MemberPageForMember.this,list);
        recyclerView.setAdapter(memberAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MemberPageForMember.this);
        recyclerView.setLayoutManager(layoutManager);

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
            startActivity(new Intent(MemberPageForMember.this,NavigationActivityForMember.class));
        });

    }
}