package edu.sjsu.newsapp.models;

/**
 * Created by akshaymathur on 8/1/17.
 */

public class TopStories {

    String mHeadline;
    String mPubDate;
    String mWebUrl;
    String mThumbnail;

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public void setWebUrl(String webUrl) {
        mWebUrl = webUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }
}
