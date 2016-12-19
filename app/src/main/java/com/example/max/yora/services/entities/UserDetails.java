package com.example.max.yora.services.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetails implements Parcelable {
    private final int id;
    private final boolean isContact;
    private final String displayName;
    private final String userName;
    private final String avatarUrl;


    public UserDetails(int id, boolean isContact, String displayName, String userName, String avatarUrl) {
        this.id = id;
        this.isContact = isContact;
        this.displayName = displayName;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    private UserDetails(Parcel in) {
        id = in.readInt();
        isContact = in.readByte() == 1;
        displayName = in.readString();
        userName = in.readString();
        avatarUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeByte((byte) (isContact ? 1 : 0));
        out.writeString(displayName);
        out.writeString(userName);
        out.writeString(avatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public boolean isContact() {
        return isContact;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }


    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };
}
