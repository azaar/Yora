package com.example.max.yora.views;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.yora.R;
import com.example.max.yora.activities.BaseActivity;
import com.example.max.yora.services.entities.ContactRequest;
import com.squareup.picasso.Picasso;

public class ContactRequestsAdapter extends ArrayAdapter<ContactRequest> {
    private LayoutInflater inflater;

    public ContactRequestsAdapter(BaseActivity activity) {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactRequest request = getItem(position);
        ViewHolder view;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_contact_request, parent, false);
            view = new ViewHolder(convertView);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.DisplayName.setText(request.getUser().getUserName());
        Picasso.with(getContext()).load(request.getUser().getAvatarUrl()).into(view.Avatar);

        String createdAt = DateUtils.formatDateTime(
                getContext(),
                request.getCreatedAt().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);


        if (request.isFromUs()) {
            view.CreatedAt.setText(getContext().getResources().getString(R.string.sent_at) + createdAt);
        } else {
            view.CreatedAt.setText(getContext().getResources().getString(R.string.received) + createdAt);
        }

        return convertView;


    }

    private class ViewHolder {
        public TextView DisplayName;
        public TextView CreatedAt;
        public ImageView Avatar;

        public ViewHolder(View view) {
            DisplayName = (TextView) view.findViewById(R.id.list_item_contact_request_displayName);
            CreatedAt = (TextView) view.findViewById(R.id.list_item_contact_request_createdAt);
            Avatar = (ImageView) view.findViewById(R.id.list_item_contact_request_avatar);
        }
    }
}
