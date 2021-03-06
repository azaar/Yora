package com.example.max.yora.services;

import com.example.max.yora.infrastructure.Auth;
import com.example.max.yora.infrastructure.User;
import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

public class InMemoryAccountService extends BaseInMemoryService {


    public InMemoryAccountService(YoraApplication application) {
        super(application);
    }

    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request) {
        final Account.UpdateProfileResponse response = new Account.UpdateProfileResponse();
        if (request.DisplayName.equals("test-error")) {
            response.setPropertyError("displayName", "You may not be named test-error!");
        }

        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                User user = application.getAuth().getUser();
                user.setDisplayName(request.DisplayName);
                user.setEmail(request.Email);

                bus.post(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        }, 2000, 3000);

    }

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                User user = application.getAuth().getUser();
                user.setAvatarUrl(request.NewAvatarUri.toString());

                bus.post(new Account.ChangeAvatarResponse());
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        }, 4000, 5000);
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

    @Subscribe
    public void loginWithUsername(final Account.LoginWithUsernameRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();

                if (request.Username.equals("testname")) {
                    response.setPropertyError("userName", "Invalid username or password");
                }

                loginUser(new Account.UserResponse());
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void loginWithExternalToken(Account.LoginWithExternalTokenRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithExternalTokenResponse response = new Account.LoginWithExternalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void register(Account.RegisterRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.RegisterResponse response = new Account.RegisterResponse();
                loginUser(response);
                bus.post(response);
            }
        });

    }

    @Subscribe
    public void externalRegister(Account.RegisterWithExternalTokenRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.RegisterResponse response = new Account.RegisterResponse();
                loginUser(response);
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void loginWithLocaltoken(Account.LoginWithLocalTokenRequest request) {
        invokeDelayed(new Runnable() {
            @Override
            public void run() {
                Account.LoginWithLocalTokenResponse response = new Account.LoginWithLocalTokenResponse();
                loginUser(response);
                bus.post(response);
            }
        });
    }

    @Subscribe
    public void updateGcmRegistration(Account.UpdateGcmRegistrationRequest request) {
        postDelayed(new Account.UpdateGcmRegistrationResponse());
    }

    @Subscribe
    public void loginUser(Account.UserResponse response) {
        Auth auth = application.getAuth();
        User user = auth.getUser();

        user.setDisplayName("Lorem_Ipsum");
        user.setUserName("Lorem Ipsum");
        user.setEmail("ipsum@example.com");
        user.setAvatarUrl("http://www.gravatar.com/avatar/1?d=identicon");
        user.setLoggedIn(true);
        user.setId(123);

        bus.post(new Account.UserDetailsUpdatedEvent(user));


        auth.setAuthToken("fakeauthtoken");

        response.DisplayName = user.getDisplayName();
        response.UserName = user.getUserName();
        response.Email = user.getEmail();
        response.AvatarUrl = user.getAvatarUrl();
        response.Id = user.getId();
        response.AuthToken = auth.getAuthToken();
    }
}
