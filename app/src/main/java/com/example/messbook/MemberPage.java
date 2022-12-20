package com.example.messbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.messbook.Adapter.MemberAdapterClass;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MemberPage extends AppCompatActivity {
    RecyclerView recyclerView;
    Member_DB member_db;
    FloatingActionButton  memberFloatingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);

        //change action bar color
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#54C0F1"));
        actionBar.setBackgroundDrawable(colorDrawable);
        memberFloatingBtn = findViewById(R.id.memberFloatingBtnId);

        member_db = new Member_DB(MemberPage.this);
        recyclerView = (RecyclerView) findViewById(R.id.members_rv);

        ArrayList<MemberModel> list = member_db.getMemberData();

        MemberAdapterClass memberAdapter = new MemberAdapterClass(MemberPage.this,list);
        recyclerView.setAdapter(memberAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MemberPage.this);
        recyclerView.setLayoutManager(layoutManager);

        memberFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberPage.this, AddMember.class);
                startActivity(intent);
            }
        });
    }

}