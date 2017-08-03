package edu.sjsu.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.sjsu.newsapp.NewsArticle;
import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.models.Doc;
import edu.sjsu.newsapp.models.TopStories;

/**
 * Created by akshaymathur on 8/1/17.
 */

public class TopStoriesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    public final String TAG = "StoriesRecyclerAdapter";
    List<Doc> mTopStories = new ArrayList<>();

    private Context mContext;

    private boolean isLoadingAdded = false;
    public TopStoriesRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public void setDataSet(List<Doc> dataset){
        mTopStories = dataset;
        notifyItemRangeChanged(0,mTopStories.size());
    }

    public void addMoreData(List<Doc> newDataSet){
        int oldSize = mTopStories.size()-1;
        mTopStories.addAll(newDataSet);
        notifyItemRangeChanged(oldSize,mTopStories.size());
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.top_stories_item, parent, false);
        viewHolder = new TopStoriesViewHolder(v1);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Doc story = mTopStories.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                TopStoriesViewHolder storiesViewHolder = (TopStoriesViewHolder) holder;

                storiesViewHolder.mHeadLineTextView.setText(story.getHeadline().getMain());
                String baseURL = "https://www.nytimes.com/";
                if(story.getMultimedia().size()>0){
                    Log.d(TAG,"Image URL--> "+baseURL+story.getMultimedia().get(0).getUrl());
                    Log.d(TAG,"Image Type--> "+baseURL+story.getMultimedia().get(0).getType());
                    Glide
                            .with(mContext)
                            .load(baseURL+story.getMultimedia().get(0).getUrl())
                            .into(storiesViewHolder.mImageView);
                }
                String pubTime = story.getPubDate();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
                Date pubDate;
                try {
                    pubDate = formatter.parse(pubTime);
                    //int diff = pubDate.compareTo(Calendar.getInstance().getTime());
                    //Log.d(TAG,"Time difference --> "+diff);
                    storiesViewHolder.mPubTime.setText(pubDate.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                storiesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newsArticle = new Intent(mContext, NewsArticle.class);
                        newsArticle.putExtra("url",story.getWebUrl());
                        mContext.startActivity(newsArticle);
                    }
                });

                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mTopStories == null ? 0 : mTopStories.size();
    }
    @Override
    public int getItemViewType(int position) {
        return (position == mTopStories.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    public void add(Doc doc){
        mTopStories.add(doc);
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Doc());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mTopStories.size() - 1;
        Doc result = getItem(position);

        if (result != null) {
            mTopStories.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Doc getItem(int position) {
        return mTopStories.get(position);
    }

    public static class TopStoriesViewHolder extends RecyclerView.ViewHolder{

        View mView;
        private TextView mHeadLineTextView;
        private ImageView mImageView;
        private TextView mPubTime;
        TopStoriesViewHolder(View view){
            super(view);
            mView=view;
            mHeadLineTextView = (TextView) view.findViewById(R.id.headline_text);
            mImageView = (ImageView) view.findViewById(R.id.news_thumbnail);
            mPubTime = (TextView) view.findViewById(R.id.time_since_post);
        }

    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
