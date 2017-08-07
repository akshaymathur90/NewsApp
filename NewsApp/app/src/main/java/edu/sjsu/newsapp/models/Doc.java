
package edu.sjsu.newsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc implements Parcelable {

    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("multimedia")
    @Expose
    private List<Multimedium> multimedia = null;
    @SerializedName("headline")
    @Expose
    private Headline headline;
    @SerializedName("pub_date")
    @Expose
    private String pubDate;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeList(this.multimedia);
        dest.writeParcelable(this.headline, flags);
        dest.writeString(this.pubDate);
    }

    public Doc() {
    }

    protected Doc(Parcel in) {
        this.webUrl = in.readString();
        this.multimedia = new ArrayList<Multimedium>();
        in.readList(this.multimedia, Multimedium.class.getClassLoader());
        this.headline = in.readParcelable(Headline.class.getClassLoader());
        this.pubDate = in.readString();
    }

    public static final Parcelable.Creator<Doc> CREATOR = new Parcelable.Creator<Doc>() {
        @Override
        public Doc createFromParcel(Parcel source) {
            return new Doc(source);
        }

        @Override
        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };
}
