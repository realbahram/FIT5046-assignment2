package com.example.goal.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goal.R;
import com.example.goal.entity.Goal;
import com.example.goal.viewmodel.GoalViewModel;

public class GoalAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private GoalViewModel goalViewModel;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Context context)
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameCard, txtNameCard2;

        public ViewHolder(RvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        ViewHolder(View itemView){
            super(itemView);
            txtNameCard=itemView.findViewById(R.id.txtNameCard);
            txtNameCard2=itemView.findViewById(R.id.txtNameCard2);
        }

        void setDetails(Goal goal){
            txtNameCard.setText(goal.getName());
            txtNameCard2.setText(goal.getCategory());
        }
    }
}


