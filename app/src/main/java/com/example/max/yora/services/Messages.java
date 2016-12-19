package com.example.max.yora.services;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.max.yora.infrastructure.ServiceResponse;
import com.example.max.yora.services.entities.Message;
import com.example.max.yora.services.entities.UserDetails;

import java.util.List;

public final class Messages {
    private Messages() {
    }

    public static class DeleteMessageRequest {
        public int MessageId;

        public DeleteMessageRequest(int messageId) {
            MessageId = messageId;
        }
    }

    public static class DeleteMessageResponse extends ServiceResponse {
        public int MessageId;
    }

    public static class SearchMessagesRequest {
        public int FromContactId;
        public boolean IncludeSentMessages;
        public boolean IncludeReceivedMessages;

        public SearchMessagesRequest(int fromContactId, boolean includeSentMessages, boolean includeReceivedMessages) {
            FromContactId = fromContactId;
            IncludeSentMessages = includeSentMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }

        public SearchMessagesRequest(boolean includeSentMessages, boolean includeReceivedMessages) {
            IncludeSentMessages = includeSentMessages;
            IncludeReceivedMessages = includeReceivedMessages;
        }
    }

    public static class SearchMessagesResponse extends ServiceResponse {
        public List<Message> Messages;
    }

    public static class SendMessageRequest implements Parcelable {
        private UserDetails recipient;
        private Uri imagePath;
        private String message;

        public SendMessageRequest() {

        }

        private SendMessageRequest(Parcel in) {
            recipient = in.readParcelable(UserDetails.class.getClassLoader());
            imagePath = in.readParcelable(Uri.class.getClassLoader());
            message = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int i) {
            out.writeParcelable(recipient, 0);
            out.writeParcelable(imagePath, 0);
            out.writeString(message);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public UserDetails getRecipient() {
            return recipient;
        }

        public void setRecipient(UserDetails recipient) {
            this.recipient = recipient;
        }

        public Uri getImagePath() {
            return imagePath;
        }

        public void setImagePath(Uri imagePath) {
            this.imagePath = imagePath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static final Creator<SendMessageRequest> CREATOR = new Creator<SendMessageRequest>() {
            @Override
            public SendMessageRequest createFromParcel(Parcel source) {
                return new SendMessageRequest(source);
            }

            @Override
            public SendMessageRequest[] newArray(int size) {
                return new SendMessageRequest[size];
            }
        };
    }

    public static class SendMessageResponse extends ServiceResponse {

    }
}
