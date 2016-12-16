package com.example.max.yora.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.max.yora.R;
import com.example.max.yora.services.Contacts;
import com.example.max.yora.services.entities.UserDetails;
import com.example.max.yora.views.UserDetailsAdapter;
import com.squareup.otto.Subscribe;

public class SelectContactActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener {
    public static final String RESULT_CONTACT = "RESULT_CONTACT";
    public static final int REQUEST_ADD_FRIEND = 1;

    private UserDetailsAdapter adapter;

    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_select_contact);
        getSupportActionBar().setTitle(R.string.select_contact);

        adapter = new UserDetailsAdapter(this);
        ListView listView = (ListView) findViewById(R.id.activity_select_contact_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Contacts.GetContactsRequest(true));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        UserDetails userDetails = adapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(RESULT_CONTACT, userDetails);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Subscribe
    public void onContactsReceived(Contacts.GetContactsResponse response) {
        response.showErrorToast(this);

        adapter.clear();
        adapter.addAll(response.Contacts);
        adapter.notifyDataSetChanged();
    }
}
