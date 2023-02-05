package com.example.messbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.Model.MemberModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private CardView AddMemberBtn, messMemberBtn,addCostBtn,messCostBtn ;
    private Button updateInfoBtn, warningYes, warningNo;
    private TextView TotalBalance,TotalMeal,TotalCost,RemainingBalance,mealRate;
    private Member_DB m_DB;
    private Cost_DB c_DB;
    FirebaseAuth mAuth;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView imageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colo2));

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
        imageMenu = findViewById(R.id.imageMenuId);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Drawar click event
        // Drawer item Click event ------
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.mHome);

        // App Bar Click Event
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //nav animation
//        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
//                final float offsetScale = 1 - diffScaledOffset;
//                contentView.setScaleX(offsetScale);
//                contentView.setScaleY(offsetScale);
//                final float xOffset = drawerView.getWidth() * slideOffset;
//                final float xoffSetDiff = contentView.getWidth() * diffScaledOffset / 2;
//                final float xT = xOffset - xoffSetDiff;
//                contentView.setTranslationX(xT);
//
//                super.onDrawerSlide(drawerView, slideOffset);
//            }
//        });
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            AlertDialog.Builder alertDialog  = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Warning!");
            alertDialog.setMessage("Do you want to exit ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }

    }
    //nav item click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        warningNo = findViewById(R.id.warningNoBtn);
        warningYes = findViewById(R.id.warningYesBtn);
        int id = item.getItemId();
        if(id==R.id.nav_C_listId){
            Intent intent = new Intent(MainActivity.this, Costpage.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_M_listId){
            Intent intent = new Intent(MainActivity.this, MemberPage.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_M_addId){
            Intent intent = new Intent(MainActivity.this, AddMember.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_C_addId){
            Intent intent = new Intent(MainActivity.this, AddCost.class);
            startActivity(intent);
        }
        else if(id == R.id.logoutId){
            AlertDialog.Builder alertDialog  = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Warning!");
            alertDialog.setMessage("Are you sure to log out ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences(LoginActivity.preferenceName,0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("hasLoggedIn",false);
                    editor.commit();
                    //mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }
        return true;
    }

    //upadate all value after each member & cost added
    @Override
    protected void onResume() {
        super.onResume();
        m_DB = new Member_DB(MainActivity.this);
        c_DB = new Cost_DB(MainActivity.this);

        //close Navigation if it's open
        drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);

        //Total Amount, Total Meal, Total Cost
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long sumAllAmnt=0, sumAllCost=0;
                float sumAllMeal = 0.0f;
                //String currentUserMail = snapshot.child("currentUser").getValue(String.class);
                //Total Amount, meal
                for(DataSnapshot itemsnapshot : snapshot.child("members").getChildren()){
                    sumAllAmnt += (itemsnapshot.child("money").getValue(Long.class));
                    sumAllMeal += itemsnapshot.child("meal").getValue(Float.class);
                }
                //Total Cost
                for(DataSnapshot itemsnapshot : snapshot.child("costs").getChildren()){
                    sumAllCost += Long.parseLong(itemsnapshot.child("amount").getValue(String.class));
                }
                TotalBalance.setText(sumAllAmnt+" Tk ");
                TotalMeal.setText(String.format("%.1f",sumAllMeal)+"  ");
                TotalCost.setText(sumAllCost+" Tk");
                RemainingBalance.setText((sumAllAmnt-sumAllCost) + " Tk " );

                if(sumAllMeal==0.0f || sumAllCost == 0) mealRate.setText("0.0 TK ");
                else {
                    float m_rate = (float) sumAllCost / sumAllMeal;
                    mealRate.setText(String.format("%.1f", m_rate) + " TK  ");
                }
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        String uid = user.getUid();
//        Toast.makeText(this, "User"+uid, Toast.LENGTH_SHORT).show();
//    }
}