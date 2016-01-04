package com.nicolascarrasco.www.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nicolascarrasco.www.redditreader.data.Post;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private Post mPost;
    @Bind(R.id.detail_post_title) TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        fetchPostFromIntent();
        mTitleView.setText(mPost.getTitle());


    }

    private void fetchPostFromIntent(){
        Intent intent = getIntent();
        mPost = intent.getParcelableExtra(Utility.PARCELABLE_KEY);
    }
}
