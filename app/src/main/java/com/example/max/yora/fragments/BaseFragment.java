package com.example.max.yora.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.example.max.yora.infrastructure.YoraApplication;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment{
    protected YoraApplication application;
    protected EventBus bus;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        application = (YoraApplication) getActivity().getApplication();

        bus = application.getBus();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
