package com.example.max.yora.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.max.yora.R;
import com.example.max.yora.services.Contacts;
import com.example.max.yora.services.Messages;
import com.example.max.yora.services.entities.ContactRequest;
import com.example.max.yora.services.entities.Message;
import com.example.max.yora.views.MainActivityAdapter;
import com.example.max.yora.views.MainNavDrawer;
import com.squareup.otto.Subscribe;

import java.util.List;

public class MainActivity extends BaseAuthenticatedActivity implements View.OnClickListener, MainActivityAdapter.MainActivityListener {
    private MainActivityAdapter adapter;
    private List<Message> messages;
    private  List<ContactRequest> contactRequests;


    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");
        setNavDrawer(new MainNavDrawer(this));

        findViewById(R.id.activity_main_newMessageButton).setOnClickListener(this);

        adapter = new MainActivityAdapter(this, this);
        messages = adapter.getMessages();
        contactRequests = adapter.getContactRequests();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduler.invokeEveryMilliseconds(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 1000 * 60 * 3);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_main_newMessageButton) {
            startActivity(new Intent(this, NewMessageActivity.class));
        }
    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        bus.post(new Messages.SearchMessagesRequest(false, true));
        bus.post(new Contacts.GetContactRequestsRequest(false));
    }

    @Subscribe
    public void onMessagesLoaded(final Messages.SearchMessagesResponse response) {
        scheduler.invokeOnResume(Messages.SearchMessagesResponse.class, new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);

                if (!response.didSucceed()) {
                    response.showErrorToast(MainActivity.this);
                    return;
                }
                messages.clear();
                messages.addAll(response.Messages);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void onContactRequestsLoaded(final Contacts.GetContactRequestsResponse response) {
        scheduler.invokeOnResume(Contacts.GetContactRequestsResponse.class, new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);

                if (!response.didSucceed()) {
                    response.showErrorToast(MainActivity.this);
                    return;
                }
                contactRequests.clear();
                contactRequests.addAll(response.Requests);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onMessageClicked(Message message) {

    }

    @Override
    public void onContactRequestClicked(ContactRequest request, int position) {

    }
}
