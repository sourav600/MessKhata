package com.example.messbook.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messbook.Model.MemberModel;
import com.example.messbook.NavigationActivity;
import com.example.messbook.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAdapter extends FirebaseRecyclerAdapter<MemberModel , FirebaseAdapter.memberAdapterView> {

    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseAdapter(@NonNull FirebaseRecyclerOptions<MemberModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull memberAdapterView holder, @SuppressLint("RecyclerView") final int position, @NonNull MemberModel model) {
        //MemberModel memberModel = memberModelArrayList.get(position);
        holder.Name.setText(model.getName()+"");
        holder.Deposit.setText(model.getMoney()+" TK");
        holder.Meal.setText(model.getMeal()+"");

        float meal_rate = NavigationActivity.getMealRate();
        holder.Amount.setText(String.format("%.1f",model.getMoney() - (meal_rate*model.getMeal()))+" ");

        //get username from SignUp activity
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preferences", MODE_PRIVATE);
        String currentuser = sharedPreferences.getString("user", "default_value");

        //click listener on recycler item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog  = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete?");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).
                                child("members").child(getRef(position).getKey()).removeValue();
                        Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            }
        });
    }

    @NonNull
    @Override
    public memberAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.member_layout, parent,false);
        return new FirebaseAdapter.memberAdapterView(view);
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
