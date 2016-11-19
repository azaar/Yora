package com.example.max.yora.views;

import android.view.View;
import android.widget.Toast;

import com.example.max.yora.R;
import com.example.max.yora.activities.BaseActivity;
import com.example.max.yora.activities.MainActivity;

public class MainNavDrawer extends NavDrawer {
    public MainNavDrawer(final BaseActivity activity) {
        super(activity);

        addItem(new ActivityNavDrawerItem(MainActivity.class, "Inbox", null, R.drawable.ic_email, 0));

        addItem(new BasicNavDrawerItem("Logout", null, R.drawable.ic_backspace, 0) {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "You have logged out",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
