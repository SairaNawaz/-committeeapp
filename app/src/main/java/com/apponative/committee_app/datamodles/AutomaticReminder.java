package com.apponative.committee_app.datamodles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Waqas on 4/28/2017.
 */

public class AutomaticReminder implements Parcelable {

    private int mData;

    private String cId;
    private String turn;
    private String cname;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<AutomaticReminder> CREATOR = new Creator<AutomaticReminder>() {
        public AutomaticReminder createFromParcel(Parcel in) {
            return new AutomaticReminder(in);
        }

        public AutomaticReminder[] newArray(int size) {
            return new AutomaticReminder[size];
        }
    };

    private AutomaticReminder(Parcel in) {
        mData = in.readInt();
    }

    public AutomaticReminder(String cId, String turn, String cname) {
        this.cId = cId;
        this.turn = turn;
        this.cname = cname;
    }
}