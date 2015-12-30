package com.nicolascarrasco.www.redditreader.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nicolascarrasco.www.redditreader.R;
import com.nicolascarrasco.www.redditreader.data.SubscriptionsColumns;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nicolás Carrasco on 29/12/2015.
 * Simple adapter for the subscriptions list
 */
public class SubscriptionAdapter extends
        RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private Cursor mCursor;
    private final static String CURSOR_MOVE_ERROR = "Could not move cursor to position ";

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            throw new IllegalStateException(CURSOR_MOVE_ERROR + position);
        }
        String title = mCursor.getString(mCursor.getColumnIndex(SubscriptionsColumns.SR_NAME));
        holder.title.setText(title);
    }

    public void swapCursor(Cursor newCursor){
        this.mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class SubscriptionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_view_subscription_name)
        TextView title;

        @Bind(R.id.button_delete_subscription)
        ImageButton deleteButton;

        public SubscriptionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
