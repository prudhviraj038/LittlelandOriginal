package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by radhe on 8/22/2017.
 */
public class ViewPostMediaArrayModelClass {

    /*"media_type" : image,
            "" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/post.png
*/

    String media_type = "";
    String path = "";
    String thumb = "";

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
