package com.zhengquan.zqlibrary.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 创 建 人：zheng Quan
 * 创建日期：2019/7/2
 * 修改时间：
 * 修改备注：
 */
public class OkHttpUtil {
    private static final byte[] LOCKER = new byte[0];
    private static int connTimeOut = 5;
    private static int readTimeOut = 20;
    private static int writeTimeOut = 10;
    private static OkHttpUtil mInstance;
    private OkHttpClient client;

    private OkHttpUtil() {
        okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
        ClientBuilder.readTimeout(readTimeOut, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(connTimeOut, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);//写入超时
        //支持HTTPS请求，跳过证书验证
        ClientBuilder.sslSocketFactory(createSSLSocketFactory());
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        client = ClientBuilder.build();
    }

    /**
     * 单例模式获取NetUtils
     *
     * @return
     */
    public static OkHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步get 异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param host     域名
     * @param path     请求地址
     * @param headers  请求头
     * @param querys   参数
     * @param callBack 回调
     * @throws Exception 抛出异常
     */
    public void get(String host, String path, Map<String, String> headers, Map<String, String> querys, final ResultCallBack callBack) throws Exception {
        StringBuffer url = new StringBuffer(host + (path == null ? "" : path));
        if (querys != null) {
            url.append("?");
            Iterator iterator = querys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry) iterator.next();
                url.append((String) e.getKey()).append("=").append((String) e.getValue() + "&");
            }
            url = new StringBuffer(url.substring(0, url.length() - 1));
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = (requestBuilder).url(url.toString()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }

    /**
     * 异步post 表单 异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url      请求地址
     * @param headers  请求头
     * @param querys   请求参数
     * @param callBack 回调
     * @throws Exception 抛出异常
     */
    public void postForm(String url, Map<String, String> headers, Map<String, String> querys, final ResultCallBack callBack) throws Exception {
        FormBody.Builder formbody = new FormBody.Builder();
        if (null != querys) {
            Iterator iterator = querys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry) iterator.next();
                formbody.add((String) elem.getKey(), (String) elem.getValue());
            }
        }

        RequestBody body = formbody.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(url);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = requestBuilder.post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }

    /**
     * 异步post json  异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param sendMessage 请求json
     * @return
     * @throws Exception 抛出异常
     */
    public void postJson(String url, Map<String, String> headers, String sendMessage, final ResultCallBack callBack) throws Exception {

        RequestBody body = FormBody.create(MediaType.parse("application/json"), sendMessage);
        Request.Builder requestBuilder = (new Request.Builder()).url(url);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = requestBuilder.post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }

    public void put(String host, String path, Map<String, String> headers, Map<String, String> querys, final ResultCallBack callBack) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator iterator = querys.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> elem = (Map.Entry) iterator.next();
            builder.add((String) elem.getKey(), (String) elem.getValue());
        }

        RequestBody body = builder.build();
        Request.Builder requestBuilder = (new Request.Builder()).url(host + path);
        if (headers != null && headers.size() > 0) {
            Iterator iteratorHeader = headers.keySet().iterator();
            while (iteratorHeader.hasNext()) {
                String key = (String) iteratorHeader.next();
                requestBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = requestBuilder.put(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(call, response);
            }
        });
    }

    public interface ResultCallBack {
        void onSuccess(Call call, Response response);

        void onFailed(Call call, IOException e);
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     */
    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
