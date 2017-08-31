package edu.sjsu.newsapp.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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

        //Creating list of the News tabs that will be displayed on the home screen.
        List<String> sectionsList = new ArrayList<>();
        sectionsList.add("home");
        sectionsList.add("world");
        sectionsList.add("national");
        sectionsList.add("politics");
        sectionsList.add("science");
        sectionsList.add("travel");

        //Using the News tab adapter to host all the tab fragments.
        mNewsTabAdapter = new NewsTabAdapter(getSupportFragmentManager(),sectionsList);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager.setAdapter(mNewsTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Log.d(TAG,"oncreate showqueryfragment-->" + showingQueryFragment);
        //in case of configuration change restore the state of the fragments.
        if(savedInstanceState!=null)
            showQueryFragment(savedInstanceState.getBoolean(getString(R.string.fragmentopen)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        //Set up search view.
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        MenuItem searchItem = menu.findItem(R.id.search);

        //Close the Search results fragment when home button on app bar is pressed.
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d(TAG,"Search view back pressed");
                switchBetweenFragmentAndActivity();
                invalidateOptionsMenu();
                return true;
            }
        });
        Log.d(TAG, "The query is-->"+queryString);

        // If query String is not empty set the value to the search view.
        if (!TextUtils.isEmpty(queryString)) {
            Log.d(TAG, "Setting query");
            searchItem.expandActionView();
            searchView.setQuery(queryString, false);
            searchView.clearFocus();
        }

        //maintain the search query string so that the value can be restored in case of configuration changes.
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

        int id = item.getItemId();
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

        //Check if the received intent is for Search.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            showingQueryFragment =true;
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG,query);
            FragmentManager fm = getSupportFragmentManager();
            Fragment f =fm.findFragmentById(R.id.fragment);
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //Add or Replace the fragment and pass the search query.
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
        //UI changes to display the search fragment or the home page containing the tab fragments.
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
        /*
         * Handling back button by removing the search results fragment if it is on display.
         * Otherwise destroy the activity.
        */
        Log.d(TAG,"Back pressed");

        if(showingQueryFragment) {
            showQueryFragment(false);
            showingQueryFragment=false;
        }else{
            super.onBackPressed();
        }

    }

    // used to close the query results fragment if it is on display.
    public void switchBetweenFragmentAndActivity(){
        if(showingQueryFragment) {
            showQueryFragment(false);
            showingQueryFragment=false;
        }
    }

    // Saving states
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"The persisting query is--> "+queryString);
        outState.putString(getString(R.string.querykey),queryString);
        outState.putBoolean(getString(R.string.fragmentopen),showingQueryFragment);
    }

    // Restoring states
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        queryString = savedInstanceState.getString(getString(R.string.querykey));
        showingQueryFragment = savedInstanceState.getBoolean(getString(R.string.fragmentopen));
        Log.d(TAG,"The restored query is--> "+queryString);
    }


}
