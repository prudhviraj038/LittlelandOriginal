package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by radhe on 8/23/2017.
 */
public class MessageListModel {

//    "id" : 8,
//            "from_uid" : 1,
//            "from_name" : Parent Mother Of Abdulaziz Al-Shuwaib Al-Shuwaib,
//            "from_profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/default-user.png,
//            "to_uid" : 120,
//            "to_name" : Teacher Teacher,
//    "to_profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/default-user.png,
//            "body" : test with attachment,
//            "stamp" : 2017-08-23 14:11:29,
//            "attachment" : -[
//            ],
//            "is_sent" : 0


    String id = "";
    String from_uid = "";
    String from_name = "";
    String from_profile_pic = "";
    String to_uid = "";
    String to_name = "";
    String to_profile_pic = "";

    String body = "";
    String stamp = "";
    String attachment = "";

    String is_sent = "";

    Boolean onthespot = true;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_profile_pic() {
        return from_profile_pic;
    }

    public void setFrom_profile_pic(String from_profile_pic) {
        this.from_profile_pic = from_profile_pic;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_profile_pic() {
        return to_profile_pic;
    }

    public void setTo_profile_pic(String to_profile_pic) {
        this.to_profile_pic = to_profile_pic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getIs_sent() {
        return is_sent;
    }

    public void setIs_sent(String is_sent) {
        this.is_sent = is_sent;
    }

    public Boolean getOnthespot() {
        return onthespot;
    }

    public void setOnthespot(Boolean onthespot) {
        this.onthespot = onthespot;
    }
}
