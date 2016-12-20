package com.example.max.yora.services;

import com.example.max.yora.infrastructure.YoraApplication;
import com.example.max.yora.services.entities.Message;
import com.example.max.yora.services.entities.UserDetails;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class InMemoryMessagesService extends BaseInMemoryService {
    public InMemoryMessagesService(YoraApplication application) {
        super(application);
    }

    @Subscribe
    public void deleteMessage (Messages.DeleteMessageRequest request) {
        Messages.DeleteMessageResponse response = new Messages.DeleteMessageResponse();
        response.MessageId = request.MessageId;
        postDelayed(response);
    }

    @Subscribe
    public void searchMessages(Messages.SearchMessagesRequest request) {
        Messages.SearchMessagesResponse response = new Messages.SearchMessagesResponse();
        response.Messages = new ArrayList<>();
        ArrayList<UserDetails> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add( new UserDetails(
                    i,
                    true,
                    "User " + i,
                    "User" + i,
                    "http://gravatar.com/avatar/" + i + "?d=identicon"
            ));
        }

        Random random = new Random();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,  -100);

        for (int i = 0; i < 100; i++) {
            boolean isFromUs;

            if (request.IncludeReceivedMessages && request.IncludeSentMessages) {
                isFromUs = random.nextBoolean();
            } else {
                isFromUs = !request.IncludeReceivedMessages;
            }

            date.set(Calendar.MINUTE, random.nextInt(60 * 24));
            response.Messages.add(new Message(
                    i,
                    (Calendar) date.clone(),
                    "Short message " + i,
                    "Long message " + i,
                    "",
                    users.get(random.nextInt(users.size())),
                    isFromUs,
                    random.nextBoolean()
            ));
        }

        postDelayed(response, 2000);
    }

    @Subscribe
    public void sendMessage(Messages.SendMessageRequest request) {
        Messages.SendMessageResponse response =  new Messages.SendMessageResponse();

        if (request.getMessage().equals("error")) {
            response.setOperationError("Something bad happened");
        } else if (request.getMessage().equals("error-message")) {
            response.setPropertyError("message", "Invalid message");
        }

        postDelayed(response, 1500, 3000);
    }

    @Subscribe
    public void markMessageAsRead(Messages.MarkMessageAsReadRequest request) {
        postDelayed(new Messages.MarkMessageAsReadResponse());
    }

    @Subscribe
    public void getMessageDetails(Messages.GetMessageDetailsRequest request) {
        Messages.GetMessageDetailsResponse response = new Messages.GetMessageDetailsResponse();

        response.Message = new Message(
                1,
                Calendar.getInstance(),
                "Short message",
                "Long message",
                null,
                new UserDetails(1, true, "Display Name", "Username", "http://gravatar.com/avatar/2?d=identicon"),
                false,
                false);

        postDelayed(response);
    }
}
