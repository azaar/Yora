package com.example.max.yora.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.max.yora.R;
import com.example.max.yora.fragments.RegisterGcmFragment;
import com.example.max.yora.infrastructure.Auth;
import com.example.max.yora.services.Account;
import com.squareup.otto.Subscribe;

public class AuthenticationActivity extends BaseActivity implements RegisterGcmFragment.GcmRegistrationCallback {
    private Auth auth;

    public static final String EXTRA_RETURN_TO_ACTIVITY = "EXTRA_RETURN_TO_ACTIVITY";


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_authentication);

        auth = application.getAuth();

        if (!auth.hasAuthToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bus.post(new Account.LoginWithLocalTokenRequest(auth.getAuthToken()));
    }

    @Subscribe
    public void onLoginWithLocalToken(Account.LoginWithLocalTokenResponse response) {
        if (!response.didSucceed()) {
            Toast.makeText(this, "Please try to login again", Toast.LENGTH_SHORT).show();
            auth.setAuthToken(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        RegisterGcmFragment.get(this, false, getFragmentManager());
    }

    @Override
    public void gcmFinished() {
        Intent intent;
        String returnTo = getIntent().getStringExtra(EXTRA_RETURN_TO_ACTIVITY);

        if (returnTo != null) {

            try {
                intent = new Intent(this, Class.forName(returnTo));

            } catch (Exception ignored) {

                intent = new Intent(this, MainActivity.class);
            }

        } else {

            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
