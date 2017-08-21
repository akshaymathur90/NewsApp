package edu.sjsu.newsapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.adapters.TopStoriesRecyclerViewAdapter;
import edu.sjsu.newsapp.models.topstories.Result;

/**
 * Created by akshaymathur on 8/15/17.
 */

public class SingleNewsTabFragment extends Fragment {

    String mSection;
    List<Result> mTopStories;
    RequestQueue requestQueue;
    TopStoriesRecyclerViewAdapter mAdapter;
    RecyclerView mRecycletView;
    LinearLayoutManager linearLayoutManager;
    View mView;

    private final String TAG = "SingleNewsTabFragment";
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static SingleNewsTabFragment newInstance(String section) {
        SingleNewsTabFragment f = new SingleNewsTabFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("section", section);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSection = getArguments() != null ? getArguments().getString("section") : "home";

    }

    private void fetchTopStoriesForSection(String section) {
        List<Result> sectionStories = new ArrayList<>();

        String url = "https://api.nytimes.com/svc/topstories/v2/"+section+".json";
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api-key","6973729bd76c46819a940bb6b55c6b0d").build();
        Log.d(TAG,"Uri path --> "+uri.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"Response: " + response.toString());
                        Gson gson = new Gson();
                        edu.sjsu.newsapp.models.topstories.TopStoriesResponse r = null;
                        r = gson.fromJson(response.toString(), edu.sjsu.newsapp.models.topstories.TopStoriesResponse.class);
                        Log.d(TAG,"Number of stories: " + r.getNumResults());
                        mAdapter.setDataSet(r.getResults());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        requestQueue.add(jsObjRequest);

    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_news_tab, container, false);
        mRecycletView = (RecyclerView) v.findViewById(R.id.news_list_recyclerview);
        Log.d("CHECK", "loading views for: " + mSection);
        mView = v;
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        mAdapter = new TopStoriesRecyclerViewAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycletView.setLayoutManager(linearLayoutManager);
        mRecycletView.setAdapter(mAdapter);
        fetchTopStoriesForSection(mSection);

    }

}
