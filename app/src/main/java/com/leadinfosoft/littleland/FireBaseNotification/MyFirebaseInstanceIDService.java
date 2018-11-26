package com.leadinfosoft.littleland.FireBaseNotification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import java.util.UUID;

/**
 * Created by Lead on 1/21/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    SharedPreferencesClass sharedPreferencesClass;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        Logger.e("03/05 onTokenRefresh" + "onTokenRefresh");

        sharedPreferencesClass = new SharedPreferencesClass(getApplicationContext());

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.e("03/05" + "Refreshed token: " + refreshedToken);

        String uniqueId = UUID.randomUUID().toString();

        if (!refreshedToken.equalsIgnoreCase("")) {
            sharedPreferencesClass.setDEVICE_TOKEN(refreshedToken);
            sharedPreferencesClass.setDEVICE_CODE_UUID(uniqueId);
            Logger.e("25/04 set token " + refreshedToken);
        } else {
            sharedPreferencesClass.setDEVICE_TOKEN("Pending");
            sharedPreferencesClass.setDEVICE_CODE_UUID(uniqueId);

//            ToastClass.makeText(getApplicationContext(),"user Token not found ", ToastClass.LENGTH_LONG).show();
        }

//
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
   /*private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        TinyDB db=new TinyDB(this);
        db.putString("DEV_TOKEN",token);

    }*/
}
