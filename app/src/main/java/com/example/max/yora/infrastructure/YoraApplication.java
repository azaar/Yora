package com.example.max.yora.infrastructure;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;

public class YoraApplication extends Application {
    private Auth auth;
    private EventBus bus;

    public YoraApplication() {
        bus = EventBus.getDefault();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        auth = new Auth(this);
    }

    public Auth getAuth() {
        return auth;
    }

    public EventBus getBus() {
        return bus;
    }
}
