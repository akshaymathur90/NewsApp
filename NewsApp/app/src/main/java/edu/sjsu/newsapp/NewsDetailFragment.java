package edu.sjsu.newsapp;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.sjsu.newsapp.receivers.InternetCheckReceiver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailFragment extends VisibleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String URL_KEY = "url";
    private static final String HEADLINE_KEY = "headline";

    // TODO: Rename and change types of parameters
    private String mUrl;
    private String mHeadline;

    public WebView mWebView;
    ShareActionProvider mShareActionProvider;
    public View mView;


    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWebView = (WebView) v.findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);

        return v;
    }

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
