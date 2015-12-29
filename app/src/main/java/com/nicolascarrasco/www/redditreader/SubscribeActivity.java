package com.nicolascarrasco.www.redditreader;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

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
        AddSubscriptionDialogFragment dialogFragment = new AddSubscriptionDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"");
    }

    public static class AddSubscriptionDialogFragment extends android.support.v4.app.DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setView(R.layout.dialog_add_subscription);
            builder.setTitle(getString(R.string.subscribe_dialog_title));
            builder.setPositiveButton(getString(R.string.subscribe_dialog_positive_button),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton(getString(R.string.subscribe_dialog_negative_button),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }
    }
}
