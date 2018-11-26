package com.leadinfosoft.littleland.utill;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lead on 7/17/2017.
 */

public class SharedPreferencesClass {

    public final String PREFS_NAME = "LITTLELAND";
    public final String USER_LANG = "user_lang";

    public final String USER_DETAILS = "user_details";

    public final String USER_Id = "uid";


    public final String DEVICE_TOKEN = "DEVICE_TOKEN_KEY";
    public final String DEVICE_CODE_UUID = "DEVICE_CODE_UUID_KEY";

    public final String Get_Initial_Days_Array = "Get_Initial_Days_Array_Key";

    public final String User_Type = "User_Type_Key";
    public final String Get_Initial_Class_Details_Array = "Get_Initial_Class_Details_Array_Key";

    public final String Get_Initial_Child_Details_Array = "Get_Initial_Child_Details_Array_Key";

    public final String Selected_Class_Id = "Selected_Class_Id_Key";
    public final String Selected_Class_Name = "Selected_Class_Name_Key";
    public final String Selected_Class_Name_ar = "Selected_Class_Name_ar_Key";


    public final String Selected_Child_Id = "Selected_Child_Id_Key";
    public final String Selected_Child_Name = "Selected_Child_Name_Key";


    public final String Is_Login = "Is_Login_Key";

    public final String Selected_Language = "Selected_Language_Key";

    public static final String Language_Response = "Language_Response_key";

    public static final String User_NAME = "User_NAME_key";

    public static final String User_qualification = "User_qualification_key";

    public static final String User_profile_pic = "User_profile_pic_key";

    public static final String First_Page_Slider_image_url = "First_Page_Slider_image_url_key";


    public static final String Login_Slider_image_url = "Login_Slider_image_url_key";

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferencesClass(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
    }

    public boolean getUserSelectLang() {
        boolean respontxt;
        respontxt = sharedPreferences.getBoolean(USER_LANG, false);
        return respontxt;
    }

    public void setUserSelectLang(boolean langflag) {
        editor = sharedPreferences.edit();
        editor.putBoolean(USER_LANG, langflag);
        editor.commit();
    }

    public String getUSER_DETAILS() {
        String respontxt;
        respontxt = sharedPreferences.getString(USER_DETAILS, "");
        return respontxt;
    }

    public void setUSER_DETAILS(String user_details) {
        editor = sharedPreferences.edit();
        editor.putString(USER_DETAILS, user_details);
        editor.commit();
    }

    public String getUSER_Id() {
        String respontxt;
        respontxt = sharedPreferences.getString(USER_Id, "");
        return respontxt;
    }

    public void setUSER_Id(String userId) {
        editor = sharedPreferences.edit();
        editor.putString(USER_Id, userId);
        editor.commit();
    }

    public String getDEVICE_TOKEN() {
        String respontxt;
        respontxt = sharedPreferences.getString(DEVICE_TOKEN, "");
        return respontxt;
    }

    public void setDEVICE_TOKEN(String device_token) {
        editor = sharedPreferences.edit();
        editor.putString(DEVICE_TOKEN, device_token);
        editor.commit();
    }

    public String getDEVICE_CODE_UUID() {
        String respontxt;
        respontxt = sharedPreferences.getString(DEVICE_CODE_UUID, "");
        return respontxt;
    }

    public void setDEVICE_CODE_UUID(String deviceCodeUuid) {
        editor = sharedPreferences.edit();
        editor.putString(DEVICE_CODE_UUID, deviceCodeUuid);
        editor.commit();
    }

    public String getGet_Initial_Days_Array() {
        String respontxt;
        respontxt = sharedPreferences.getString(Get_Initial_Days_Array, "");
        return respontxt;
    }

    public void setGet_Initial_Days_Array(String getInitialDaysArray) {
        editor = sharedPreferences.edit();
        editor.putString(Get_Initial_Days_Array, getInitialDaysArray);
        editor.commit();
    }

    public String getUser_Type() {
        String respontxt;
        respontxt = sharedPreferences.getString(User_Type, "");
        return respontxt;
    }

    public void setUser_Type(String userType) {
        editor = sharedPreferences.edit();
        editor.putString(User_Type, userType);
        editor.commit();
    }

    public String getGet_Initial_Class_Details_Array() {
        String respontxt;
        respontxt = sharedPreferences.getString(Get_Initial_Class_Details_Array, "");
        return respontxt;
    }

    public void setGet_Initial_Class_Details_Array(String get_Initial_Class_Details_Array) {
        editor = sharedPreferences.edit();
        editor.putString(Get_Initial_Class_Details_Array, get_Initial_Class_Details_Array);
        editor.commit();
    }

    public String getGet_Initial_Child_Details_Array() {
        String respontxt;
        respontxt = sharedPreferences.getString(Get_Initial_Child_Details_Array, "");
        return respontxt;
    }

    public void setGet_Initial_Child_Details_Array(String get_initial_child_details_array) {
        editor = sharedPreferences.edit();
        editor.putString(Get_Initial_Child_Details_Array, get_initial_child_details_array);
        editor.commit();
    }


    public String getSelected_Class_Id() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Class_Id, "");
        return respontxt;
    }

    public void setSelected_Class_Id(String selectedClassId) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Class_Id, selectedClassId);
        editor.commit();
    }

    public String getSelected_Class_Name() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Class_Name, "");
        return respontxt;
    }

    public void setSelected_Class_Name(String selectedClassName) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Class_Name, selectedClassName);
        editor.commit();
    }

    public String getSelected_Class_Name_ar() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Class_Name_ar, "");
        return respontxt;
    }

    public void setSelected_Class_Name_ar(String selectedClassName_ar) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Class_Name_ar, selectedClassName_ar);
        editor.commit();
    }

    public String getIs_Login() {
        String respontxt;
        respontxt = sharedPreferences.getString(Is_Login, "");

        return respontxt;
    }

    public void setIs_Login(String str_isLogin) {
        editor = sharedPreferences.edit();
        editor.putString(Is_Login, str_isLogin);
        editor.commit();
    }

    public String getSelected_Language() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Language, "");

        return respontxt;
    }

    public void setSelected_Language(String str_Selected_language) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Language, str_Selected_language);
        editor.commit();
    }

    public String getSelected_Child_Id() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Child_Id, "");

        return respontxt;
    }

    public void setSelected_Child_Id(String child_id) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Child_Id, child_id);
        editor.commit();
    }

    public String getSelected_Child_Name() {
        String respontxt;
        respontxt = sharedPreferences.getString(Selected_Child_Name, "");

        return respontxt;
    }

    public void setSelected_Child_Name(String child_name) {
        editor = sharedPreferences.edit();
        editor.putString(Selected_Child_Name, child_name);
        editor.commit();
    }
    public String getLanguage_Response() {
        String restoredText = sharedPreferences.getString(Language_Response, "");
        return restoredText;
    }

    public void setLanguage_Response(String str_language_response) {
        editor = sharedPreferences.edit();
        editor.putString(Language_Response, str_language_response); //3
        editor.commit();
    }
    public String getUser_NAME() {
        String restoredText = sharedPreferences.getString(User_NAME, "");
        return restoredText;
    }

    public void setUser_NAME(String str_User_NAME) {
        editor = sharedPreferences.edit();
        editor.putString(User_NAME, str_User_NAME); //3
        editor.commit();
    }

    public String getUser_qualification() {
        String restoredText = sharedPreferences.getString(User_qualification, "");
        return restoredText;
    }

    public void setUser_qualification(String str_User_qualification) {
        editor = sharedPreferences.edit();
        editor.putString(User_qualification, str_User_qualification); //3
        editor.commit();
    }


    public String getUser_profile_pic() {
        String restoredText = sharedPreferences.getString(User_profile_pic, "");
        return restoredText;
    }

    public void setUser_profile_pic(String str_User_profile_pic) {
        editor = sharedPreferences.edit();
        editor.putString(User_profile_pic, str_User_profile_pic); //3
        editor.commit();
    }

    public String getFirst_Page_Slider_image_url() {
        String restoredText = sharedPreferences.getString(First_Page_Slider_image_url, "");
        return restoredText;
    }

    public void setFirst_Page_Slider_image_url(String str_First_Page_Slider_image_url) {
        editor = sharedPreferences.edit();
        editor.putString(First_Page_Slider_image_url, str_First_Page_Slider_image_url); //3
        editor.commit();
    }

    public String getLogin_Slider_image_url() {
        String restoredText = sharedPreferences.getString(Login_Slider_image_url, "");
        return restoredText;
    }

    public void setLogin_Slider_image_url(String str_Login_Slider_image_url) {
        editor = sharedPreferences.edit();
        editor.putString(Login_Slider_image_url, str_Login_Slider_image_url); //3
        editor.commit();
    }


}
