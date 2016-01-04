package com.nicolascarrasco.www.redditreader;

import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Nicolás Carrasco on 30/12/2015.
 */
public class Utility {

    public static final String PARCELABLE_KEY = "post_parcelable_key";

    public static String formatSubredditName(String subredditName) {
        return subredditName.substring(2, subredditName.length() - 2);
    }

    public static void closeCursor(Cursor cursor){
        if (null != cursor){
            cursor.close();
        }
    }

    public static Uri buildCommentsUri(String path){
        return Uri.parse("http://i.reddit.com".concat(path));
    }
}
