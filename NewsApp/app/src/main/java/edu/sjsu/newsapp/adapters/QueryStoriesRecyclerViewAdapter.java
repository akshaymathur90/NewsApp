package edu.sjsu.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.sjsu.newsapp.activity.NewsArticle;
import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.models.querystories.Doc;

/**
 * Created by akshaymathur.
 */

public class QueryStoriesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    public final String TAG = "StoriesRecyclerAdapter";
    ArrayList<Doc> mTopStories = new ArrayList<>();

    private Context mContext;

    private boolean isLoadingAdded = false;
    public QueryStoriesRecyclerViewAdapter(Context context){
        mContext = context;
    }

    // initialize the starting dataset.
    public void setDataSet(ArrayList<Doc> dataset){
        mTopStories = dataset;
        //notifyItemRangeChanged(0,mTopStories.size());
        notifyDataSetChanged();
    }
    public ArrayList<Doc> getDataSet(){
        return mTopStories;
    }

    // append more data to the dataset.
    public void addMoreData(List<Doc> newDataSet){
        int oldSize = mTopStories.size()-1;
        mTopStories.addAll(newDataSet);
        notifyItemRangeChanged(oldSize,mTopStories.size());
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Return appropriate view holder for the view type.
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
        // Bind appropriate data based on the view type.
        switch (getItemViewType(position)) {
            case ITEM:
                TopStoriesViewHolder storiesViewHolder = (TopStoriesViewHolder) holder;
                if(story.getHeadline()!=null) {
                    storiesViewHolder.mHeadLineTextView.setText(story.getHeadline().getMain());
                }
                String baseURL = mContext.getString(R.string.query_image_base_url);

                // Load images in Image View if available
                if(story.getMultimedia()!=null && story.getMultimedia().size()>0){
                    Log.d(TAG,"Image URL--> "+baseURL+story.getMultimedia().get(0).getUrl());
                    Log.d(TAG,"Image Type--> "+baseURL+story.getMultimedia().get(0).getType());
                    Glide
                            .with(mContext)
                            .load(baseURL+story.getMultimedia().get(0).getUrl())
                            .into(storiesViewHolder.mImageView);
                }
                String pubTime = story.getPubDate();
                if(pubTime!=null) {
                    // Format Timestamp to local date format.
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
                    Date pubDate;
                    try {
                        pubDate = formatter.parse(pubTime);
                        DateFormat dateFormat = DateFormat.getDateInstance();
                        storiesViewHolder.mPubTime.setText(dateFormat.format(pubDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                // On click of news article start News Details activity to display the complete article.
                storiesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newsArticle = new Intent(mContext, NewsArticle.class);
                        newsArticle.putExtra(mContext.getString(R.string.url_key),story.getWebUrl());
                        if(story.getHeadline()!=null) {
                            newsArticle.putExtra(mContext.getString(R.string.headline_key), story.getHeadline().getMain());
                        }
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

    /*
    Start of Helper Methods for adding and removing loading footer.
     */
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

    /*
    End of Helper Methods for adding and removing loading footer.
     */

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
