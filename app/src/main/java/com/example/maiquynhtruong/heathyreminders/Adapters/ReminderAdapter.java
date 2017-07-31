package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;
import com.example.maiquynhtruong.heathyreminders.ReminderListController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    ReminderListController controller;
    public List<Reminder> reminderList;

    public ReminderAdapter(ReminderListController controller) {
        Log.i("reminder-adapter", "created");
         this.controller = controller;
    }
    @Override
    public ReminderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_reminder, parent, false);
        ReminderView reminderView = new ReminderView(view);
        Log.i("on-create-view-holder", "created");
        return reminderView;
    }

    @Override
    public void onBindViewHolder(ReminderView holder, int position) {
        Reminder reminder = controller.getReminderByPosition(position);
        Log.i("bindViewHolder", "position:" + position);
        holder.name.setText(reminder.getTitle());
        Log.i("bindViewHolder", "text:" + reminder.getTitle());
    }
    @Override
    public int getItemCount() {
        return controller.getListSize();
    }

    class ReminderView extends RecyclerView.ViewHolder{
//        @BindView(R.id.reminder_name)
        TextView name;
//        @BindView(R.id.reminder_edit)
        ImageButton editBtn;
        public ReminderView(View itemView) {
            super(itemView);
            Log.i("reminder-view", "created with name:" + name);
//            ButterKnife.bind(this, itemView);
            name = itemView.findViewById(R.id.reminder_name);
            editBtn = itemView.findViewById(R.id.reminder_edit);
        }
    }
}
