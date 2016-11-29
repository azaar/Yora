package com.example.max.yora.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.yora.R;
import com.example.max.yora.activities.BaseActivity;
import com.example.max.yora.activities.ContactsActivity;
import com.example.max.yora.activities.MainActivity;
import com.example.max.yora.activities.ProfileActivity;
import com.example.max.yora.activities.SentMessagesActivity;
import com.example.max.yora.infrastructure.User;
import com.example.max.yora.services.Account;
import com.squareup.otto.Subscribe;


public class MainNavDrawer extends NavDrawer {
    private final TextView displayNameText;
    private final ImageView avatarImage;

    public MainNavDrawer(final BaseActivity activity) {
        super(activity);

        addItem(new ActivityNavDrawerItem(
                MainActivity.class, "Inbox", null, R.drawable.ic_email, R.id.include_main_nav_drawer_topItems));

        addItem(new ActivityNavDrawerItem(
                SentMessagesActivity.class, "Sent Messages", null, R.drawable.ic_send, R.id.include_main_nav_drawer_topItems));

        addItem(new ActivityNavDrawerItem(
                ContactsActivity.class, "Contacts",null, R.drawable.ic_group, R.id.include_main_nav_drawer_topItems));

        addItem(new ActivityNavDrawerItem(
                ProfileActivity.class, "Profile", null, R.drawable.ic_person, R.id.include_main_nav_drawer_topItems));

        addItem(new BasicNavDrawerItem(
                "Logout", null, R.drawable.ic_backspace, R.id.include_main_nav_drawer_bottomItems) {
            @Override
            public void onClick(View v) {
                activity.getYoraApplication().getAuth().logout();
                Toast.makeText(activity, "You have logged out",Toast.LENGTH_SHORT).show();
            }
        });

        displayNameText = (TextView) navDrawerView.findViewById(R.id.include_main_nav_drawer_displayName);
        avatarImage = (ImageView) navDrawerView.findViewById(R.id.include_main_nav_drawer_avatar);

        User loggedInUser = activity.getYoraApplication().getAuth().getUser();
        displayNameText.setText(loggedInUser.getDisplayName());

        // TODO: change avatarImage to avatarUrl from loggedInUser

    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event) {
        // TODO: update avatar URL
        displayNameText.setText(event.User.getDisplayName());
    }
}
