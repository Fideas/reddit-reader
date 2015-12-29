package com.nicolascarrasco.www.redditreader;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nicolascarrasco.www.redditreader.client.RedditRestClient;
import com.nicolascarrasco.www.redditreader.data.RedditProvider;
import com.nicolascarrasco.www.redditreader.data.SubscriptionsColumns;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscribeActivity extends AppCompatActivity implements RedditRestClient.ResponseListener {

    private static final String PAGE_NOT_FOUND_ERROR = "404";
    private static final String TIMEOUT_ERROR = "0";
    private static final String REPEATED_VALUE_ERROR = "repeated-value-error";

    private static final String LOG_TAG = SubscribeActivity.class.getSimpleName();
    AddSubscriptionDialogFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab_add_subscription)
    public void launchAddSubscriptionDialog() {
        mDialogFragment = new AddSubscriptionDialogFragment();
        mDialogFragment.show(getSupportFragmentManager(), "");
    }

    private void validateSubredditName(String name) {
        //First, check if it is a subreddit
        RedditRestClient restClient = new RedditRestClient();
        restClient.getSubreddit(name, this);
    }

    @Override
    public void OnGetSubredditResponse(boolean isReddit, String content) {
        if (isReddit) {
            //Check if its already in the DB
            searchForDuplicate(content);
        } else {
            //Call the error setter function
            setErrorHint(content);
        }
    }

    private void searchForDuplicate(String name) {
        Cursor cursor = getContentResolver().query(
                RedditProvider.Subscriptions.withSubredditName(name),
                null,
                null,
                null,
                null);

        if (cursor == null || !cursor.moveToFirst()) {
            //The cursor is empty so everything is fine
            Uri insertUri = InsertSubscription(name);
            Log.i(LOG_TAG, insertUri.toString());
            mDialogFragment.getDialog().dismiss();

        } else {
            //The subreddit is already in the DB, call error setter function
            setErrorHint(REPEATED_VALUE_ERROR);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void setErrorHint(String errorCode) {
        EditText editText = ButterKnife.findById(
                mDialogFragment.getDialog(),
                R.id.subscription_dialog_edit_text);
        String errorMessage = null;
        switch (errorCode) {
            case PAGE_NOT_FOUND_ERROR:
                errorMessage = getString(R.string.subscribe_dialog_page_not_found_error);
                break;
            case TIMEOUT_ERROR:
                errorMessage = getString(R.string.subscribe_dialog_timeout_error);
                break;
            case REPEATED_VALUE_ERROR:
                errorMessage = getString(R.string.subscribe_dialog_repeated_value_error);
                break;
        }
        editText.setError(errorMessage);
        editText.requestFocus();
    }

    private Uri InsertSubscription(String name) {
        ContentValues values = new ContentValues();
        values.put(SubscriptionsColumns.SR_NAME, name);
        return getContentResolver().insert(RedditProvider.Subscriptions.CONTENT_URI, values);
    }

    public static class AddSubscriptionDialogFragment extends android.support.v4.app.DialogFragment {
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setView(R.layout.dialog_add_subscription);
            builder.setTitle(getString(R.string.subscribe_dialog_title));
            builder.setPositiveButton(getString(R.string.subscribe_dialog_positive_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.setNegativeButton(getString(R.string.subscribe_dialog_negative_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }

        @Override
        public void onStart() {
            super.onStart();
            final AlertDialog d = (AlertDialog) getDialog();
            if (d != null) {
                final EditText editText =
                        ButterKnife.findById(d, R.id.subscription_dialog_edit_text);

                Button positiveButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SubscribeActivity) getActivity())
                                .validateSubredditName(editText.getText().toString());
                    }
                });
            }
        }
    }
}
