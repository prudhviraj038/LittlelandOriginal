package com.leadinfosoft.littleland.ModelClass;

/**
 * Created by radhe on 8/23/2017.
 */
public class UploadMulipleFileNewPost {

    /*"full_path" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/temp\/20170823_0241_eaa1da31f7product1.png,
            "name" : 20170823_0241_eaa1da31f7product1.png*/

    String full_path = "";
    String name = "";
    String file_type = "";
    String thumb = "";


    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
