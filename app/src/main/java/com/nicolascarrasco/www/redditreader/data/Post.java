package com.nicolascarrasco.www.redditreader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nicolás Carrasco on 30/12/2015.
 */
public class Post implements Parcelable {
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    private String mTitle;
    private String mAfter;

    public Post() {
    }

    public Post(Parcel in) {
        mTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAfter() {
        return mAfter;
    }

    public void setAfter(String after) {
        mAfter = after;
    }
}
