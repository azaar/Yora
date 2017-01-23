package com.example.max.yora.services;

import com.example.max.yora.infrastructure.RetrofitCallbackPost;
import com.example.max.yora.infrastructure.YoraApplication;
import com.squareup.otto.Subscribe;

import java.io.File;

import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class LiveMessagesService extends BaseLiveService {
    public LiveMessagesService(YoraApplication application, YoraWebService api) {
        super(application, api);
    }

    @Subscribe
    public void sendMessage(Messages.SendMessageRequest request) {
        api.sendMessage(
                new TypedString(request.getMessage()),
                new TypedString(Integer.toString(request.getRecipient().getId())),
                new TypedFile("image/jpeg", new File(request.getImagePath().getPath())),
                new RetrofitCallbackPost<>(Messages.SendMessageResponse.class, bus));
    }

    @Subscribe
    public void searchMessages(Messages.SearchMessagesRequest request) {
        if (request.FromContactId != -1) {
            api.searchMessages(
                    request.FromContactId,
                    request.IncludeSentMessages,
                    request.IncludeReceivedMessages,
                    new RetrofitCallbackPost<>(Messages.SearchMessagesResponse.class, bus));

        } else {
            api.searchMessages(
                    request.IncludeSentMessages,
                    request.IncludeReceivedMessages,
                    new RetrofitCallbackPost<>(Messages.SearchMessagesResponse.class, bus));
        }
    }

    @Subscribe
    public void deleteMessage(Messages.DeleteMessageRequest request) {
        api.deleteMessage(
                request.MessageId,
                new RetrofitCallbackPost<>(Messages.DeleteMessageResponse.class, bus));
    }

    @Subscribe
    public void markMessageAsRead(Messages.MarkMessageAsReadRequest request) {
        api.markMessageAsRead(
                request.MessageId,
                new RetrofitCallbackPost<>(Messages.MarkMessageAsReadResponse.class, bus));
    }

    @Subscribe
    public void getMessageDetails(Messages.GetMessageDetailsRequest request) {
        api.getMessageDetails(
                request.Id,
                new RetrofitCallbackPost<>(Messages.GetMessageDetailsResponse.class, bus));
    }
}
