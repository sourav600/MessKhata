package com.example.messbook.Adapter;

import static com.example.messbook.R.layout.member_dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.example.messbook.Model.MemberModel;
import com.example.messbook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    public void onBindViewHolder(@NonNull memberAdapterView holder, int position) {
        MemberModel memberModel = memberModelArrayList.get(position);
        holder.Name.setText(memberModel.getName()+"");
        holder.Deposit.setText(memberModel.getMoney()+" TK");
        holder.Meal.setText(memberModel.getMeal()+"");

        member_db = new Member_DB(context);
        cost_db = new Cost_DB(context);
        //set current status of a member
        float T_meal = member_db.getSumOfMeal();
        int sumAllCost = cost_db.getTotalCost();
        float meal_rate = (sumAllCost*1.0f) / T_meal;
        holder.Amount.setText(String.format("%.1f",memberModel.getMoney() - (meal_rate*memberModel.getMeal()))+" ");

        //click on member row recycler item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private float meal = 0.5f;

            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.member_dialog);
                dialog.show();

                FloatingActionButton incrementMeal =  dialog.findViewById(R.id.incrementMealId);
                FloatingActionButton decrementMeal = dialog.findViewById(R.id.decrementMealID);
                TextView updateMeal_tv = dialog.findViewById(R.id.updateMealId);
                TextView updateAmount_tv = dialog.findViewById(R.id.updateAmountId);
                Button updateBtn = dialog.findViewById(R.id.updateBtnId);

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





//
//                updateBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        member_db = new Member_DB(context);
//                        member_db.updateMember(name, amount,meal);
//                        Toast.makeText(context, "Information Updated !", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberModelArrayList.size();
    }


    public class memberAdapterView extends RecyclerView.ViewHolder{
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
