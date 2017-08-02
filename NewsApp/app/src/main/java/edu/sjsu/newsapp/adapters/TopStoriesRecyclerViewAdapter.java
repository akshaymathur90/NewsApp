package edu.sjsu.newsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.newsapp.R;
import edu.sjsu.newsapp.models.Doc;
import edu.sjsu.newsapp.models.TopStories;

/**
 * Created by akshaymathur on 8/1/17.
 */

public class TopStoriesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
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

        Doc story = mTopStories.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                TopStoriesViewHolder storiesViewHolder = (TopStoriesViewHolder) holder;

                storiesViewHolder.mHeadLineTextView.setText(story.getHeadline().getMain());
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
        TopStoriesViewHolder(View view){
            super(view);
            mView=view;
            mHeadLineTextView = (TextView) view.findViewById(R.id.headline_text);
        }

    }
    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
