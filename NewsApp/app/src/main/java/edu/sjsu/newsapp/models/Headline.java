
package edu.sjsu.newsapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Headline implements Parcelable {

    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("print_headline")
    @Expose
    private String printHeadline;
    @SerializedName("kicker")
    @Expose
    private String kicker;
    @SerializedName("content_kicker")
    @Expose
    private String contentKicker;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }

    public void setPrintHeadline(String printHeadline) {
        this.printHeadline = printHeadline;
    }

    public String getKicker() {
        return kicker;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    public String getContentKicker() {
        return contentKicker;
    }

    public void setContentKicker(String contentKicker) {
        this.contentKicker = contentKicker;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.main);
        dest.writeString(this.printHeadline);
        dest.writeString(this.kicker);
        dest.writeString(this.contentKicker);
    }

    public Headline() {
    }

    protected Headline(Parcel in) {
        this.main = in.readString();
        this.printHeadline = in.readString();
        this.kicker = in.readString();
        this.contentKicker = in.readString();
    }

    public static final Parcelable.Creator<Headline> CREATOR = new Parcelable.Creator<Headline>() {
        @Override
        public Headline createFromParcel(Parcel source) {
            return new Headline(source);
        }

        @Override
        public Headline[] newArray(int size) {
            return new Headline[size];
        }
    };
}
