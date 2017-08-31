package edu.sjsu.newsapp.adapters;

import android.content.Context;
import android.content.Intent;
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
import edu.sjsu.newsapp.models.topstories.Result;

/**
 * Created by akshaymathur
 */

public class TopStoriesRecyclerViewAdapter extends RecyclerView.Adapter<TopStoriesRecyclerViewAdapter.TopStoryViewHolder> {

    List<Result> mTopStories;
    private final String TAG = "TopStoriesAdapter";

    Context mContext;

    public TopStoriesRecyclerViewAdapter(Context context){
        mContext = context;
        mTopStories = new ArrayList<>();
    }

    public void setDataSet(List<Result> dataSet){
        mTopStories = dataSet;
        notifyDataSetChanged();
    }
    @Override
    public TopStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.top_stories_item,parent,false);

        return new TopStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopStoryViewHolder holder, int position) {
        final Result story = mTopStories.get(position);

        if(story.getTitle()!=null){
            holder.mHeadLineTextView.setText(story.getTitle());
        }
        if(story.getMultimedia()!=null && story.getMultimedia().size()>0 && story.getMultimedia().get(0)!=null){
            Log.d(TAG,"Image URL--> "+story.getMultimedia().get(0).getUrl());
            Log.d(TAG,"Image Type--> "+story.getMultimedia().get(0).getType());
            Glide
                    .with(mContext)
                    .load(story.getMultimedia().get(0).getUrl())
                    .into(holder.mImageView);
        }
        if(story.getPublishedDate()!=null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            Date pubDate;
            try {
                pubDate = formatter.parse(story.getPublishedDate());
                DateFormat dateFormat = DateFormat.getDateInstance();
                dateFormat.format(pubDate);
                holder.mPubTime.setText(dateFormat.format(pubDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsArticle = new Intent(mContext, NewsArticle.class);
                newsArticle.putExtra(mContext.getString(R.string.url_key),story.getUrl());
                if(story.getTitle()!=null) {
                    newsArticle.putExtra(mContext.getString(R.string.headline_key), story.getTitle());
                }
                mContext.startActivity(newsArticle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTopStories.size();
    }

    public class TopStoryViewHolder extends RecyclerView.ViewHolder{

        View mView;
        private TextView mHeadLineTextView;
        private ImageView mImageView;
        private TextView mPubTime;
        TopStoryViewHolder(View view){
            super(view);
            mView=view;
            mHeadLineTextView = (TextView) view.findViewById(R.id.headline_text);
            mImageView = (ImageView) view.findViewById(R.id.news_thumbnail);
            mPubTime = (TextView) view.findViewById(R.id.time_since_post);
        }
    }
}
