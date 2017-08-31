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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.adapters.TopStoriesRecyclerViewAdapter;

/**
 * Created by akshaymathur.
 */

public class SingleNewsTabFragment extends Fragment {

    String mSection;
    RequestQueue requestQueue;
    TopStoriesRecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager;
    View mView;

    private final String TAG = "SingleNewsTabFragment";

    public static SingleNewsTabFragment newInstance(String section) {
        SingleNewsTabFragment f = new SingleNewsTabFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("section", section);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSection = getArguments() != null ? getArguments().getString("section") : "home";

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_news_tab, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_list_recyclerview);
        Log.d("CHECK", "loading views for: " + mSection);
        mView = v;
        return v;
    }
    // Displaying top stories for the requested section.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        mAdapter = new TopStoriesRecyclerViewAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        fetchTopStoriesForSection(mSection);

    }
    // Fetching top stories for the requested section.
    private void fetchTopStoriesForSection(String section) {
        String url = getString(R.string.top_stories_base_url)+section+".json";
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api-key",getString(R.string.api_key)).build();
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
                        //Display Toast/Snackbar to inform the user of a network error.
                        Toast.makeText(getActivity(),getString(R.string.network_error_msg),Toast.LENGTH_SHORT).show();
                        Log.d(TAG,error.getMessage());

                    }
                });

        requestQueue.add(jsObjRequest);

    }

}
