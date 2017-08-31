package edu.sjsu.newsapp.fragments;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.receivers.InternetCheckReceiver;


public class NewsDetailFragment extends VisibleFragment {
    private static final String URL_KEY = "url";
    private static final String HEADLINE_KEY = "headline";

    private String mUrl;
    private String mHeadline;

    public WebView mWebView;
    ShareActionProvider mShareActionProvider;
    public View mView;


    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(String param1, String param2) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, param1);
        args.putString(HEADLINE_KEY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL_KEY);
            mHeadline = getArguments().getString(HEADLINE_KEY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_article,container,false);
        mView = v;
        Toolbar myToolbar = (Toolbar) v.findViewById(R.id.news_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mWebView = (WebView) v.findViewById(R.id.web_view);
        //Setting new webview client.
        mWebView.setWebViewClient(new WebViewClient());
        //load the url of the news article in webview.
        mWebView.loadUrl(mUrl);

        return v;
    }

    // create share menu item to share article through apps like email and Whatsapp.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.newsarticle_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        if(shareItem!=null){
            mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
            sendIntent.setType("text/plain");
            mShareActionProvider.setShareIntent(sendIntent);

        }

    }
    // Handle back press
    public boolean backButtonPressed(){
        // Check if the key event was the Back button and if there's history
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return false;
    }

    @Override
    BroadcastReceiver getBroadcastReceiver() {
        return new InternetCheckReceiver(mView);
    }

}
