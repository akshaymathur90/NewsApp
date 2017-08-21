package edu.sjsu.newsapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.webkit.WebView;

import edu.sjsu.newsapp.fragments.NewsDetailFragment;
import edu.sjsu.newsapp.R;

public class NewsArticle extends AppCompatActivity {

    public final String TAG = "NewsArticle";
    public WebView mWebView;
    ShareActionProvider mShareActionProvider;
    String url;
    Fragment newsDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        FragmentManager fm = getSupportFragmentManager();
        newsDetailFragment = NewsDetailFragment.newInstance(getIntent().getExtras().getString("url"),getIntent().getExtras().getString("headline"));
        fm.beginTransaction().add(R.id.news_detail_container,newsDetailFragment,"detailfragment").commit();
    }



    @Override
    public void onBackPressed() {
        ((NewsDetailFragment)newsDetailFragment).backButtonPressed();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
