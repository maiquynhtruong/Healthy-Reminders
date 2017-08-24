package com.example.maiquynhtruong.heathyreminders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    public List<Reminder> reminderList;
    public List<Reminder> pendingRemovalReminders;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    Context context;
    MainActivity activity;
    boolean undoOn;
    int mExpandedPosition = -1;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private static final int PENDING_REMOVAL_TIMEOUT = 7000; // 3sec
    HashMap<Reminder, Runnable> pendingRunnables = new HashMap<>(); // map of reminders to pending runnables, so we can cancel a removal if need be

    public ReminderAdapter(Context context) {
        this.reminderList = new ArrayList<>();
        this.context = context;
        this.pendingRemovalReminders = new ArrayList<>();
        activity = (MainActivity) context;
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
        Reminder reminder = reminderList.get(position);
        if (pendingRemovalReminders.contains(reminder)) {
            pendingRemovalReminders.remove(reminderList.get(position));
        }
        if (reminderList.contains(reminder)) {
            reminderList.remove(position);
            notifyItemRemoved(position);
        }
        if (reminderList.isEmpty()) {activity.noReminders.setVisibility(View.VISIBLE);}
        activity.database.deleteReminder(reminder.getId());
        activity.receiver.cancelAlarm(context.getApplicationContext(), reminder.getId());
    }

    public boolean isPendingRemoval(int position) {
        Reminder reminder = reminderList.get(position);
        return pendingRemovalReminders.contains(reminder);
    }
    public void pendingRemove(int position) {
        final Reminder reminder = reminderList.get(position);
        if (!pendingRemovalReminders.contains(reminder)) {
            pendingRemovalReminders.add(reminder);
            Toast.makeText(context, "added reminder " + reminder.getTitle() + " into pending queue", Toast.LENGTH_SHORT).show();
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    removeReminder(reminderList.indexOf(reminder));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT); // delay the thread
            pendingRunnables.put(reminder, pendingRemovalRunnable);
        }
    }

    public void undoDelete(Reminder reminder) {
        Runnable pendingRemovalRunnable = pendingRunnables.get(reminder);
        pendingRunnables.remove(reminder);
        if (pendingRemovalRunnable != null) {
            handler.removeCallbacks(pendingRemovalRunnable);
        }
        pendingRemovalReminders.remove(reminder);

        int position = reminderList.indexOf(reminder);
        recyclerView.scrollToPosition(position);
        notifyItemChanged(position);
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public List<Reminder> createFakeReminders() {
        reminderList.add(new Reminder("Pay Internet bill", 12, 0, 9, 10, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Pay Insurance", 12, 0, 9, 5, 2017, true, 1, Reminder.MONTHLY));
        reminderList.add(new Reminder("Change tooth brush", 12, 0, 9, 8, 2017, true, 3, Reminder.MONTHLY));
        return reminderList;
    }

    @Override
    public ReminderView onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_reminder, parent, false);
        return new ReminderView(view);
    }

    @Override
    public void onBindViewHolder(final ReminderView holder, final int position) {
        final Reminder reminder = reminderList.get(position);
        if (pendingRemovalReminders.contains(reminder)) {
            holder.mainLayout.setVisibility(View.GONE);
            holder.swipeLayout.setVisibility(View.VISIBLE);
            holder.undo.setVisibility(View.VISIBLE);
            holder.undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undoDelete(reminder);
                }
            });

        } else {
            holder.mainLayout.setVisibility(View.VISIBLE);
            holder.swipeLayout.setVisibility(GONE);
            holder.title.setText(reminder.getTitle());
            holder.undo.setVisibility(GONE);
            holder.undo.setOnClickListener(null);

            final boolean isExpanded = position == mExpandedPosition;
            holder.editBtn.setVisibility(isExpanded?View.VISIBLE:GONE);
            holder.dateTextView.setText(reminder.getMonth() + "/" + reminder.getDay() + "/" + reminder.getYear());
            holder.timeTextView.setText(reminder.getHour() + ":" + reminder.getMinute());
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
        int xMarkMargin;
        Drawable deleteIcon;
        boolean initiated;
        Paint backgroundPaint;
        Reminder reminder;
        public ItemTouchCallBack() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (!initiated) initiate(recyclerView, viewHolder);
            int itemHeight = itemView.getBottom() - itemView.getTop(); // height of itemView
            // mark the boundaries of the trash bin
            int intrinsicWidth = deleteIcon.getIntrinsicWidth();
            int intrinsicHeight = deleteIcon.getIntrinsicWidth();
            xMarkMargin = itemHeight / 3;
            if (!pendingRemovalReminders.contains(reminder) && reminderList.contains(reminder)) {
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
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        public void initiate(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.parseColor("#D32F2F"));
            deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24px);
            reminder = reminderList.get(viewHolder.getAdapterPosition());
            initiated = true;
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
            int position = viewHolder.getAdapterPosition();
            if (isPendingRemoval(position)) {
                return 0;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int swipePosition = viewHolder.getAdapterPosition();
            if (!pendingRemovalReminders.contains(reminder)) {
                Toast.makeText(context, "Swipe first time: " + reminder.getTitle(), Toast.LENGTH_SHORT).show();
                pendingRemove(swipePosition);
            } else {
                Toast.makeText(context, "Swipe a second tiem", Toast.LENGTH_LONG).show();
                viewHolder.itemView.setVisibility(GONE);
            }
        }
    }
}
