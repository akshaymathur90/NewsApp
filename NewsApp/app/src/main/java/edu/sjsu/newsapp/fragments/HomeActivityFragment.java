package edu.sjsu.newsapp.fragments;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.adapters.PaginationScrollListner;
import edu.sjsu.newsapp.adapters.QueryStoriesRecyclerViewAdapter;
import edu.sjsu.newsapp.models.querystories.Doc;
import edu.sjsu.newsapp.receivers.InternetCheckReceiver;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends VisibleFragment {

    QueryStoriesRecyclerViewAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    Parcelable listState;

    RecyclerView rv;
    ProgressBar progressBar;
    View mView;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    RequestQueue rq;

    public final String TAG = "HomeFragment";
    public String mQuery;

    public static HomeActivityFragment newInstance(String query){
        HomeActivityFragment homeActivityFragment = new HomeActivityFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        homeActivityFragment.setArguments(args);

        return  homeActivityFragment;
    }

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // getting the query string from fragment arguments.
        mQuery = getArguments().getString("query");

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rv = (RecyclerView) v.findViewById(R.id.top_stories_recyclerview);
        progressBar = (ProgressBar) v.findViewById(R.id.loadmore_progress);
        mView =  v;
        adapter = new QueryStoriesRecyclerViewAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);

        //adding a scroll listener to help with pagination of the results.
        rv.addOnScrollListener(new PaginationScrollListner(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //creating a new request queue for network calls.
        rq = Volley.newRequestQueue(getActivity());

        //restoring state after configuration change.
        if(savedInstanceState!=null){
            Log.d(TAG,"****Loading from destroyed activity****");
            Log.d(TAG,"The current page is --> " +savedInstanceState.getInt(getString(R.string.current_page_key)));
            Log.d(TAG,"The query is --> " +savedInstanceState.getString(getString(R.string.querykey)));

            currentPage = savedInstanceState.getInt(getString(R.string.current_page_key));
            mQuery = savedInstanceState.getString(getString(R.string.querykey));
            ArrayList<Doc> data =  savedInstanceState.getParcelableArrayList(getString(R.string.dataset_key));
            adapter.setDataSet(data);
            listState = savedInstanceState.getParcelable(getString(R.string.recyler_view_key));
            TOTAL_PAGES = savedInstanceState.getInt(getString(R.string.total_pages_key));

        }
        //proceed with loading the first page if the activity is created for the first time.
        else{
            loadFirstPage();
        }


        return v;





    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null) {
            linearLayoutManager.onRestoreInstanceState(listState);
        }
    }

    // Method to fetch new page from the API using volley.
    private void loadNextPage() {
        String url = getString(R.string.article_search_base_url);
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("q",mQuery)
                .appendQueryParameter("api-key",getString(R.string.api_key))
                .appendQueryParameter("fl","web_url,multimedia,headline,pub_date")
                .appendQueryParameter("page",String.valueOf(currentPage))
                .build();
        Log.d(TAG,"Uri path --> "+uri.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"Response: " + response.toString());
                        Gson gson = new Gson();
                        edu.sjsu.newsapp.models.querystories.Response r = null;
                        try {
                            r = gson.fromJson(response.getString("response"), edu.sjsu.newsapp.models.querystories.Response.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.removeLoadingFooter();
                        Log.d(TAG,"Page: " + (r.getMeta().getOffset()/10));
                        adapter.addMoreData(r.getDocs());
                        isLoading = false;
                        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display Toast/Snackbar to inform the user of a network error.
                        Toast.makeText(getActivity(),getString(R.string.network_error_msg),Toast.LENGTH_SHORT).show();
                        Log.d(TAG,error.getMessage());
                    }
                });

        rq.add(jsObjRequest);
    }

    // Method to fetch first page from the API using volley.
    private void loadFirstPage() {

        String url = getString(R.string.article_search_base_url);
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("q",mQuery)
                .appendQueryParameter("api-key",getString(R.string.api_key))
                .appendQueryParameter("fl","web_url,multimedia,headline,pub_date").build();
        Log.d(TAG,"Uri path --> "+uri.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"Response: " + response.toString());
                        Gson gson = new Gson();
                        edu.sjsu.newsapp.models.querystories.Response r = null;
                        try {
                            r = gson.fromJson(response.getString("response"), edu.sjsu.newsapp.models.querystories.Response.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,"Number of hits: " + r.getMeta().getHits());
                        TOTAL_PAGES = r.getMeta().getHits()/10;
                        Log.d(TAG,"Total Pages --> "+ TOTAL_PAGES);
                        adapter.setDataSet(r.getDocs());

                        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display Toast/Snackbar to inform the user of a network error.
                        Toast.makeText(getActivity(),getString(R.string.network_error_msg),Toast.LENGTH_SHORT).show();
                        Log.d(TAG,error.getMessage());

                    }
                });

        rq.add(jsObjRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.querykey),mQuery);
        outState.putParcelableArrayList(getString(R.string.dataset_key),adapter.getDataSet());
        outState.putInt(getString(R.string.current_page_key),currentPage);
        outState.putParcelable(getString(R.string.recyler_view_key),linearLayoutManager.onSaveInstanceState());
        outState.putInt(getString(R.string.total_pages_key),TOTAL_PAGES);
    }

    @Override
    BroadcastReceiver getBroadcastReceiver() {
        return new InternetCheckReceiver(mView);
    }
}
