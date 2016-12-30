package com.example.max.yora.services;

import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Bus;

public abstract class BaseLiveService {
    protected final Bus bus;
    protected final YoraWebService api;
    protected final YoraApplication application;

    protected BaseLiveService(YoraApplication application, YoraWebService api) {
        this.application = application;
        this.api = api;
        bus = application.getBus();
        bus.register(this);
    }
}
