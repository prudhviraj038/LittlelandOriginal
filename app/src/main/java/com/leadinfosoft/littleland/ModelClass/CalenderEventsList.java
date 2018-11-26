package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by admin on 8/8/2017.
 */

public class CalenderEventsList {

    /*"id":"1",
            "datetime":"2017-08-02 00:00:00",
            "color":"FF0055",
            "title":"",
            "title_ar":"",
            "note":"Parent meeting",
            "note_ar":"\u0627\u062c\u062a\u0645\u0627\u0639 \u0627\u0644\u0648\u0627\u0644\u062f\u064a\u0646"*/

    String id = "";
    String datetime = "";
    String color = "";
    String title = "";
    String title_ar = "";
    String note = "";
    String note_ar = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_ar() {
        return note_ar;
    }

    public void setNote_ar(String note_ar) {
        this.note_ar = note_ar;
    }
}
