package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderListController;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    ReminderListController controller;

    public ReminderAdapter(ReminderListController controller) {
         this.controller = controller;
    }
    @Override
    public ReminderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_reminder, parent, false);
        ReminderView reminderView = new ReminderView(view);
        return reminderView;
    }

    @Override
    public void onBindViewHolder(ReminderView holder, int position) {
        Reminder reminder = controller.getReminderByPosition(position);
        holder.name.setText(reminder.getTitle());
    }
    @Override
    public int getItemCount() {
        return 0;
    }

    class ReminderView extends RecyclerView.ViewHolder{
        @BindView(R.id.reminder_name) TextView name;
        @BindView(R.id.reminder_edit) ImageButton editBtn;
        public ReminderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
