package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;

import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    public List<Reminder> reminderList;

    public ReminderAdapter() {
        Log.i("reminder-adapter", "created");
    }
    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_reminder, parent, false);
        final ReminderView reminderView = new ReminderView(view);
        Log.i("on-create-view-holder", "created");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = parent.indexOfChild(view);
                Toast.makeText(parent.getContext(), "You clicked on the " + position + "th child", Toast.LENGTH_LONG).show();
            }
        });
        return reminderView;
    }

    @Override
    public void onBindViewHolder(ReminderView holder, int position) {
        Reminder reminder = reminderList.get(position);
        Log.i("bindViewHolder", "position:" + position);
        holder.name.setText(reminder.getTitle());
        Log.i("bindViewHolder", "text:" + reminder.getTitle());
    }
    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ReminderView extends RecyclerView.ViewHolder{
        TextView name;
        ImageButton editBtn;
        public ReminderView(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reminder_title);
        }
    }
}
