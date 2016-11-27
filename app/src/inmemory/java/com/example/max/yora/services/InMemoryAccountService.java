package com.example.max.yora.services;

import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

public class InMemoryAccountService {
    private YoraApplication application;

    public InMemoryAccountService(YoraApplication application) {
        this.application = application;
        application.getBus().register(this);

    }

    @Subscribe
    public void updateProfile(Account.UpdateProfileRequest request) {
        Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();
        application.getBus().post(response);
    }
}
