package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView AddMemberBtn, messMemberBtn,addCostBtn,messCostBtn ;
    private Button updateInfoBtn;
    private TextView TotalBalance,TotalMeal,TotalCost,RemainingBalance,mealRate;
    private Member_DB m_DB;
    private Cost_DB c_DB;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView imageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.titleBar));

        addCostBtn =  findViewById(R.id.addCostBtnId);
        messCostBtn =  findViewById(R.id.messCostBtnId);
        AddMemberBtn =  findViewById(R.id.AddMemberBtnId);
        messMemberBtn =  findViewById(R.id.MessMemberBtnId);
        TotalBalance = (TextView) findViewById(R.id.TotalBalanceId);
        TotalMeal = (TextView) findViewById(R.id.TotalMealId);
        TotalCost = (TextView) findViewById(R.id.TotalCostId);
        RemainingBalance = (TextView) findViewById(R.id.RemainingBalanceId);
        mealRate = (TextView) findViewById(R.id.mealRateId);
        updateInfoBtn = (Button) findViewById(R.id.updateInfoBtnId);


        addCostBtn.setOnClickListener(this);
        messCostBtn.setOnClickListener(this);
        messMemberBtn.setOnClickListener(this);
        updateInfoBtn.setOnClickListener(this);
        AddMemberBtn.setOnClickListener(this);


        // Navagation Drawar------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Drawar click event
        // Drawer item Click event ------
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.mHome:
                        Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.mShare:
                        Toast.makeText(MainActivity.this, "Facebook", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                }

                return false;
            }
        });

        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    //upadate all value after each member & cost added
    @Override
    protected void onResume() {
        super.onResume();
        m_DB = new Member_DB(MainActivity.this);
        c_DB = new Cost_DB(MainActivity.this);

        //Total Amount, Total Meal, Total Cost
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long sumAllAmnt=0, sumAllCost=0;
                float sumAllMeal = 0.0f;
                String currentUserMail = snapshot.child("currentUser").getValue(String.class);
                //Total Amount, meal
                for(DataSnapshot itemsnapshot : snapshot.child(currentUserMail).child("members").getChildren()){
                    sumAllAmnt += (itemsnapshot.child("money").getValue(Long.class));
                    sumAllMeal += itemsnapshot.child("meal").getValue(Float.class);
                }
                //Total Cost
                for(DataSnapshot itemsnapshot : snapshot.child(currentUserMail).child("costs").getChildren()){
                    sumAllCost += Long.parseLong(itemsnapshot.child("amount").getValue(String.class));
                }
                TotalBalance.setText(sumAllAmnt+" Tk ");
                TotalMeal.setText(String.format("%.1f",sumAllMeal)+"  ");
                TotalCost.setText(sumAllCost+" Tk");
                RemainingBalance.setText((sumAllAmnt-sumAllCost) + " Tk " );

                float m_rate = (float) sumAllCost/sumAllMeal;
                mealRate.setText(String.format("%.1f",m_rate)+" TK  ");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.AddMemberBtnId){
            Intent intent1 = new Intent(MainActivity.this, AddMember.class);
            startActivity(intent1);
        }
        else if(view.getId()==R.id.MessMemberBtnId){
            Intent intent2 = new Intent(MainActivity.this, MemberPage.class);
            startActivity(intent2);
        }
        else if(view.getId()==R.id.addCostBtnId){
            Intent intent3 = new Intent(MainActivity.this,AddCost.class);
            startActivity(intent3);
        }
        else if(view.getId()==R.id.messCostBtnId){
            Intent intent4 = new Intent(MainActivity.this,Costpage.class);
            startActivity(intent4);
        }
        else if(view.getId()==R.id.updateInfoBtnId){
            Intent intent5 = new Intent(MainActivity.this, Update_Info.class);
            startActivity(intent5);
        }

    }

}