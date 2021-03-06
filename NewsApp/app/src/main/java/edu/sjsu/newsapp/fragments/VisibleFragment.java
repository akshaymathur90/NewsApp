package edu.sjsu.newsapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.sjsu.newsapp.receivers.InternetCheckReceiver;

/**
 * Created by akshaymathur.
 */

public abstract class VisibleFragment extends Fragment{

    BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onStart() {
        super.onStart();
        mBroadcastReceiver = getBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    abstract BroadcastReceiver getBroadcastReceiver();
}
