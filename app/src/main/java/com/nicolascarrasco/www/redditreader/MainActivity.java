package com.nicolascarrasco.www.redditreader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

    @OnClick(R.id.card_view_post)
    public void launchDetailActivity() {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadAdBanner();
        getAuthToken();
        fetchPost();
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
            new FetchPostTask(this).execute(subreddit);
        } else {
            mCardView.setVisibility(View.GONE);
            mSubscribeButton.setVisibility(View.VISIBLE);
        }
        Utility.closeCursor(cursor);
    }

    @Override
    public void taskCompletitionResult(Post result) {
        mTitleTextView.setText(result.getTitle());
    }
}
