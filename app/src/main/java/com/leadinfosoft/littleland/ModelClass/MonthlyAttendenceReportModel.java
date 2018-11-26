package com.leadinfosoft.littleland.ModelClass;

import java.io.Serializable;

/**
 * Created by radhe on 8/25/2017.
 */
public class MonthlyAttendenceReportModel implements Serializable{

   /* "uid" : 666,
            "fname" : Abdulatif,
            "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/48696e2e2b4b8adb9707991904d6e46d17.jpg,
            "report" : +[ ... ]*/

    String uid = "";
    String fname = "";
    String profile_pic = "";
    String report = "";


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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
