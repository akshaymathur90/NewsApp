
package edu.sjsu.newsapp.models.querystories;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Legacy implements Parcelable {

    @SerializedName("xlargewidth")
    @Expose
    private Integer xlargewidth;
    @SerializedName("xlarge")
    @Expose
    private String xlarge;
    @SerializedName("xlargeheight")
    @Expose
    private Integer xlargeheight;
    @SerializedName("wide")
    @Expose
    private String wide;
    @SerializedName("widewidth")
    @Expose
    private Integer widewidth;
    @SerializedName("wideheight")
    @Expose
    private Integer wideheight;
    @SerializedName("thumbnailheight")
    @Expose
    private Integer thumbnailheight;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("thumbnailwidth")
    @Expose
    private Integer thumbnailwidth;

    public Integer getXlargewidth() {
        return xlargewidth;
    }

    public void setXlargewidth(Integer xlargewidth) {
        this.xlargewidth = xlargewidth;
    }

    public String getXlarge() {
        return xlarge;
    }

    public void setXlarge(String xlarge) {
        this.xlarge = xlarge;
    }

    public Integer getXlargeheight() {
        return xlargeheight;
    }

    public void setXlargeheight(Integer xlargeheight) {
        this.xlargeheight = xlargeheight;
    }

    public String getWide() {
        return wide;
    }

    public void setWide(String wide) {
        this.wide = wide;
    }

    public Integer getWidewidth() {
        return widewidth;
    }

    public void setWidewidth(Integer widewidth) {
        this.widewidth = widewidth;
    }

    public Integer getWideheight() {
        return wideheight;
    }

    public void setWideheight(Integer wideheight) {
        this.wideheight = wideheight;
    }

    public Integer getThumbnailheight() {
        return thumbnailheight;
    }

    public void setThumbnailheight(Integer thumbnailheight) {
        this.thumbnailheight = thumbnailheight;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getThumbnailwidth() {
        return thumbnailwidth;
    }

    public void setThumbnailwidth(Integer thumbnailwidth) {
        this.thumbnailwidth = thumbnailwidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.xlargewidth);
        dest.writeString(this.xlarge);
        dest.writeValue(this.xlargeheight);
        dest.writeString(this.wide);
        dest.writeValue(this.widewidth);
        dest.writeValue(this.wideheight);
        dest.writeValue(this.thumbnailheight);
        dest.writeString(this.thumbnail);
        dest.writeValue(this.thumbnailwidth);
    }

    public Legacy() {
    }

    protected Legacy(Parcel in) {
        this.xlargewidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.xlarge = in.readString();
        this.xlargeheight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.wide = in.readString();
        this.widewidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.wideheight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.thumbnailheight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.thumbnail = in.readString();
        this.thumbnailwidth = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Legacy> CREATOR = new Parcelable.Creator<Legacy>() {
        @Override
        public Legacy createFromParcel(Parcel source) {
            return new Legacy(source);
        }

        @Override
        public Legacy[] newArray(int size) {
            return new Legacy[size];
        }
    };
}
