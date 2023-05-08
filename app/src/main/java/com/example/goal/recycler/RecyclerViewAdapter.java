package com.example.goal.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goal.R;
import com.example.goal.entity.Goal;
import com.example.goal.viewmodel.GoalViewModel;

import java.util.ArrayList;
import java.util.List;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.GoalViewHolder> {
    private List<Goal> goals = new ArrayList<>();
    private GoalViewModel goalViewModel;

    public RecyclerViewAdapter(GoalViewModel viewModel) {
        this.goalViewModel = viewModel;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.items_goals, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);
        holder.bind(goal);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class GoalViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView categoryTextView;
        private TextView tasksTextView;
        private TextView startDateTextView;
        private TextView endDateTextView;
        private TextView priorityTextView;
        private Button deleteButton;
        private Button completeButton;
        private ImageView completionimg;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.btnDelete);
            nameTextView = itemView.findViewById(R.id.txtNameCard);
            categoryTextView = itemView.findViewById(R.id.txtCategoryCard);
            tasksTextView = itemView.findViewById(R.id.txtTasksCard);
            startDateTextView = itemView.findViewById(R.id.txtStartDateCard);
            endDateTextView = itemView.findViewById(R.id.txtEndDateCard);
            priorityTextView = itemView.findViewById(R.id.txtPriorityCard);
            completeButton = itemView.findViewById(R.id.btnStatus);
            completionimg = itemView.findViewById(R.id.compl_img);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Goal goal = goals.get(position);
                        goalViewModel.deleteGoal(goal);
                    }
                }
            });
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Goal goal = goals.get(position);
                        goal.setStatus("Complete"); // Update the goal status
                        goalViewModel.updateGoalStatus(goal);
                        notifyItemChanged(position);

                    }
                }
            });
        }

        public void bind(Goal goal) {
            nameTextView.setText(goal.getName());
            categoryTextView.setText(goal.getCategory());
            tasksTextView.setText(goal.getTasks());
            startDateTextView.setText("Start date: " + goal.getStartDate());
            endDateTextView.setText("End date: " + goal.getEndDate());
            priorityTextView.setText("Priority: " + goal.getPriority());
            if (goal.getStatus().equals("Complete")) {
                completionimg.setVisibility(View.VISIBLE);
            } else {
                completionimg.setVisibility(View.GONE);
            }

        }

    }
}

