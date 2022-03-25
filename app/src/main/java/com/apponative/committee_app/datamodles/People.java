package com.apponative.committee_app.datamodles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad Waqas on 4/28/2017.
 */

public class People implements Parcelable {

    private int mData;

    private String ContactName;
    private int ContactUsingApp;
    private String ContactNumber;
    private String Turn;
    private boolean PaymentStatus;

    public boolean isPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getTurn() {
        return Turn;
    }

    public void setTurn(String turn) {
        Turn = turn;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public int getContactUsingApp() {
        return ContactUsingApp;
    }

    public void setContactUsingApp(int contactUsingApp) {
        ContactUsingApp = contactUsingApp;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>() {
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        public People[] newArray(int size) {
            return new People[size];
        }
    };

    private People(Parcel in) {
        mData = in.readInt();
    }

    public People() {
    }

    public People(String ContactNumber, String ContactName) {
        this.ContactNumber = ContactNumber;
        this.ContactName = ContactName;
    }

    public People(String ContactName, String ContactNumber, String turn) {
        this.ContactNumber = ContactNumber;
        this.ContactName = ContactName;
        this.Turn = turn;
    }
}