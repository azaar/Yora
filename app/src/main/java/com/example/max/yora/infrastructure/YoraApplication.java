package com.example.max.yora.infrastructure;

import android.app.Application;
import android.net.Uri;

import com.example.max.yora.BuildConfig;
import com.example.max.yora.services.Module;
import com.squareup.otto.Bus;

public class YoraApplication extends Application {
    public static final Uri API_ENDPOINT = Uri.parse("http://yora-playground.3dbuzz.com");
    public static final String STUDENT_TOKEN = BuildConfig.YORA_API_TOKEN;

    private Auth auth;
    private Bus bus;

    public YoraApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        auth = new Auth(this);
        Module.register(this);
    }

    public Auth getAuth() {
        return auth;
    }

    public Bus getBus() {
        return bus;
    }
}
