package com.example.max.yora.services;

import com.example.max.yora.infrastructure.Auth;
import com.example.max.yora.infrastructure.RetrofitCallback;
import com.example.max.yora.infrastructure.RetrofitCallbackPost;
import com.example.max.yora.infrastructure.User;
import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

import java.io.File;

import retrofit.mime.TypedFile;

public class LiveAccountService extends BaseLiveService {
    private final Auth auth;

    public LiveAccountService(YoraApplication application, YoraWebService api) {
        super(application, api);
        auth = application.getAuth();
    }

    @Subscribe
    public void register(Account.RegisterRequest request) {
        api.register(request, new RetrofitCallback<Account.RegisterResponse>(Account.RegisterResponse.class) {
            @Override
            protected void onResponse(Account.RegisterResponse registerResponse) {
                if (registerResponse.didSucceed()) {
                    loginUser(registerResponse);
                }

                bus.post(registerResponse);
            }
        });
    }

    @Subscribe
    public void loginWithUsername(final Account.LoginWithUsernameRequest request) {
        api.login(
                request.Username,
                request.Password,
                "android",
                "password",
                new RetrofitCallback<YoraWebService.LoginResponse>(YoraWebService.LoginResponse.class) {
                    @Override
                    protected void onResponse(YoraWebService.LoginResponse loginResponse) {
                        if (!loginResponse.didSucceed()) {
                            Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                            response.setPropertyError("userName", loginResponse.ErrorDescription);
                            bus.post(response);
                            return;
                        }

                        auth.setAuthToken(loginResponse.Token);
                        api.getAccount(new RetrofitCallback<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class) {

                            @Override
                            protected void onResponse(Account.LoginWithLocalTokenResponse loginWithLocalTokenResponse) {
                                if (!loginWithLocalTokenResponse.didSucceed()) {
                                    Account.LoginWithUsernameResponse response = new Account.LoginWithUsernameResponse();
                                    response.setOperationError(loginWithLocalTokenResponse.getOperationError());
                                    bus.post(response);
                                    return;
                                }

                                loginUser(loginWithLocalTokenResponse);
                                bus.post(new Account.LoginWithUsernameResponse());
                            }
                        });
                    }
                }
        );
    }

    @Subscribe
    public void loginWithLocalToken(final Account.LoginWithLocalTokenRequest request) {
        api.getAccount(new RetrofitCallbackPost<Account.LoginWithLocalTokenResponse>(Account.LoginWithLocalTokenResponse.class, bus) {
            @Override
            protected void onResponse(Account.LoginWithLocalTokenResponse response) {
                loginUser(response);
                super.onResponse(response);
            }
        });
    }

    @Subscribe
    public void updateProfile(final Account.UpdateProfileRequest request) {
        api.updateProfile(request, new RetrofitCallbackPost<Account.UpdateProfileResponse>(Account.UpdateProfileResponse.class, bus) {
            @Override
            protected void onResponse(Account.UpdateProfileResponse response) {
                User user = auth.getUser();
                user.setDisplayName(response.DisplayName);
                user.setEmail(response.Email);
                super.onResponse(response);
                bus.post(new Account.UserDetailsUpdatedEvent(user));
            }
        });
    }

    @Subscribe
    public void updateAvatar(final Account.ChangeAvatarRequest request) {
        api.updateAvatar(
                new TypedFile("image/jpeg", new File(request.NewAvatarUri.getPath())),
                new RetrofitCallbackPost<Account.ChangeAvatarResponse>(Account.ChangeAvatarResponse.class, bus) {
                    @Override
                    protected void onResponse(Account.ChangeAvatarResponse response) {
                        User user = auth.getUser();
                        user.setAvatarUrl(response.AvatarUrl);
                        super.onResponse(response);
                        bus.post(new Account.UserDetailsUpdatedEvent(user));
                    }
                }
        );
    }

    @Subscribe
    public void changePassword(final Account.ChangePasswordRequest request) {
        api.updatePassword(request, new RetrofitCallbackPost<Account.ChangePasswordResponse>(Account.ChangePasswordResponse.class, bus) {
            @Override
            protected void onResponse(Account.ChangePasswordResponse response) {
                if (response.didSucceed()) {
                    auth.getUser().setHasPassword(true);
                }
                super.onResponse(response);
            }
        });
    }

    @Subscribe
    public void loginWithExternalToken(Account.LoginWithExternalTokenRequest request) {
        api.loginWithExternalToken(request, new RetrofitCallbackPost<Account.LoginWithExternalTokenResponse>(Account.LoginWithExternalTokenResponse.class, bus) {
            @Override
            protected void onResponse(Account.LoginWithExternalTokenResponse response) {
                if (response.didSucceed()) {
                    loginUser(response);
                }
                super.onResponse(response);
            }
        });
    }

    @Subscribe
    public void registerWithExternalToken(Account.RegisterWithExternalTokenRequest request) {
        api.registerExternal(request, new RetrofitCallbackPost<Account.RegisterWithExternalTokenResponse>(Account.RegisterWithExternalTokenResponse.class, bus) {
            @Override
            protected void onResponse(Account.RegisterWithExternalTokenResponse response) {
                if (response.didSucceed()) {
                    loginUser(response);
                }
                super.onResponse(response);
            }
        });
    }

    @Subscribe
    public void registerGcm(Account.UpdateGcmRegistrationRequest request) {
        api.updateGcmRegistration(request, new RetrofitCallbackPost<>(Account.UpdateGcmRegistrationResponse.class, bus));
    }


    private void loginUser(Account.UserResponse response) {
        if (response.AuthToken != null && !response.AuthToken.isEmpty()) {
            auth.setAuthToken(response.AuthToken);
        }

        User user = auth.getUser();
        user.setId(response.Id);
        user.setDisplayName(response.DisplayName);
        user.setUserName(response.UserName);
        user.setEmail(response.Email);
        user.setAvatarUrl(response.AvatarUrl);
        user.setHasPassword(response.HasPassword);
        user.setLoggedIn(true);

        bus.post(new Account.UserDetailsUpdatedEvent(user));
    }
}
