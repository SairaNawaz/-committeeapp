package com.apponative.committee_app.datamodles;

import com.apponative.committee_app.utils.Constants;

/**
 * Created by Muhammad Waqas on 4/28/2017.
 */

public class Notification {

    private String title;
    private String message;
    private String sentFrom;
    private String sentTo;
    private String cid;
    private String desc;
    private String admin;
    private Constants.NTYPE type;

    public Constants.NTYPE getType() {
        return type;
    }

    public void setType(Constants.NTYPE type) {
        this.type = type;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Notification() {

    }

    public Notification(Constants.NTYPE type, String cid, String title, String message, String sentFrom, String sentTo) {
        this.cid = cid;
        this.type = type;
        this.title = title;
        this.message = message;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
    }

    public Notification(String title, String message, String sentFrom,  String cid, Constants.NTYPE type) {
        this.title = title;
        this.message = message;
        this.sentFrom = sentFrom;
        this.cid = cid;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (sentFrom != null ? !sentFrom.equals(that.sentFrom) : that.sentFrom != null)
            return false;
        if (sentTo != null ? !sentTo.equals(that.sentTo) : that.sentTo != null) return false;
        if (cid != null ? !cid.equals(that.cid) : that.cid != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (admin != null ? !admin.equals(that.admin) : that.admin != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (sentFrom != null ? sentFrom.hashCode() : 0);
        result = 31 * result + (sentTo != null ? sentTo.hashCode() : 0);
        result = 31 * result + (cid != null ? cid.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}