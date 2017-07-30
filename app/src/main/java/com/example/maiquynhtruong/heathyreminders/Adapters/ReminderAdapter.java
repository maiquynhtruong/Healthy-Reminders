package com.example.maiquynhtruong.heathyreminders.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderView> {
    @Override
    public ReminderView onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(ReminderView holder, int position) {

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
