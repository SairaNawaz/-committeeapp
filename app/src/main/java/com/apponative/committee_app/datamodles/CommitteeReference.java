package com.apponative.committee_app.datamodles;

/**
 * Created by Muhammad Waqas on 4/27/2017.
 */

public class CommitteeReference {

    private String cid;
    private String description;
    private String admin;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommitteeReference(String cid,String admin ,String description) {
        this.cid = cid;
        this.description = description;
        this.admin = admin;
    }

    public CommitteeReference(String admin, String description){
        this.admin = admin;
        this.description = description;
    }
    public CommitteeReference(){}
}
