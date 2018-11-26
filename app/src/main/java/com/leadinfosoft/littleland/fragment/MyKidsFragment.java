package com.leadinfosoft.littleland.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.BuildConfig;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.FilePath;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.MyKidsListModelClass;
import com.leadinfosoft.littleland.ModelClass.NotificationListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.adapter.MyKidsListAdapter;
import com.leadinfosoft.littleland.adapter.NotificationListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MyKidsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    ListView lv_kids;
    TextView tv_no_notifications;
    boolean isEnglish;


    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    MyKidsListAdapter myKidsListAdapter = null;

    String str_offset = "0";

    ArrayList<MyKidsListModelClass> myKidsListModelClassArrayList = new ArrayList<>();

    int int_selected_adapter_position;

    String str_selected_uid = "";

    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;

    final int PIC_CROP = 3;

    final int PICK_IMAGE_REQUEST = 2;

    private Bitmap bitmapgallery;

    String image = "";

    private String selectedFilePath = "";

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;


    public MyKidsFragment() {
        // Required empty public constructor
    }

    public static MyKidsFragment newInstance(String param1, String param2) {
        MyKidsFragment fragment = new MyKidsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_get_kids_ar, container, false);
            isEnglish = false;

        } else {
            rootView = inflater.inflate(R.layout.fragment_get_kids, container, false);

            isEnglish = true;

        }

        initHeader();

        init(rootView);

        setTypeface_Text();

        GetMyKidsListWebService();

        return rootView;
    }

    private void init(View rootView) {

        lv_kids = (ListView) rootView.findViewById(R.id.lv_kids);

        tv_no_notifications = (TextView) rootView.findViewById(R.id.tv_no_notifications);
        tv_no_notifications.setVisibility(View.GONE);

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            MainActivity.tv_title.setText(Common.filter("lbl_MY_KIDS", "ar"));

        } else {
            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            MainActivity.tv_title.setText(Common.filter("lbl_MY_KIDS", "en"));
        }


    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
        customDialogClass = new CustomDialogClass((Activity) context);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        }

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(context.getAssets(), fontPath_numeric);

    }

    private void initHeader() {

        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.VISIBLE);


        getActivity().findViewById(R.id.iv_add).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.INVISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        if (isEnglish) {
            ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("MY KIDS");
            ((MyTextView) getActivity().findViewById(R.id.tv_subtitle)).setVisibility(View.GONE);
            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());
        } else {
            ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("رأي آخر");
            ((MyTextView) getActivity().findViewById(R.id.tv_subtitle)).setVisibility(View.GONE);
            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

        }

        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }


    public void GetMyKidsListWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetMyKidsListWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess.length() > 0) {

                            tv_no_notifications.setVisibility(View.GONE);

                            myKidsListModelClassArrayList.clear();
                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);


                              /*  "uid" : 199,
                                        "name" : Parent Child1 Parent,
                                        "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/default-user.png*/


                                String uid = jsonObject1.optString("uid");
                                String name = jsonObject1.optString("name");
                                String profile_pic = jsonObject1.optString("profile_pic");

                                MyKidsListModelClass myKidsListModelClass = new MyKidsListModelClass();
                                myKidsListModelClass.setUid(uid);
                                myKidsListModelClass.setName(name);
                                myKidsListModelClass.setProfile_pic(profile_pic);

                                myKidsListModelClassArrayList.add(myKidsListModelClass);


                            }

                        } else {
                            myKidsListModelClassArrayList.clear();

                            tv_no_notifications.setVisibility(View.VISIBLE);


                        }

                        myKidsListAdapter = new MyKidsListAdapter(context, myKidsListModelClassArrayList);
                        lv_kids.setAdapter(myKidsListAdapter);
                        myKidsListAdapter.notifyDataSetChanged();
                        lv_kids.setEmptyView(tv_no_notifications);

                        myKidsListAdapter.setCustomEventListener(new MyKidsListAdapter.OnKidsImageListener() {
                            @Override
                            public void onEvent(int position, String uid) {
//                                Toast.makeText(context, "uid click =====>" + uid, Toast.LENGTH_SHORT).show();
                                int_selected_adapter_position = position;
                                str_selected_uid = uid;
//                                pickImage();

                                setPermission();


                            }
                        });


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


       /* action	REQUIRED	get_kids
        uid	REQUIRED	User ID - Logged-in parent's UID*/

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("action", "get_kids"));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetMyKidsListWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_get_data);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    private void setPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!Common.hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
        } else {
            pickImage();

        }
    }

    private void pickImage() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery_ar);

        } else {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery);

        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_main = (LinearLayout) dialog.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView iv_new_gallery = (ImageView) dialog.findViewById(R.id.iv_new_gallery);
        iv_new_gallery.setVisibility(View.VISIBLE);


        ImageView iv_new_camera = (ImageView) dialog
                .findViewById(R.id.iv_new_camera);
        iv_new_camera.setVisibility(View.VISIBLE);

        iv_new_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    //use standard intent to capture an image

                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;

                    String image_name = "/" + "picture" + i1 + "" + ".jpg";

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + image_name*//*"/picture.jpg"*//*;
                    File imageFile = new File(imageFilePath);
                    picUri = Uri.fromFile(imageFile); // convert path to Uri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }*/

                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                dialog.dismiss();
            }
        });

        iv_new_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                dialog.dismiss();


            }
        });

        dialog.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        selectedFilePath = "file:" + image.getAbsolutePath();
        Logger.e("12/01 mCurrentPhotoPath ----->" + selectedFilePath + "");


        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            photoFile = createImageFile();

            Logger.e("12/01 photoFile ------>" + photoFile + "");

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.e("22/08 requestCode ===> " + requestCode + "");
        Logger.e("22/08 resultCode ===> " + resultCode + "");

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == CAMERA_CAPTURE) {


                Logger.e("12/01 CAMERA_CAPTURE mCurrentPhotoPath ----> " + selectedFilePath + "");

                Uri imageUri = Uri.parse(selectedFilePath);
                File file = new File(imageUri.getPath());

                Logger.e("12/01 imageUri getPath -----> " + imageUri.getPath() + "");

                try {
                    InputStream ims = new FileInputStream(file);
//                    ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                if (file.exists()) {

                    Logger.e("12/01 file exists");
                }
//Do something
                else {
                    Logger.e("12/01 file not exists");

                    return;
                }

                selectedFilePath = imageUri.getPath();

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    InputStream ims = null;
                    try {
                        ims = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(ims);

                    new Rotation_Check(bitmap, imageUri).execute();
/*
                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(context,
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                                Logger.e("12/01 onScanCompleted ------> " + "Done");

//                                Toast.makeText(context, "onScanCompleted", Toast.LENGTH_SHORT).show();

                            }
                        });


//get the Uri for the captured image
               /* Uri uri = picUri;

                if (picUri == null) {
                    //no data present
                    return;
                }

//            Toast.makeText(context, "picUri ====>" + picUri + "", Toast.LENGTH_SHORT).show();

                selectedFilePath = FilePath.getPath(context, uri);
                Logger.e("28/08 Camera ====> " + "Selected Camera File Path:" + selectedFilePath);

                File file = new File(String.valueOf(selectedFilePath));
                if (file.exists()) {
                }
//Do something
                else {
                    return;
                }

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }*/
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");

                bitmapgallery = thePic;

                if (bitmapgallery == null) {
                    // set the toast for select image
                } else {
                    image = getStringImage(bitmapgallery);
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }


                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == PICK_IMAGE_REQUEST) {

                if (data == null) {
                    //no data present
                    return;
                }

                picUri = data.getData();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

//            performCrop();
            }
        }
    }

    class Rotation_Check extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap_new;
        Uri imageUri_new;

        public Rotation_Check(Bitmap bitmap, Uri imageUri) {

            this.bitmap_new = bitmap;
            this.imageUri_new = imageUri;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            customDialogClass.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Record method

            try {


                Bitmap bitmap_rotate = rotateImageIfRequired(bitmap_new, context, imageUri_new);
                Logger.e("29/01 bitmap_rotate ----> " + bitmap_rotate + "");

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(selectedFilePath);
                    bitmap_rotate.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (con.isConnectingToInternet()) {
                UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

      /*  if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {*/
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Logger.e("orientation: %s" + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
//        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    private void performCrop() {
        try {
//call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//set crop properties
            cropIntent.putExtra("crop", "true");
//indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
//indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
//retrieve data on return
            cropIntent.putExtra("return-data", true);
//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        String encodedImage = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return encodedImage;
    }

    class UploadProfilePicWebService extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogClass.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String s = "";

            int i = 0;
            while (i <= 50) {
                try {
                    Thread.sleep(50);
                    publishProgress(i);
                    i++;
                } catch (Exception e) {
                    Log.i("makemachine", e.getMessage());
                }
            }

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(Common.URL_save_pic);
            MultipartEntity reqEntity = new MultipartEntity();

            try {
                reqEntity.addPart("uid", new StringBody(new String(str_selected_uid)));
                reqEntity.addPart("lan", new StringBody(new String(sharedPreferencesClass.getSelected_Language())));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            reqEntity.addPart("file", new FileBody(new File(selectedFilePath)));

            Logger.e("30/08 my kids Upload Pic params ======> " + reqEntity + "");

            httppost.setEntity(reqEntity);
            HttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
                customDialogClass.dismiss();

            }
            HttpEntity resEntity = response.getEntity();
            httpclient.getConnectionManager().shutdown();

            try {
                return EntityUtils.toString(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
                customDialogClass.dismiss();


                return null;
            }

           /* HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httppost = new HttpPost(SERVER_URL);

            List<NameValuePair> list = new ArrayList<NameValuePair>();


            list.add(new BasicNameValuePair("referral_code", "" + ""));

            Log.e("01/05 social login params", list + "");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(list));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();

            }

            try {
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);

            } catch (Exception e) {
                e.printStackTrace();

            }*/

//            logClass.Log(context, "Read Response", s + "");

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Logger.e("23/08 progress values======>" + values.length + "");
            Logger.e("23/08 progress values======>" + values + "");

            Logger.e("23/08 progress values 1111111111======>" + (values[0] * 2) + "%" + "");
            Logger.e("23/08 progress values 2222222222======>" + values[0] + "");


         /*   _percentField.setText((values[0] * 2) + "%");
            _percentField.setTextSize(values[0]);*/

        }

        @Override
        protected void onPostExecute(String result) {
            Logger.e("23/08 file upload response ==========>" + result + "");

            if (customDialogClass.isShowing()) {
                customDialogClass.dismiss();

            }

            try {


                if (result != null) {
                    Logger.e("23/08 not null ======> " + "");
                } else {
                    return;
                }

                String myJSON = result;

                JSONObject jsonObject = new JSONObject(myJSON);

                String error = jsonObject.optString("error");

                if (error.equalsIgnoreCase("") || error.length() == 0) {

                    String success = jsonObject.optString("success");
                    Toast.makeText(context, success, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObjectuser = jsonObject.optJSONObject("user");

                    String profile_pic = jsonObjectuser.optString("profile_pic");

                    MyKidsListModelClass kidsListModelClass = myKidsListModelClassArrayList.get(int_selected_adapter_position);
                    kidsListModelClass.setProfile_pic(profile_pic);
                    myKidsListModelClassArrayList.set(int_selected_adapter_position, kidsListModelClass);

                    myKidsListAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
