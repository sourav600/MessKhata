package com.example.messbook.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.messbook.R.layout.member_dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messbook.Database.Cost_DB;
import com.example.messbook.Database.Member_DB;
import com.example.messbook.LoginActivity;
import com.example.messbook.Model.MemberModel;
import com.example.messbook.NavigationActivity;
import com.example.messbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MemberAdapterClass extends RecyclerView.Adapter<MemberAdapterClass.memberAdapterView>{

    Member_DB member_db;
    Cost_DB cost_db;
    Context context;
    ArrayList<MemberModel> memberModelArrayList;


    public MemberAdapterClass (Context context, ArrayList<MemberModel>memberModelArrayList){
        this.context = context;
        this.memberModelArrayList = memberModelArrayList;
    }

    @NonNull
    @Override
    public memberAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.member_layout, parent,false);
        return new memberAdapterView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull memberAdapterView holder, @SuppressLint("RecyclerView") final int position) {
        MemberModel memberModel = memberModelArrayList.get(position);
        holder.Name.setText(memberModel.getName()+"");
        holder.Deposit.setText(memberModel.getMoney()+" TK");
        holder.Meal.setText(memberModel.getMeal()+"");

//        member_db = new Member_DB(context);
//        cost_db = new Cost_DB(context);
//        //set current status of a member
//        float T_meal = member_db.getSumOfMeal();
//        int sumAllCost = cost_db.getTotalCost();
//        float meal_rate = (sumAllCost*1.0f) / T_meal;
//        holder.Amount.setText(String.format("%.1f",memberModel.getMoney() - (meal_rate*memberModel.getMeal()))+" ");

        float meal_rate = NavigationActivity.getMealRate();
        holder.Amount.setText(String.format("%.1f",memberModel.getMoney() - (meal_rate*memberModel.getMeal()))+" ");

        //get username from SignUp activity
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");
        //check Admin login or not
        SharedPreferences sharedPreferences2 = context.getSharedPreferences(LoginActivity.preferenceName,0);
        boolean adminLoggedIn = sharedPreferences2.getBoolean("hasLoggedIn",false);

        final String user = memberModelArrayList.get(position).getName();
        //click listener on recycler item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                if(adminLoggedIn) {
                    alertDialog.setTitle("Delete!");
                    alertDialog.setMessage("Do you want to delete this data?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).
                                    child("members");
                            Query query = ref.child(user);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();
                                    Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


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
                else{
                    Toast.makeText(context, "Only Admin can delete data", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberModelArrayList.size();
    }


    public class memberAdapterView extends RecyclerView.ViewHolder {
        TextView Name,Amount,Deposit,Meal;

        public memberAdapterView(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.memberNameId);
            Deposit = itemView.findViewById(R.id.memberDepositId);
            Meal = itemView.findViewById(R.id.memberMealId);
            Amount = itemView.findViewById(R.id.memberAmountId);
        }
    }
}
