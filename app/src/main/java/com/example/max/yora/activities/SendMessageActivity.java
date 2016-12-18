package com.example.max.yora.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.max.yora.R;
import com.squareup.picasso.Picasso;

public class SendMessageActivity extends BaseAuthenticatedActivity {
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";
    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    public static final int MAX_IMAGE_HEIGHT = 1280;


    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_send_message);

        Uri imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_PATH);

        if (imageUri != null) {
            ImageView image = (ImageView) findViewById(R.id.activity_send_message_image);
            Picasso.with(this).load(imageUri).into(image);
        }

    }
}
