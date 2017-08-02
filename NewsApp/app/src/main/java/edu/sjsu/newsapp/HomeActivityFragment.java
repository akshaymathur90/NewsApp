package edu.sjsu.newsapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
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

import edu.sjsu.newsapp.adapters.PaginationScrollListner;
import edu.sjsu.newsapp.adapters.TopStoriesRecyclerViewAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment {

    TopStoriesRecyclerViewAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    RequestQueue rq;

    public final String TAG = "HomeFragment";

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        rv = (RecyclerView) v.findViewById(R.id.top_stories_recyclerview);
        progressBar = (ProgressBar) v.findViewById(R.id.loadmore_progress);

        adapter = new TopStoriesRecyclerViewAdapter(getActivity());

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

        loadFirstPage();
        
        return v;





    }

    private void loadNextPage() {
    }

    private void loadFirstPage() {

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("q","trump")
                .appendQueryParameter("api-key","d31fe793adf546658bd67e2b6a7fd11a")
                .appendQueryParameter("fl","web_url,multimedia,headline,pub_date").build();
        Log.d(TAG,"Uri path --> "+uri.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"Response: " + response.toString());
                        Gson gson = new Gson();
                        edu.sjsu.newsapp.models.Response r = null;
                        try {
                            r = gson.fromJson(response.getString("response"), edu.sjsu.newsapp.models.Response.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,"Number of hits: " + r.getMeta().getHits());
                        adapter.setDataSet(r.getDocs());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        rq.add(jsObjRequest);
    }

}
