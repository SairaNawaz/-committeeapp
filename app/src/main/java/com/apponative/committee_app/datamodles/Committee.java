package com.apponative.committee_app.datamodles;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Muhammad Waqas on 4/27/2017.
 */

public class Committee implements Parcelable {

    private int mData;

    private String cid;
    private int uid;
    private String cname;
    private String admin;
    private String adminName;
    private int member_payment;
    private int member_count;
    private int amount;
    private String currency;
    private int interval;
    private int status; //0 ->open 1 -> close
    private String start_date;
    private String end_date;
    private Date create_date;
    private String payment_collection_date;
    private int payment_collection_mode; // by admin , by members
    private boolean adminfirst; //0 -> true , 1 -> false
    private HashMap<String, People> members_joined;
    private HashMap<String, People> members_confirmed;
    private String c_desc;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getC_desc() {
        return c_desc;
    }

    public void setC_desc(String c_desc) {
        this.c_desc = c_desc;
    }

    public HashMap<String, People> getMembers_confirmed() {
        return members_confirmed;
    }

    public void setMembers_confirmed(HashMap<String, People> members_confirmed) {
        this.members_confirmed = members_confirmed;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getPayment_collection_date() {
        return payment_collection_date;
    }

    public void setPayment_collection_date(String payment_collection_date) {
        this.payment_collection_date = payment_collection_date;
    }

    public int getPayment_collection_mode() {
        return payment_collection_mode;
    }

    public void setPayment_collection_mode(int payment_collection_mode) {
        this.payment_collection_mode = payment_collection_mode;
    }

    public boolean isAdminfirst() {
        return adminfirst;
    }

    public void setAdminfirst(boolean adminfirst) {
        this.adminfirst = adminfirst;
    }

    public HashMap<String, People> getMembers_joined() {
        return members_joined;
    }

    public void setMembers_joined(HashMap<String, People> members_joined) {
        this.members_joined = members_joined;
    }

    public int getMember_payment() {
        return member_payment;
    }

    public void setMember_payment(int member_payment) {
        this.member_payment = member_payment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Committee> CREATOR = new Parcelable.Creator<Committee>() {
        public Committee createFromParcel(Parcel in) {
            return new Committee(in);
        }

        public Committee[] newArray(int size) {
            return new Committee[size];
        }
    };

    private Committee(Parcel in) {
        mData = in.readInt();
    }

    public Committee() {

    }
}
