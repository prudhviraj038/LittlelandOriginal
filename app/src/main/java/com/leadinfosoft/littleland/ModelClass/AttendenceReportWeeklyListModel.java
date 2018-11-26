package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by radhe on 8/25/2017.
 */
public class AttendenceReportWeeklyListModel {

 /*
    "": "2017-08-25",
            "": "absent",
            "": ""
 */

    String date = "";
    String status = "";
    String remark = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
