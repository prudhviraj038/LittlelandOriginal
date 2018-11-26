package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by radhe on 8/25/2017.
 */
public class AttendenceReportListModel {

    /*"month" : August,
            "year" : 2017,
            "present" : 0,
            "absent" : 0
    */

    String month = "";
    String year = "";
    String present = "";
    String absent = "";

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }
}
