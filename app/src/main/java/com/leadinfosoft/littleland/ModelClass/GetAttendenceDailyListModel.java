package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by admin on 8/8/2017.
 */

public class GetAttendenceDailyListModel {

    /*"uid":"12",
            "fname":"Teacher",
            "lname":"Demo",
            "profile_pic":"http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/default-user.png",
            "present":"1"*/

    String uid = "";
    String fname = "";
    String lname = "";
    String profile_pic = "";
    String present = "";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }
}
