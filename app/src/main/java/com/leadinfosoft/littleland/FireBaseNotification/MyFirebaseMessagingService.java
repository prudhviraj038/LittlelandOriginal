package com.leadinfosoft.littleland.FireBaseNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.FirstActivity;
import com.leadinfosoft.littleland.activity.LoginActivity;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    Context context;
    JSONObject jsonObjectMessage = null;
    Bitmap bitmap_smallimage = null;
    Bitmap bitmap_largeicon = null;

    SharedPreferencesClass sharedPreferencesClass;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Logger.e("Notification message =======> " + remoteMessage + "");
        Logger.e("Notification message getData =======> " + remoteMessage.getData() + "");

        context = getApplicationContext();
        sharedPreferencesClass = new SharedPreferencesClass(context);


        if (remoteMessage.getData().size() > 0) {
            //Log.e("20/01 ", "Message data payload: " + remoteMessage.getData());
            //Log.e("20/01 hello 4 getdata", remoteMessage.getData().get("msg_body"));

            String message = remoteMessage.getData().get("message");
            Logger.e("Notification message new Test =======> " + message + "");

            try {
                jsonObjectMessage = new JSONObject(message);
                Logger.e("Notification jsonObjectMessage image_url==========> " + jsonObjectMessage.optString("image_url"));

                String msg_title = jsonObjectMessage.optString("msg_title");
                String msg_body = jsonObjectMessage.optString("msg_body");
                String image_url = "";
                String big_image = "";
                image_url = jsonObjectMessage.optString("image_url");
                big_image = jsonObjectMessage.optString("big_image");

                String ref_id = jsonObjectMessage.optString("ref_id");
                String msg_type = jsonObjectMessage.optString("msg_type");

               /* image_url = small icon ma use krvanu.
                big_image = big picture style notification.*/

                if (!image_url.equalsIgnoreCase("") || image_url.length() != 0) {

                    bitmap_smallimage = getBitmapfromUrl(image_url);
                    bitmap_largeicon = getBitmapfromUrl(image_url);
                    sendNotification2(msg_title, msg_body, bitmap_smallimage);

                } else if (!big_image.equalsIgnoreCase("") || big_image.length() != 0) {

                    bitmap_smallimage = getBitmapfromUrl(big_image);
                    bitmap_largeicon = getBitmapfromUrl(big_image);
                    sendNotification1(msg_title, msg_body, bitmap_smallimage);

                } else {
                    sendNotification(msg_title, msg_body);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.


       /* if (remoteMessage != null && remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }*/


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification1(String msg_title, String msg_body, Bitmap image_url) {
        Intent intent;

        Logger.e("30/08 is login =====> "+sharedPreferencesClass.getIs_Login());

        if (sharedPreferencesClass.getIs_Login().equalsIgnoreCase("true")) {
            intent = new Intent(this, MainActivity.class);

        } else {
            intent = new Intent(this, FirstActivity.class);

        }

        intent.putExtra("fromnotification", "true");
        intent.putExtra("msg_type", jsonObjectMessage + "");

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(image_url)
                .setContentTitle(msg_title)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image_url))/*Notification with Image*/
                .setContentText(msg_body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    private void sendNotification2(String msg_title, String msg_body, Bitmap image_url) {

        Intent intent;

        Logger.e("30/08 is login =====> "+sharedPreferencesClass.getIs_Login());


        if (sharedPreferencesClass.getIs_Login().equalsIgnoreCase("true")) {
            intent = new Intent(this, MainActivity.class);

        } else {
            intent = new Intent(this, FirstActivity.class);

        }


        intent.putExtra("fromnotification", "true");
        intent.putExtra("msg_type", jsonObjectMessage + "");

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(image_url)
                .setContentTitle(msg_title)
                .setContentText(msg_body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

  /*  @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Logger.e("03/07 ====> " + "call onMessageReceived");

        Logger.e("03/07 remoteMessage====> " + remoteMessage + "");


        // TODO(developer): Handle FCM messages here.
        Logger.e("03/07 ====> " + "From: ====> " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logger.e("03/07 ======> " + "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.e("03/07 ======> " + "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
*/

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent;

        Logger.e("30/08 is login =====> "+sharedPreferencesClass.getIs_Login());

        if (sharedPreferencesClass.getIs_Login().equalsIgnoreCase("true")) {
            intent = new Intent(this, MainActivity.class);

        } else {
            intent = new Intent(this, FirstActivity.class);

        }

        intent.putExtra("fromnotification", "true");
        intent.putExtra("msg_type", jsonObjectMessage + "");

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
