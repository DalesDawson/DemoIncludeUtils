package com.daledawson.products.somedemo.AsyncHttpClient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daledawson.products.somedemo.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class AsyncHttpclientActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_get, btn_post, btn_download, btn_upload;
    TextView tv_content;
    String url = "https://api.github.com/users/DalesDawson/repos";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchttpclient);
        btn_get = (Button) findViewById(R.id.get);
        btn_post = (Button) findViewById(R.id.post);
        btn_download = (Button) findViewById(R.id.download);
        btn_upload = (Button) findViewById(R.id.upload);

        tv_content = (TextView) findViewById(R.id.content);
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_download.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("msg", statusCode + responseBody.toString());
                        tv_content.setText(responseBody.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("msg", statusCode + "失败");
                    }
                });
                break;
            case R.id.post:

                break;
            case R.id.download:

                break;
            case R.id.upload:

                break;
        }
    }
}
