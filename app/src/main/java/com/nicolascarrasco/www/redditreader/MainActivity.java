package com.nicolascarrasco.www.redditreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nicolascarrasco.www.redditreader.client.RedditRestClient;
import com.nicolascarrasco.www.redditreader.data.FetchPostTask;
import com.nicolascarrasco.www.redditreader.data.Post;
import com.nicolascarrasco.www.redditreader.data.RedditProvider;
import com.nicolascarrasco.www.redditreader.data.SubscriptionsColumns;

import org.json.JSONException;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements FetchPostTask.TaskDelegate {

    private static final String TOKEN_URL = "v1/access_token";
    private static final String DEVICE_ID = UUID.randomUUID().toString();

    @Bind(R.id.card_view_post)
    CardView mCardView;
    @Bind(R.id.adView)
    AdView mAdView;
    @Bind(R.id.button_subscribe)
    AppCompatButton mSubscribeButton;
    @Bind(R.id.post_title)
    TextView mTitleTextView;
    @Bind(R.id.button_next_post)
    AppCompatButton mNextButton;
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    private Post mPost;

    @OnClick(R.id.card_view_post)
    public void openPostInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Utility.buildCommentsUri(mPost.getPermalink()));
        startActivity(intent);
    }

    @OnClick(R.id.button_next_post)
    public void fetchNextPost() {
        updateAfterField();
        fetchPost();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        loadAdBanner();
        getAuthToken();
        fetchPost();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracker tracker = ((CustomApplication) getApplication()).getTracker();
        tracker.setScreenName("Main Activity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_subscribe:
                startActivity(new Intent(this, SubscribeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadAdBanner() {
        //Add your own device Id if testing on a real device
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F178C54519B81461A49075E590A1E90C")
                .build();
        mAdView.loadAd(adRequest);
    }

    public void launchSubscribeSetting(View unusedView) {
        startActivity(new Intent(this, SubscribeActivity.class));
    }

    private void getAuthToken() {
        try {
            new RedditRestClient().getToken(TOKEN_URL, DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchPost() {
        Cursor cursor = getContentResolver().query(RedditProvider.Subscriptions.randomSubscription,
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            //keep going
            String subreddit = cursor.getString(
                    cursor.getColumnIndex(SubscriptionsColumns.SR_NAME));
            String postAfter = cursor.getString(
                    cursor.getColumnIndex(SubscriptionsColumns.POST_AFTER));
            new FetchPostTask(this).execute(subreddit, postAfter);
        } else {
            mCardView.setVisibility(View.GONE);
            mNextButton.setVisibility(View.GONE);
            mSubscribeButton.setVisibility(View.VISIBLE);
        }
        Utility.closeCursor(cursor);
    }

    private void updateAfterField() {
        ContentValues values = new ContentValues();
        values.put(SubscriptionsColumns.POST_AFTER, mPost.getAfter());

        getApplicationContext().getContentResolver().update(
                RedditProvider.Subscriptions.withSubredditName(mPost.getSubreddit()),
                values,
                null,
                null);
    }

    @Override
    public void taskCompletionResult(Post result) {
        mPost = result;
        mTitleTextView.setText(result.getTitle());
    }
}
