package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerPreference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static com.example.maiquynhtruong.heathyreminders.ReminderDetailsActivity.REMINDER_DETAILS_ID;
import static com.example.maiquynhtruong.heathyreminders.ReminderReceiver.REMINDER_REPEAT_NUMBER;
import static com.example.maiquynhtruong.heathyreminders.ReminderReceiver.REMINDER_REPEAT_TYPE;
import static com.example.maiquynhtruong.heathyreminders.ReminderReceiver.REMINDER_TIME_MILLIS;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    public List<Reminder> reminderList;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    Context context;
    MainActivity activity;
    boolean undoOn;
    int mExpandedPosition = -1;
//    SharedPreferences sharedPreferences;

    public ReminderAdapter(Context context) {
        this.reminderList = new ArrayList<>();
        this.context = context;
        activity = (MainActivity) context;
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
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
        Log.i("ReminderAdapter", "removeReminder() removes reminder with position " + position);
        Reminder reminder = reminderList.get(position);
        reminderList.remove(position);
        notifyItemRemoved(position);
        if (reminderList.isEmpty()) {activity.noReminders.setVisibility(View.VISIBLE);}
        activity.database.deleteReminder(reminder.getId());
        ReminderReceiver.cancelAlarm(context.getApplicationContext(), reminder.getId());
    }

    public void undoDelete(int position, Reminder reminder) {
        reminderList.add(position, reminder);
        reminder.setId((int) activity.database.setReminder(reminder)); // need to have a new id
        Intent intent = new Intent(context.getApplicationContext(), ReminderReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, reminder.getYear());
        calendar.set(Calendar.MONTH, reminder.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, reminder.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calendar.set(Calendar.MINUTE, reminder.getMinute());
        calendar.set(Calendar.SECOND, 0);
        intent.putExtra(REMINDER_REPEAT_TYPE, reminder.getRepeatType());
        intent.putExtra(REMINDER_TIME_MILLIS, calendar.getTimeInMillis());
        intent.putExtra(REMINDER_DETAILS_ID, reminder.getId());
        intent.putExtra(REMINDER_REPEAT_NUMBER, reminder.getRepeatNumber());
        context.startService(intent);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_reminder, parent, false);
        return new ReminderView(view);
    }

    @Override
    public void onBindViewHolder(final ReminderView holder, final int position) {
        final Reminder reminder = reminderList.get(position);
//        Log.i("ReminderAdapter", "onBindViewHolder() current background color: " + sharedPreferences.getString(Settings.PREF_KEY_COLOR_PICKER, "#C5CAE9"));
//        Log.i("ReminderAdapter", "onBindViewHolder() current background color: #" + Integer.toHexString(sharedPreferences.getInt(Settings.PREF_KEY_COLOR_PICKER, 0xC5CAE9)));
//        holder.itemView.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(sharedPreferences.getInt(Settings.PREF_KEY_COLOR_PICKER, 0xC5CAE9))));
//        holder.itemView.setBackgroundColor(Color.parseColor("#" + sharedPreferences.getString(Settings.PREF_KEY_COLOR_PICKER, "C5CAE9")));
        holder.itemView.setBackgroundColor(Color.parseColor(reminder.getColor()));
        holder.mainLayout.setVisibility(View.VISIBLE);
        holder.title.setText(reminder.getTitle());

        final boolean isExpanded = position == mExpandedPosition;
        holder.editBtn.setVisibility(isExpanded?View.VISIBLE:GONE);
        boolean isPM = (reminder.getHour() >= 12);
        holder.timeTextView.setText(String.format(Locale.US, "%02d:%02d %s", (reminder.getHour() == 12 || reminder.getHour() == 0) ? 12 :
                reminder.getHour() % 12, reminder.getMinute(), isPM ? "PM" : "AM"));
        Log.i("ReminderAdapter", "onBindViewHolder() setting reminder title as " + reminder.getTitle() + " and date as " + reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
        holder.dateTextView.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
        holder.timeAndDate.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showEditReminder(reminderList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ReminderView extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout, timeAndDate;
        TextView title, undo, deleteSwipe, timeTextView, dateTextView;
        LinearLayout swipeLayout;
        RelativeLayout mainLayout;
        ImageView editBtn;
        CardView cardView;
        public ReminderView(final View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            title = itemView.findViewById(R.id.reminder_title_view_holder);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            mainLayout = itemView.findViewById(R.id.main_layout);
            deleteSwipe = itemView.findViewById(R.id.swipe_delete);
            undo = itemView.findViewById(R.id.swipe_undo);
            cardView = itemView.findViewById(R.id.card_view);
            timeAndDate = itemView.findViewById(R.id.time_and_date);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            editBtn = itemView.findViewById(R.id.edit_btn);
        }
    }

    public class ItemTouchCallBack extends ItemTouchHelper.SimpleCallback {
        RectF background;
        int xMarkMargin, intrinsicWidth, intrinsicHeight;
        Drawable deleteIcon;
        boolean initiated;
        Paint backgroundPaint;
//        Reminder reminder;
        int itemHeight;
        public ItemTouchCallBack() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        public void initiate(RecyclerView.ViewHolder viewHolder) {
            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.parseColor("#D32F2F")); // it's a red color
            deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24px);
            // mark the boundaries of the trash bin
            itemHeight = viewHolder.itemView.getBottom() - viewHolder.itemView.getTop(); // height of itemView
            intrinsicWidth  = deleteIcon.getIntrinsicWidth();
            intrinsicHeight = deleteIcon.getIntrinsicWidth();
            xMarkMargin = 75;
            initiated = true;
        }
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (!initiated) initiate(viewHolder);
            Log.i("ReminderAdapter", "onChildDraw() current reminder with position " + viewHolder.getAdapterPosition());
//            if (!pendingRemovalReminders.contains(reminder)) {
                if (dX < 0) { // swipe to the left
                    // draw the background
                    background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, backgroundPaint);

                    int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                    int xMarkRight = itemView.getRight() - xMarkMargin;
                    int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int xMarkBottom = xMarkTop + intrinsicHeight;
                    // now draw the bin
                    deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                    deleteIcon.draw(c);
                } else { // swipe to the right
                    // draw the background
                    background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, backgroundPaint);

                    int xMarkLeft = itemView.getLeft() + xMarkMargin;
                    int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
                    int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int xMarkBottom = xMarkTop + intrinsicHeight;
                    // now draw the bin
                    deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                    deleteIcon.draw(c);
                }
//            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int swipePosition = viewHolder.getAdapterPosition();
            final Reminder reminder = reminderList.get(swipePosition);
            String name = reminder.getTitle();
            removeReminder(swipePosition);
            Snackbar.make(viewHolder.itemView, name + " deleted!", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("ReminderAdapter", "onSwiped() trying to undo delete reminder " + reminder.getTitle() + " with position " + swipePosition);
                            undoDelete(swipePosition, reminder);
                        }
                    }).show();
        }
    }
}
