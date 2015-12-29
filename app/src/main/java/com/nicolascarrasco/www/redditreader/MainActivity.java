package com.nicolascarrasco.www.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nicolascarrasco.www.redditreader.client.RedditRestClient;

import org.json.JSONException;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TOKEN_URL = "v1/access_token";
    private static final String DEVICE_ID = UUID.randomUUID().toString();

    @Bind(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadAdBanner();
        getAuthToken();
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
}
