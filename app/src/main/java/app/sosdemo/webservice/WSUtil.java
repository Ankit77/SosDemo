package app.sosdemo.webservice;

import android.util.Log;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.sosdemo.util.WriteLog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Web Service utility class to call web urls. And returns response.
 */
public class WSUtil {


    public String callServiceHttpPost(final String url, final RequestBody requestBody) {

        Log.e(WSUtil.class.getSimpleName(), String.format("Request String : %s", requestBody.toString()));

        String responseString = "";

        try {
            final OkHttpClient okHttpClient = new OkHttpClient();

//            okHttpClient.setConnectTimeout(WSConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
//            okHttpClient.setReadTimeout(WSConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

//            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), jsonRequest);


            final Request request = new Request.Builder()
                    .url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(requestBody)
                    .build();

            final Response response = okHttpClient.newCall(request).execute();

//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }
            responseString = response.body().string();

            Log.e(WSUtil.class.getSimpleName(), String.format("Response String : %s", responseString));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WSUtil", e.getMessage());
            responseString = WSConstants.getNetworkError();
        }

        return responseString;

    }

    public String callServiceHttpGet(final String url) {

        WriteLog.E("URL", url);
        String responseString = "";

        try {
            final OkHttpClient okHttpClient = new OkHttpClient();

//            okHttpClient.setConnectTimeout(WSConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
//            okHttpClient.setReadTimeout(WSConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);

//            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), jsonRequest);


            final Request request = new Request.Builder()
                    .url(url).addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .get()
                    .build();

            final Response response = okHttpClient.newCall(request).execute();

//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }
            responseString = response.body().string();

            Log.e(WSUtil.class.getSimpleName(), String.format("Response String : %s", responseString));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("WSUtil", e.getMessage());
            responseString = WSConstants.getNetworkError();
        }

        return responseString;

    }

}
