package edu.sjsu.newsapp;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Parcelable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
        mQuery = getArguments().getString("query");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        rv = (RecyclerView) v.findViewById(R.id.top_stories_recyclerview);
        progressBar = (ProgressBar) v.findViewById(R.id.loadmore_progress);
        mView =  v;

        adapter = new QueryStoriesRecyclerViewAdapter(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
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
        rq = Volley.newRequestQueue(getActivity());

        if(savedInstanceState!=null){
            Log.d(TAG,"****Loading from destroyed activity****");
            Log.d(TAG,"The current page is --> " +savedInstanceState.getInt("currentpage"));
            Log.d(TAG,"The query is --> " +savedInstanceState.getString("query"));

            currentPage = savedInstanceState.getInt("currentpage");
            mQuery = savedInstanceState.getString("query");
            ArrayList<Doc> data =  savedInstanceState.getParcelableArrayList("dataset");
            adapter.setDataSet(data);
            listState = savedInstanceState.getParcelable("recyclerview");
            TOTAL_PAGES = savedInstanceState.getInt("totalpages");

        }
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

    private void loadNextPage() {
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("q",mQuery)
                .appendQueryParameter("api-key","6973729bd76c46819a940bb6b55c6b0d")
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
                        // TODO Auto-generated method stub

                    }
                });

        rq.add(jsObjRequest);
    }

    private void loadFirstPage() {

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("q",mQuery)
                .appendQueryParameter("api-key","6973729bd76c46819a940bb6b55c6b0d")
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
                        // TODO Auto-generated method stub

                    }
                });

        rq.add(jsObjRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("query",mQuery);
        outState.putParcelableArrayList("dataset",adapter.getDataSet());
        outState.putInt("currentpage",currentPage);
        outState.putParcelable("recyclerview",linearLayoutManager.onSaveInstanceState());
        outState.putInt("totalpages",TOTAL_PAGES);
    }

    @Override
    BroadcastReceiver getBroadcastReceiver() {
        return new InternetCheckReceiver(mView);
    }
}
