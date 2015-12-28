package com.nicolascarrasco.www.redditreader.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Nicolas Carrasco on 28/12/2015.
 * Columns for the table the stores the list of subscribed reddits
 */
public interface SubscriptionsColumns {
    @DataType(DataType.Type.INTEGER)
    @AutoIncrement
    @PrimaryKey
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SR_NAME = "subreddit_name";
}
