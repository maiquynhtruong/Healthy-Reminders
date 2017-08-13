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

import java.util.ArrayList;
import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    public static List<Reminder> reminderList;

    public ReminderAdapter() {
        this.reminderList = new ArrayList<>();
        Log.i("reminder-adapter", "created");
    }

    public void setUpReminders(List<Reminder> reminders) {
        reminderList.addAll(reminders);
        notifyDataSetChanged();
    }

    public List<Reminder> createFakeReminders() {
        List<Reminder> reminderList = new ArrayList<>();
        reminderList.add(new Reminder("Pay Internet bill", 12, 0, 9, 10, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Pay Insurance", 12, 0, 9, 5, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Change tooth brush", 12, 0, 9, 8, 2017, true, 3, Reminder.MONTHLY));
        return reminderList;
    }

    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_reminder, parent, false);
        final ReminderView reminderView = new ReminderView(view);
        Log.i("on-create-view-holder", "created");
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


    class ReminderView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView name;
        ImageButton editBtn;
        public ReminderView(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reminder_title_view_holder);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
