package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.maiquynhtruong.heathyreminders.Activities.MainActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;

import java.util.ArrayList;
import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public static List<Reminder> reminderList;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    Context context;
    public ReminderAdapter(Context context) {
        this.reminderList = new ArrayList<>();
        this.context = context;
        viewBinderHelper.setOpenOnlyOne(true); // show only one swipe view at a time
//        createFakeReminders();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchCallBack());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void setUpReminders(List<Reminder> reminders) {
        reminderList.clear();
        reminderList.addAll(reminders);
        notifyDataSetChanged();
    }

    public void removeReminder(int position) {
        if (reminderList.isEmpty()) return;
        ((MainActivity)context).database.deleteReminder(reminderList.get(position).getId());
        ((MainActivity)context).receiver.cancelAlarm(context.getApplicationContext(), reminderList.get(position).getId());
        reminderList.remove(position);
        notifyItemRemoved(position);
    }

    public List<Reminder> createFakeReminders() {
        reminderList.add(new Reminder("Pay Internet bill", 12, 0, 9, 10, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Pay Insurance", 12, 0, 9, 5, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Change tooth brush", 12, 0, 9, 8, 2017, true, 3, Reminder.MONTHLY));
        return reminderList;
    }

    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipelayout_main, parent, false);
        return new ReminderView(view);
    }

    @Override
    public void onBindViewHolder(ReminderView holder, final int position) {
        final Reminder reminder = reminderList.get(position);
        // Save/restore the open/close state.
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(reminder.getId()));

        holder.title.setText(reminder.getTitle());
        holder.deleteSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminderList.isEmpty()) return;
                ((MainActivity)context).database.deleteReminder(reminderList.get(position).getId());
                ((MainActivity)context).receiver.cancelAlarm(context.getApplicationContext(), reminderList.get(position).getId());
                reminderList.remove(position);
                notifyItemRemoved(position);
            }
        });
        holder.editSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).showEditReminder(position);
            }
        });
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
        }
    }

    public class ItemTouchCallBack extends ItemTouchHelper.SimpleCallback {
        public ItemTouchCallBack() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);

        }
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            removeReminder(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
