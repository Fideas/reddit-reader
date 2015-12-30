package com.nicolascarrasco.www.redditreader;

/**
 * Created by Nicolás Carrasco on 30/12/2015.
 */
public class Utility {

    public static String formatSubredditName(String subredditName) {
        return subredditName.substring(2, subredditName.length() - 2);
    }
}
