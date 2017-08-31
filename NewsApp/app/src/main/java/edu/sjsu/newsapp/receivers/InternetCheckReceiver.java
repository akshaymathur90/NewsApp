package edu.sjsu.newsapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import edu.sjsu.newsapp.R;

/**
 * Created by akshaymathur
 */

public class InternetCheckReceiver extends BroadcastReceiver{

    private View mView;
    public final String TAG = "InternetCheckReceiver";
    public boolean internet = false;
    public InternetCheckReceiver(View view){
        mView = view;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"caught the change");
        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.datalayout);
        RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.no_internet_layout);
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if(isConnected){
            Log.d(TAG,"Connect to Internet");
            internet = true;
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else {
            internet = false;
            Snackbar mySnackbar = Snackbar.make(mView,
                    "Lost Internet Connectivity", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            mySnackbar.show();


            linearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            Log.d(TAG,"Disconnect from Internet");
        }


    }

    public boolean isInternetAvailable(){
        return internet;
    }
}
