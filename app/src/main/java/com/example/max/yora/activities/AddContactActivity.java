package com.example.max.yora.activities;

import android.os.Bundle;

import com.example.max.yora.R;

public class AddContactActivity extends BaseAuthenticatedActivity {
    public static final String RESULT_CONTACT = "RESULT_CONTACT";

    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_add_contact);
    }
}
