package com.example.max.yora.activities;


import android.os.Bundle;

import com.example.max.yora.R;

public class MainActivity extends BaseAuthenticatedActivity {
    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");

    }
}
