package com.mike.project_2.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mike.project_2.R;

/**
 * Created by mng on 2/25/15.
 */
public class SearchOptions implements Parcelable{
    public String imageSize;
    public String colorFilter;
    public String imageType;
    public String site;
    public String searchTerm;
    public String start;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(imageSize);
        out.writeString(colorFilter);
        out.writeString(imageType);
        out.writeString(site);
        out.writeString(searchTerm);
        out.writeString(start);
    }

    public static final Parcelable.Creator<SearchOptions> CREATOR
            = new Parcelable.Creator<SearchOptions>() {
        @Override
        public SearchOptions createFromParcel(Parcel in) {
            return new SearchOptions(in);
        }

        @Override
        public SearchOptions[] newArray(int size) {
            return new SearchOptions[size];
        }
    };

    private SearchOptions(Parcel in) {
        imageSize = in.readString();
        colorFilter = in.readString();
        imageType = in.readString();
        site = in.readString();
        searchTerm = in.readString();
        start = in.readString();
    }

    public SearchOptions(String query) {
        searchTerm = query;

        // default values
        imageSize = "any";
        colorFilter = "any";
        imageType = "any";
    }
}
