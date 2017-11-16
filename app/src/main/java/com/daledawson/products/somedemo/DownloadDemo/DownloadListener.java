package com.daledawson.products.somedemo.DownloadDemo;

/**
 * Created by Administrator on 2017/11/14/014.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
