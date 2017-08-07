package edu.sjsu.newsapp;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import edu.sjsu.newsapp.receivers.InternetCheckReceiver;

public class HomeActivity extends AppCompatActivity {

    final String TAG = "Home Activity-->";
    String queryString;
    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleIntent(getIntent());
        mView = findViewById(R.id.baselayout);


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

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"The persisting query is--> "+queryString);
        outState.putString("query",queryString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        queryString = savedInstanceState.getString("query");
        Log.d(TAG,"The restored query is--> "+queryString);
    }


}
