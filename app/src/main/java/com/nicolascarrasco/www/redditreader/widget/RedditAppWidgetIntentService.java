package com.nicolascarrasco.www.redditreader.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.nicolascarrasco.www.redditreader.MainActivity;
import com.nicolascarrasco.www.redditreader.R;
import com.nicolascarrasco.www.redditreader.Utility;
import com.nicolascarrasco.www.redditreader.data.RedditProvider;
import com.nicolascarrasco.www.redditreader.data.SubscriptionsColumns;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RedditAppWidgetIntentService extends IntentService {

    public RedditAppWidgetIntentService() {
        super("RedditAppWidgetIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //This is merely a proof of concept method, it doesn't really add any value to the app
        if (intent != null) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(this, RedditAppWidget.class));

            Cursor data = getContentResolver().query(
                    RedditProvider.Subscriptions.randomSubscription,
                    null,
                    null,
                    null,
                    null);

            if (data == null || !data.moveToFirst()) {
                Utility.closeCursor(data);
                return;
            }
            String randomSubscription =
                    data.getString(data.getColumnIndex(SubscriptionsColumns.SR_NAME));

            for (int appWidgetId : appWidgetIds) {
                Intent launchIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.reddit_app_widget);
                views.setTextViewText(R.id.appwidget_text, randomSubscription);
                views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }
}
