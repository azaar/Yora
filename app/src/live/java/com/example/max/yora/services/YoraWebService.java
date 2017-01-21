package com.example.max.yora.services;

import com.example.max.yora.infrastructure.ServiceResponse;
import com.google.gson.annotations.SerializedName;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

public interface YoraWebService {

    // --------------------------------------------------------------------------------------------
    // Account

    @POST("/token")
    @FormUrlEncoded
    void login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("grant_type") String grantType,
            Callback<LoginResponse> callback);

    @POST("/api/v1/account/external/token")
    void loginWithExternalToken(@Body Account.LoginWithExternalTokenRequest request,
                                Callback<Account.LoginWithExternalTokenResponse> callback);

    @POST("/api/v1/account")
    void register(@Body Account.RegisterRequest request,
                  Callback<Account.RegisterResponse> callback);

    @POST("/api/v1/account/external")
    void registerExternal(@Body Account.RegisterWithExternalTokenRequest request,
                          Callback<Account.RegisterWithExternalTokenResponse> callback);

    @GET("/api/v1/account")
    void getAccount(Callback<Account.LoginWithLocalTokenResponse> callback);

    @PUT("/api/v1/account")
    void updateProfile(@Body Account.UpdateProfileRequest request,
                       Callback<Account.UpdateProfileResponse> callback);

    @Multipart
    @PUT("/api/v1/account/avatar")
    void updateAvatar(@Part("avatar")TypedFile avatar,
                      Callback<Account.ChangeAvatarResponse> callback);

    @PUT("/api/v1/account/password")
    void updatePassword(@Body Account.ChangePasswordRequest request,
                        Callback<Account.ChangePasswordResponse> callback);

    @PUT("/api/v1/account/gcm-registration")
    void updateGcmRegistration(@Body Account.UpdateGcmRegistrationRequest request,
                               Callback<Account.UpdateGcmRegistrationResponse> callback);



    // --------------------------------------------------------------------------------------------
    // DTOs

    public class LoginResponse extends ServiceResponse {
        @SerializedName(".expires")
        public String Expires;

        @SerializedName(".issued")
        public String Issued;

        @SerializedName("access_token")
        public String Token;

        @SerializedName("error")
        public String Error;

        @SerializedName("error_description")
        public String ErrorDescription;
    }
}
