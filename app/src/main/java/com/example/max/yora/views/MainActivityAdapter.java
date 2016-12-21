package com.example.max.yora.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.max.yora.activities.BaseActivity;
import com.example.max.yora.services.entities.ContactRequest;
import com.example.max.yora.services.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter {
    private List<Message> messages;
    private List<ContactRequest> contactRequests;
    private BaseActivity activity;
    private LayoutInflater inflater;
    private MainActivityListener listener;

    public MainActivityAdapter(BaseActivity activity, MainActivityListener listener) {
        this.activity = activity;
        this.listener = listener;
        inflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        contactRequests = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<ContactRequest> getContactRequests() {
        return contactRequests;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface MainActivityListener {
        void onMessageClicked(Message message);
        void onContactRequestClicked(ContactRequest request, int position);
    }
}
