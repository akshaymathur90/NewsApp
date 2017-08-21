package edu.sjsu.newsapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.newsapp.fragments.HomeActivityFragment;
import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.adapters.NewsTabAdapter;

public class HomeActivity extends AppCompatActivity {

    final String TAG = "Home Activity-->";
    String queryString;
    View mView;

    NewsTabAdapter mNewsTabAdapter;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Boolean showingQueryFragment=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleIntent(getIntent());
        mView = findViewById(R.id.baselayout);

        List<String> sectionsList = new ArrayList<>();
        sectionsList.add("home");
        sectionsList.add("world");
        sectionsList.add("national");
        sectionsList.add("politics");
        sectionsList.add("science");
        sectionsList.add("travel");
        mNewsTabAdapter = new NewsTabAdapter(getSupportFragmentManager(),sectionsList);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager.setAdapter(mNewsTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Log.d(TAG,"oncreate showqueryfragment-->" + showingQueryFragment);

        if(savedInstanceState!=null)
            showQueryFragment(savedInstanceState.getBoolean("fragmentopen"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        MenuItem searchItem = menu.findItem(R.id.search);

        Log.d(TAG, "The query is-->"+queryString);
        if (!TextUtils.isEmpty(queryString)) {
            Log.d(TAG, "Setting query");
            searchItem.expandActionView();
            searchView.setQuery(queryString, false);
            searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG,"Query Text change--> "+newText);
                queryString = newText;
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            showingQueryFragment =true;
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG,query);
            //use the query to search your data somehow

            FragmentManager fm = getSupportFragmentManager();
            Fragment f =fm.findFragmentById(R.id.fragment);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            if(f instanceof HomeActivityFragment){
                HomeActivityFragment newHomeActivityFragment = HomeActivityFragment.newInstance(query);
                fragmentTransaction.replace(R.id.fragment,newHomeActivityFragment).commit();
            }
            else{
                HomeActivityFragment homeActivityFragment = HomeActivityFragment.newInstance(query);
                fragmentTransaction.add(R.id.fragment,homeActivityFragment,"searchresults").commit();
            }
            showQueryFragment(true);
        }
    }

    public void showQueryFragment(Boolean showFragment){
        if(showFragment){
            mView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
        }
        else{
            Log.d(TAG,"Closing query fragment");
            mView.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            mTabLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {

        Log.d(TAG,"Back pressed");
        if(showingQueryFragment) {
            showQueryFragment(false);
            showingQueryFragment=false;
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"The persisting query is--> "+queryString);
        outState.putString("query",queryString);
        outState.putBoolean("fragmentopen",showingQueryFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        queryString = savedInstanceState.getString("query");
        showingQueryFragment = savedInstanceState.getBoolean("fragmentopen");
        Log.d(TAG,"The restored query is--> "+queryString);
    }


}
