package com.nicolascarrasco.www.redditreader.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Nicolás Carrasco on 28/12/2015.
 * Database for the app
 */
@Database(version = RedditDatabase.VERSION,
        packageName = "com.nicolascarrasco.www.redditreader.provider")
public final class RedditDatabase {
    public static final int VERSION = 1;

    @Table(SubscriptionsColumns.class) public static final String SUBSCRIPTIONS = "subscriptions";
}
