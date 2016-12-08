package com.example.max.yora.activities;

import android.os.Bundle;

import com.example.max.yora.R;

public class ContactActivity extends BaseAuthenticatedActivity {
    public static final String EXTRA_USER_DETAILS = "EXTRA_USER_DETAILS";

    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_contact);
    }
}
