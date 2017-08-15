package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.maiquynhtruong.heathyreminders.Activities.ReminderDetailsActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;

import java.util.ArrayList;
import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public static List<Reminder> reminderList;

    public ReminderAdapter() {
        this.reminderList = new ArrayList<>();
        viewBinderHelper.setOpenOnlyOne(true); // show only one swipe view at a time
        createFakeReminders();
    }

    public void setUpReminders(List<Reminder> reminders) {
        reminderList.clear();
        reminderList.addAll(reminders);
        notifyDataSetChanged();
    }

    public List<Reminder> createFakeReminders() {
//        List<Reminder> reminderList = new ArrayList<>();
        reminderList.add(new Reminder("Pay Internet bill", 12, 0, 9, 10, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Pay Insurance", 12, 0, 9, 5, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Change tooth brush", 12, 0, 9, 8, 2017, true, 3, Reminder.MONTHLY));
        return reminderList;
    }

    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipelayout_main, parent, false);
        final ReminderView reminderView = new ReminderView(view);
        return reminderView;
    }

    @Override
    public void onBindViewHolder(ReminderView holder, int position) {
        Reminder reminder = reminderList.get(position);
        // Save/restore the open/close state.
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(reminder.getId()));

        holder.title.setText(reminder.getTitle());
        Log.i("bindViewHolder", "text:" + reminder.getTitle());
    }
    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void saveStates(Bundle outState) {
        viewBinderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        viewBinderHelper.restoreStates(inState);
    }

    class ReminderView extends RecyclerView.ViewHolder{
        TextView title;
        ImageView deleteSwipe, editSwipe;
        SwipeRevealLayout swipeRevealLayout;

        public ReminderView(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reminder_title_view_holder);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            deleteSwipe = itemView.findViewById(R.id.swipe_delete);
            editSwipe = itemView.findViewById(R.id.swipe_edit);

            deleteSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    notifyItemRemoved(position);
                    Toast.makeText(itemView.getContext(), "Gonna delete the reminder at index " + position, Toast.LENGTH_LONG).show();
                }
            });
            editSwipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Edit this reminder", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(itemView.getContext(), ReminderDetailsActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
