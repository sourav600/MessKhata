package com.example.messbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messbook.Model.CostModel;
import com.example.messbook.R;

import java.util.ArrayList;

public class CostAdapterClass extends RecyclerView.Adapter<CostAdapterClass.costAdapterView> {

    Context context;
    ArrayList<CostModel> costModelArrayList;

    public CostAdapterClass(Context context, ArrayList<CostModel>costModelArrayList){
        this.context = context;
        this.costModelArrayList = costModelArrayList;
    }

    @NonNull
    @Override
    public costAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cost_layout,parent,false);
        return new costAdapterView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull costAdapterView holder, int position) {
        CostModel costModel = costModelArrayList.get(position);
        holder.person.setText(costModel.getName()+"");
        holder.date.setText(costModel.getDate()+"");
        holder.expenditure.setText(costModel.getAmount()+" TK");
        holder.description.setText(costModel.getDesc()+"");
    }

    @Override
    public int getItemCount() {
        return costModelArrayList.size();
    }


    public class costAdapterView extends RecyclerView.ViewHolder{
        TextView person,date, expenditure,description;
        public costAdapterView(@NonNull View itemView) {
            super(itemView);
            person = itemView.findViewById(R.id.costPersonId);
            date = itemView.findViewById(R.id.costDateId);
            expenditure = itemView.findViewById(R.id.costID);
            description = itemView.findViewById(R.id.descID);
        }
    }
}
