package com.example.max.yora.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.example.max.yora.infrastructure.YoraApplication;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseDialogFragment extends DialogFragment {
    protected YoraApplication application;
    protected EventBus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
