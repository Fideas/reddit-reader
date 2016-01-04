package com.nicolascarrasco.www.redditreader;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Nicolás Carrasco on 04/01/2016.
 * This class is used to make sure we only have one instance of the tracker
 */
public class CustomApplication extends Application {
    public Tracker mTracker;

    public void startTracking() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.track_app);
            analytics.enableAutoActivityReports(this);
        }
    }

    public Tracker getTracker() {
        startTracking();
        return mTracker;
    }
}
