package com.example.max.yora.activities;

import android.os.Bundle;

import com.example.max.yora.R;
import com.example.max.yora.views.MainNavDrawer;

public class ContactsActivity extends BaseAuthenticatedActivity {
    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_contacts);
        setNavDrawer(new MainNavDrawer(this));
    }
}
