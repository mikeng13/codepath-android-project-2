package com.mike.project_2.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mng on 2/25/15.
 */
public class Image implements Parcelable {
    public String imageUrl;
    public String thumbnailUrl;
    public String title;
    public String content;
    public String originalContextUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(thumbnailUrl);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(originalContextUrl);
    }

    public static final Parcelable.Creator<Image> CREATOR
            = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    private Image(Parcel in) {
        imageUrl = in.readString();
        thumbnailUrl = in.readString();
        title = in.readString();
        content = in.readString();
        originalContextUrl = in.readString();
    }

    public Image() {}
}
