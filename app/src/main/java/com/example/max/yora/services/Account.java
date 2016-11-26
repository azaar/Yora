package com.example.max.yora.services;

import android.net.Uri;

public final class Account {
    private Account() {
    }

    public static class ChangeAvatarRequest {
        public Uri NewAvatarUri;

        public ChangeAvatarRequest(Uri newAvatarUri) {
            NewAvatarUri = newAvatarUri;
        }
    }

    public static class ChangeAvatarResponse {

    }
}
