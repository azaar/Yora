package com.example.max.yora.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.max.yora.infrastructure.YoraApplication;

public abstract class BaseActivity extends AppCompatActivity{
    protected YoraApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (YoraApplication) getApplication();
    }
}
