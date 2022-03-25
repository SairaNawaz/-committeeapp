package com.apponative.committee_app.datamodles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Waqas on 4/19/2017.
 */

public class User implements Parcelable{
    private int mData;
    private String username;
    private String imagePath;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;

    }

    public User() {

    }

    public User(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }
    public User(String username) {
        this.username = username;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        mData = in.readInt();
    }

}
