package com.example.max.yora.activities;

import android.os.Bundle;

import com.example.max.yora.R;
import com.example.max.yora.views.MainNavDrawer;

public class SentMessagesActivity extends BaseAuthenticatedActivity {
    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_sent_messages);
        setNavDrawer(new MainNavDrawer(this));

    }
}
