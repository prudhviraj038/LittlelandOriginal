package com.leadinfosoft.littleland.Common;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.leadinfosoft.littleland.Dialog.CustomDialogClass;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class RequestMaker extends AsyncTask<String, String, String> {

    private Response_string<String> response;
    private ArrayList<NameValuePair> arguments_values;
    Context ctxt;
    Activity activity;
    //    ProgressDialog progress;
    private String method;

    CustomDialogClass customDialogClass;

    private static final int HTTP_CONNECTION_TIMEOUT = 150000;


    public RequestMaker(Response_string<String> resp,
                        ArrayList<NameValuePair> parameters, Context c) {
        response = resp;
        arguments_values = parameters;
        ctxt = c;
        this.method = "POST";
        customDialogClass = new CustomDialogClass((Activity) c);
        customDialogClass.show();
    }

    public RequestMaker(Response_string<String> resp,
                        ArrayList<NameValuePair> parameters, Activity activity) {
        response = resp;
        arguments_values = parameters;
        this.activity = activity;
        this.method = "POST";
        customDialogClass = new CustomDialogClass(activity);
        customDialogClass.show();
    }

    public RequestMaker(Response_string<String> resp,
                        ArrayList<NameValuePair> parameters, Context c, String Method) {
        response = resp;
        arguments_values = parameters;
        ctxt = c;
        this.method = Method;

        customDialogClass = new CustomDialogClass((Activity) c);
        customDialogClass.show();


    }

    @SuppressWarnings("deprecation")
    @Override
    protected String doInBackground(String... URL) {
        String reply1 = null;
        try {
            if (Common.IS_DEBUG) {
                Log.i(Common.TAG, "Executing " + method + ": " + URL[0]);
                Log.i(Common.TAG, "Params: " + arguments_values);
            }

            InputStream inStream = null;
//            HttpClient client = new DefaultHttpClient();

            HttpClient client = getNewHttpClient();


            HttpResponse response;

            if (method == "POST") {
                HttpPost post = new HttpPost(URL[0]);

                post.setHeader("Content-type", "application/x-www-form-urlencoded");
                post.setHeader("Accept", "application/json");
                // post.setHeader(Servicelist.AUTHORIZATION_HEADER,Servicelist.AUTHORIZATION_VAL);
                if (arguments_values != null) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            arguments_values, "UTF-8");
                    post.setEntity(entity);
                }
                response = client.execute(post);
            } else {
                String paramString = URLEncodedUtils.format(arguments_values, "utf-8");
                URL[0] += "?" + paramString;
                HttpGet httpGet = new HttpGet(URL[0]);
                response = client.execute(httpGet);
            }

            inStream = response.getEntity().getContent();
            StringBuffer sb = new StringBuffer();
            int chr;
            while ((chr = inStream.read()) != -1) {
                sb.append((char) chr);
            }
            reply1 = sb.toString();

        } catch (Exception e) {

            try {
                customDialogClass.dismiss();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
            return null;
        }
        return reply1;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if ((customDialogClass != null) && customDialogClass.isShowing()) {
            try {
                customDialogClass.dismiss();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

       /* if(customDialogClass.isShowing())
        {
            customDialogClass.dismiss();

        }*/

//        progress.dismiss();

        Logger.e("RequestMaker Response => " + result + "");

        if (result != null) {
            if (Common.IS_DEBUG)
                Log.w(Common.TAG, "Service Resp: " + result);
            response.OnRead_response(result);
        } else {
            Log.w(Common.TAG, "Server unavailable.");
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*try {

            customDialogClass = new CustomDialogClass((Activity) ctxt);

            customDialogClass.show();
        } catch (Exception e1) {
            e1.printStackTrace();
        }*/


    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
