package com.example.max.yora.activities;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.yora.R;
import com.example.max.yora.services.Events;
import com.example.max.yora.services.Messages;
import com.example.max.yora.services.entities.Message;
import com.example.max.yora.services.entities.UserDetails;
import com.squareup.otto.Subscribe;

import java.util.GregorianCalendar;

public class MessageActivity extends BaseAuthenticatedActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String RESULT_EXTRA_MESSAGE_ID = "RESULT_EXTRA_MESSAGE_ID";
    public static final String EXTRA_MESSAGE_ID = "EXTRA_MESSAGE_ID";
    public static final int REQUEST_IMAGE_DELETED = 100;

    private static final String STATE_MESSAGE = "STATE_MESSAGE";

    private View drawer;
    private TextView translateButton;
    private boolean isOpen;
    private int translateOffset;
    private AnimatorSet currentAnimation;
    private Message currentMessage;
    private View progressFrame;
    private TextView shortMessage;
    private TextView longMessage;

    @Override
    protected void onYoraCreate(Bundle savedState) {
        setContentView(R.layout.activity_message);

        drawer = findViewById(R.id.activity_message_drawer);
        translateButton = (TextView) findViewById(R.id.activity_message_translate);
        progressFrame = findViewById(R.id.activity_message_progressFrame);
        shortMessage = (TextView) findViewById(R.id.activity_message_shortMessage);
        longMessage = (TextView) findViewById(R.id.activity_message_longMessage);

        drawer.setOnClickListener(this);

        progressFrame.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMessage(RESULT_OK);

            }
        });

        Message message = null;

        if (savedState != null) {
            message = savedState.getParcelable(STATE_MESSAGE);
        }

        if (message == null) {
            message = getIntent().getParcelableExtra(EXTRA_MESSAGE);
            if (message == null) {
                int id = getIntent().getIntExtra(EXTRA_MESSAGE_ID, -1);
                if (id != -1) {
                    progressFrame.setVisibility(View.VISIBLE);
                    bus.post(new Messages.GetMessageDetailsRequest(id));
                } else {
                    // Dummy message for testing
                    message = new Message(
                            1,
                            new GregorianCalendar(),
                            getString(R.string.short_message),
                            getString(R.string.long_message),
                            null,
                            new UserDetails(1, true, "Person", "person", ""),
                            false,
                            false);
                }
            }
        }

        if (message != null) {
            showMessage(message);
        }
    }

    @Subscribe
    public void onMessageDetailsLoaded(final Messages.GetMessageDetailsResponse response) {
        scheduler.invokeOnResume(Messages.GetMessageDetailsResponse.class, new Runnable() {
            @Override
            public void run() {
                progressFrame.setVisibility(View.GONE);

                if (!response.didSucceed()) {
                    response.showErrorToast(MessageActivity.this);
                    return;
                }

                showMessage(response.Message);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MESSAGE, currentMessage);
    }

    private void showMessage(Message message) {
        currentMessage = message;

        String createdAt = DateUtils.formatDateTime(
                this,
                message.getCreatedAt().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);

        if (message.isFromUs()) {
            getSupportActionBar().setTitle(getString(R.string.sent_at) + createdAt);
        } else {
            getSupportActionBar().setTitle(getString(R.string.received_at) + createdAt);
        }

        longMessage.setText(message.getLongMessage());
        shortMessage.setText(message.getShortMessage());

        if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
            ImageView image = (ImageView) findViewById(R.id.activity_message_image);
            application.getAuthedPicasso().load(message.getImageUrl()).into(image);
        }

        invalidateOptionsMenu();

        Intent defaultResult = new Intent();
        defaultResult.putExtra(RESULT_EXTRA_MESSAGE_ID, message.getId());
        setResult(RESULT_OK, defaultResult);

        if (!message.isRead()) {
            bus.post(new Messages.MarkMessageAsReadRequest(message.getId()));
        }
    }

    private void closeMessage(int resultCode) {
        Intent data = new Intent();
        data.putExtra(RESULT_EXTRA_MESSAGE_ID, currentMessage.getId());
        setResult(resultCode, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_message_menuContact) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra(ContactActivity.EXTRA_USER_DETAILS, currentMessage.getOtherUser());
            startActivity(intent);
            return true;

        } else if (id == R.id.activity_message_menuReply) {
            Intent intent = new Intent(this, NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.EXTRA_CONTACT, currentMessage.getOtherUser());
            startActivity(intent);

        } else if (id == R.id.activity_message_menuDelete) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.delete_message_questionmark)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            bus.post(new Messages.DeleteMessageRequest(currentMessage.getId()));
                            closeMessage(REQUEST_IMAGE_DELETED);
                        }
                    })
                    .setCancelable(false)
                    .setNeutralButton(R.string.cancel, null)
                    .create();

            dialog.show();

        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.activity_message_menuReply).setVisible(currentMessage != null && !currentMessage.isFromUs());
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        translateOffset = drawer.getHeight() - translateButton.getHeight();
        drawer.setTranslationY(translateOffset);

    }

    @Override
    public void onClick(View view) {
        isOpen = !isOpen;

        if (currentAnimation != null) {
            currentAnimation.cancel();
        }

        int currentBackgroundColor = ((ColorDrawable) drawer.getBackground()).getColor();
        int translationY, color;

        if (isOpen) {
            translationY = 0;
            color = getResources().getColor(R.color.blue_transparent_2);
            translateButton.setText(R.string.close);
        } else {
            translationY = translateOffset;
            color = getResources().getColor(R.color.blue_transparent);
            translateButton.setText(R.string.translate);
        }

        ObjectAnimator translateAnimator = ObjectAnimator
                .ofFloat(drawer, "translationY", translationY)
                .setDuration(100);

        ObjectAnimator colorAnimator = ObjectAnimator
                .ofInt(drawer, "backgroundColor", currentBackgroundColor, color)
                .setDuration(100);

        colorAnimator.setEvaluator(new ArgbEvaluator());

        currentAnimation = new AnimatorSet();
        currentAnimation.setDuration(300);
        currentAnimation.play(translateAnimator).with(colorAnimator);
        currentAnimation.start();
    }

    @Subscribe
    public void onNotification(Events.OnNotificationReceivedEvent event) {
        if (currentMessage == null) {
            return;
        }

        if (event.OperationType == Events.OPERATION_DELETED &&
                event.EntityType == Events.ENTITY_MESSAGE &&
                event.EntityId == currentMessage.getId()) {

            closeMessage(REQUEST_IMAGE_DELETED);
        }
    }
}
