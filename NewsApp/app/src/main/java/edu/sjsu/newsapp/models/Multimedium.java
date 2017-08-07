
package edu.sjsu.newsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedium implements Parcelable {

    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("legacy")
    @Expose
    private Legacy legacy;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Legacy getLegacy() {
        return legacy;
    }

    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.width);
        dest.writeString(this.url);
        dest.writeValue(this.rank);
        dest.writeValue(this.height);
        dest.writeString(this.subtype);
        dest.writeParcelable(this.legacy, flags);
        dest.writeString(this.type);
    }

    public Multimedium() {
    }

    protected Multimedium(Parcel in) {
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        this.url = in.readString();
        this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.subtype = in.readString();
        this.legacy = in.readParcelable(Legacy.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Multimedium> CREATOR = new Parcelable.Creator<Multimedium>() {
        @Override
        public Multimedium createFromParcel(Parcel source) {
            return new Multimedium(source);
        }

        @Override
        public Multimedium[] newArray(int size) {
            return new Multimedium[size];
        }
    };
}
