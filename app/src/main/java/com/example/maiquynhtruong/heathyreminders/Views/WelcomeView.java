package com.example.maiquynhtruong.heathyreminders.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by maiquynhtruong on 7/30/17.
 */

public class WelcomeView extends ConstraintLayout {
    @BindView(R.id.hello)
    TextView hello;
    
    public WelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}



