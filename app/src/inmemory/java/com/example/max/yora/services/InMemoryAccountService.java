package com.example.max.yora.services;

import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

public class InMemoryAccountService extends BaseInMemoryService {


    public InMemoryAccountService(YoraApplication application) {
        super(application);
    }

    @Subscribe
    public void updateProfile(Account.UpdateProfileRequest request) {
        Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();
        if (request.DisplayName.equals("test-error")) {
            response.setPropertyError("displayName", "You may not be named test-error!");
        }
        postDelayed(response, 4000);
    }

    @Subscribe
    public void updateAvatar(Account.ChangeAvatarRequest request) {
        postDelayed(new Account.ChangeAvatarResponse());

    }

    @Subscribe
    public void changePassword(Account.ChangePasswordRequest request) {

        Account.ChangePasswordResponse response = new Account.ChangePasswordResponse();

        if (!request.NewPassword.equals(request.ConfirmNewPassword)) {
            response.setPropertyError("confirmNewPassword", "Passwords must match");
        }
        if (request.NewPassword.length() < 3) {
            response.setPropertyError("newPassword", "Password must be longer than 3 characters");
        }

        postDelayed(response);
    }
}
