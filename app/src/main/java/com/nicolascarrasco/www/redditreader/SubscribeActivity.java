package com.nicolascarrasco.www.redditreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscribeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab_add_subscription)
    public void launchAddSubscriptionDialog(){
        Toast.makeText(getApplicationContext(), "Just a Test Toast", Toast.LENGTH_LONG).show();
    }
}
