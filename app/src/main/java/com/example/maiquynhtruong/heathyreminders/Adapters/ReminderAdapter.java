package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.Activities.MainActivity;
import com.example.maiquynhtruong.heathyreminders.R;
import com.example.maiquynhtruong.heathyreminders.Reminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    public List<Reminder> reminderList;
    public List<Reminder> reminderPendingRemoval;
    public static int PENDING_REMOVAL_TIMEOUT = 3000;
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    Context context;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    public ReminderAdapter(Context context) {
        this.reminderList = new ArrayList<>();
        this.context = context;
        this.reminderPendingRemoval = new ArrayList<>();
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
        reminderPendingRemoval.remove(reminderList.get(position));
        reminderList.remove(position);
        notifyItemRemoved(position);
    }

    public void pendingRemoveReminder(int position) {
        final Reminder reminder = reminderList.get(position);
        reminderPendingRemoval.add(reminder);
        // this will redraw row in "undo" state
        notifyItemChanged(position);
        // let's create, store and post a runnable to remove the item
        Runnable pendingRemovalRunnable = new Runnable() {
            @Override
            public void run() {
                removeReminder(reminderList.indexOf(reminder));
            }
        };
        handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
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
        if (reminderPendingRemoval.contains(reminder)) {
//            holder.itemView.setBackground(Color.RED);
            holder.mainLayout.setVisibility(GONE);
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
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).showEditReminder(position);
            }
        });
    }

    public void undoDelete(Reminder reminder) {
        reminderList.add(reminder);
        reminderPendingRemoval.remove(reminder);
        int position = reminderList.indexOf(reminder);
        recyclerView.scrollToPosition(position);
        notifyItemChanged(position);
    }
    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ReminderView extends RecyclerView.ViewHolder {
        TextView title, undo, deleteSwipe;
        LinearLayout mainLayout, swipeLayout;
        CardView cardView;
        public ReminderView(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reminder_title_view_holder);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            mainLayout = itemView.findViewById(R.id.main_layout);
            deleteSwipe = itemView.findViewById(R.id.swipe_delete);
            undo = itemView.findViewById(R.id.undo);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }

    public class ItemTouchCallBack extends ItemTouchHelper.SimpleCallback {

        Drawable background, xMark; // cache these and not allocate anything repeatedly in the onChildDraw method
        int xMarkMargin = 5;
        Drawable deleteIcon;
        boolean initiated;
        public ItemTouchCallBack() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (!initiated) initiate();
            if (dX < 0) { // swipe to the left
                // draw the background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // mark the boundaries of the trash bin
                int itemHeight = itemView.getBottom() - itemView.getTop(); // height of itemView
                int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                int intrinsicHeight = deleteIcon.getIntrinsicWidth();
                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                // now draw the bin
                deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                deleteIcon.draw(c);

                // set the swipe delete text
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(48);
                paint.setTextAlign(Paint.Align.CENTER);
                c.drawText("DELETE", itemView.getLeft() + 150, xMarkBottom, paint);
            } else {
                // draw the background
                background.setBounds(itemView.getRight(), itemView.getTop(), itemView.getLeft() - (int) dX, itemView.getBottom());
                background.draw(c);

                // mark the boundaries of the trash bin
                int itemHeight = itemView.getBottom() - itemView.getTop(); // height of itemView
                int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                int intrinsicHeight = deleteIcon.getIntrinsicWidth();
                int xMarkLeft = itemView.getLeft() + xMarkMargin;
                int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                // now draw the bin
                deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                deleteIcon.draw(c);

                // set the swipe delete text
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(48);
                paint.setTextAlign(Paint.Align.CENTER);
                c.drawText("DELETE", itemView.getRight() - 150, xMarkBottom, paint);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        public void initiate() {
            background = new ColorDrawable(Color.RED);
            deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24px);
            initiated = true;
        }
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int swipePosition = viewHolder.getAdapterPosition();
            removeReminder(swipePosition);
        }
    }
}
