package com.example.maiquynhtruong.heathyreminders.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.maiquynhtruong.heathyreminders.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeView extends ConstraintLayout {
    @BindView(R.id.hello) TextView hello;
    @BindView(R.id.time_left) TextView timeLeft;

    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.welcome, null);
//        ButterKnife.bind(this, view);
        addView(view);
    }
}



