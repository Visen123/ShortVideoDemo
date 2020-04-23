package com.tencent.liteav.demo.videouploader.videoupload.impl;

import android.text.TextUtils;
import android.util.Log;


import com.tencent.liteav.demo.videouploader.videoupload.impl.compute.TXOkHTTPEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * UGC Client
 */
public class UGCClient {
    private final static String TAG = "TVC-UGCClient";
    private String signature;
    private OkHttpClient okHttpClient;
    private TXOkHTTPEventListener mTXOkHTTPEventListener;
    public static String SERVER = "https://" + TVCConstants.VOD_SERVER_HOST + "/v3/index.php?Action=";
    private String serverIP = "";

    private static UGCClient ourInstance;

    public static UGCClient getInstance(String signature, int iTimeOut) {
        synchronized (UGCClient.class) {
            if (ourInstance == null) {
                ourInstance = new UGCClient(signature, iTimeOut);
            } else if (signature != null && !TextUtils.isEmpty(signature)) {
                ourInstance.updateSignature(signature);
            }
        }

        return ourInstance;
    }


    private UGCClient(String signature, int iTimeOut) {
        this.signature = signature;
        mTXOkHTTPEventListener = new TXOkHTTPEventListener();
        okHttpClient = new OkHttpClient().newBuilder()
                .dns(new HttpDNS())
                .connectTimeout(iTimeOut, TimeUnit.SECONDS)    // 设置超时时间
                .readTimeout(iTimeOut, TimeUnit.SECONDS)       // 设置读取超时时间
                .writeTimeout(iTimeOut, TimeUnit.SECONDS)      // 设置写入超时时间
                .addNetworkInterceptor(new LoggingInterceptor())
                .eventListener(mTXOkHTTPEventListener)
                .build();
    }


    /**
     * 预上传（UGC接口）
     *@param callback 回调
     */
    public void PrepareUploadUGC(Callback callback) {
        String reqUrl = SERVER + "PrepareUploadUGC";
        Log.d(TAG, "PrepareUploadUGC->request url:" + reqUrl);

        String body = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientVersion", TVCConstants.TVCVERSION);
            jsonObject.put("signature", signature);
            body = jsonObject.toString();
            Log.d(TAG, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发送head请求探测
     *@param callback 回调
     */
    public void detectDomain(String domain, Callback callback) {
        String reqUrl = "http://" + domain;
        Log.d(TAG, "detectDomain->request url:" + reqUrl);
        Request request = new Request.Builder()
                .url(reqUrl)
                .method("HEAD", null)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 申请上传（UGC接口）
     *
     * @param info     文件信息
     * @param customKey
     *@param callback 回调  @return
     */
    public int initUploadUGC(TVCUploadInfo info, String customKey, String vodSessionKey, Callback callback) {
        String reqUrl = SERVER + "ApplyUploadUGC";
        Log.d(TAG, "initUploadUGC->request url:" + reqUrl);

        String body = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("signature", signature);
            jsonObject.put("videoName", info.getFileName());
            jsonObject.put("videoType", info.getFileType());
            jsonObject.put("videoSize", info.getFileSize());

            // 判断是否需要上传封面
            if (info.isNeedCover()) {
                jsonObject.put("coverName",info.getCoverName());
                jsonObject.put("coverType",info.getCoverImgType());
                jsonObject.put("coverSize",info.getCoverFileSize());
            }
            jsonObject.put("clientReportId", customKey);
            jsonObject.put("clientVersion", TVCConstants.TVCVERSION);
            if (!TextUtils.isEmpty(vodSessionKey)) {
                jsonObject.put("vodSessionKey", vodSessionKey);
            }
            String region = TXUGCPublishOptCenter.getInstance().getCosRegion();
            if (!TextUtils.isEmpty(region)) {
                jsonObject.put("storageRegion", region);
            }
            body = jsonObject.toString();
            Log.d(TAG, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(requestBody)
                .build();
        if (TVCDnsCache.useProxy()) {
            final String host = request.url().host();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress address = InetAddress.getByName(host);
                        serverIP = address.getHostAddress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        okHttpClient.newCall(request).enqueue(callback);

        return TVCConstants.NO_ERROR;
    }

    /**
     * 上传结束(UGC接口)
     *
     * @param domain        视频上传的域名
     * @param vodSessionKey 视频上传的会话key
     * @param callback 回调
     * @return
     */
    public int finishUploadUGC(String domain, String customKey, String vodSessionKey, final Callback callback) {
//        String reqUrl = "https://" + domain + "/v3/index.php?Action=CommitUploadUGC";
        String reqUrl = SERVER + "CommitUploadUGC";
        Log.d(TAG, "finishUploadUGC->request url:" + reqUrl);
        String body = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("signature", signature);
            jsonObject.put("clientReportId", customKey);
            jsonObject.put("clientVersion", TVCConstants.TVCVERSION);
            jsonObject.put("vodSessionKey", vodSessionKey);
            body = jsonObject.toString();
            Log.d(TAG, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(requestBody)
                .build();

        if (TVCDnsCache.useProxy()) {
            final String host = request.url().host();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress address = InetAddress.getByName(host);
                        serverIP = address.getHostAddress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        okHttpClient.newCall(request).enqueue(callback);

        return TVCConstants.NO_ERROR;
    }

    public String getServerIP() {
        return serverIP;
    }

    public long getTcpConnTimeCost() {
        return mTXOkHTTPEventListener.getTCPConnectionTimeCost();
    }

    public long getRecvRespTimeCost() {
        return mTXOkHTTPEventListener.getRecvRspTimeCost();
    }

    public void updateSignature(String signature) {
        this.signature = signature;
    }

    public void postFile(TVCUploadInfo info, String customKey, final ProgressRequestBody.ProgressListener listener, Callback callback){
        File videoFile = new File(info.getFilePath());
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String body = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("signature", signature);
            jsonObject.put("videoName", info.getFileName());
            jsonObject.put("videoType", info.getFileType());
            jsonObject.put("videoSize", info.getFileSize());
            // 判断是否需要上传封面
            if (info.isNeedCover()) {
                jsonObject.put("coverName",info.getCoverName());
                jsonObject.put("coverType",info.getCoverImgType());
                jsonObject.put("coverSize", info.getCoverFileSize());
            }
            jsonObject.put("clientReportId", customKey);
            jsonObject.put("clientVersion", TVCConstants.TVCVERSION);
            body = jsonObject.toString();
            Log.d(TAG, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.addFormDataPart("para", null, RequestBody.create(MediaType.parse("application/json"), body));
        builder.addFormDataPart("video_content",videoFile.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),videoFile));
        if (info.isNeedCover()) {
            builder.addFormDataPart("cover_content", info.getCoverName(), RequestBody.create(MediaType.parse("application/octet-stream"),new File(info.getCoverPath())));
        }

        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(SERVER + "UploadFile").post(new ProgressRequestBody(multipartBody,listener)).build();

        if (TVCDnsCache.useProxy()) {
            final String host = request.url().host();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress address = InetAddress.getByName(host);
                        serverIP = address.getHostAddress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        okHttpClient.newCall(request).enqueue(callback);
    }

    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Log.d(TAG, "Sending request " + request.url() + " on " + chain.connection() + "\n" + request.headers());
            if (!TVCDnsCache.useProxy()) {
                serverIP = chain.connection().route().socketAddress().getAddress().getHostAddress();
            }

            Response response = chain.proceed(request);

            return response;
        }
    }
}
