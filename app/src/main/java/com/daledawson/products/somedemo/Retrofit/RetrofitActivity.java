package com.daledawson.products.somedemo.Retrofit;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daledawson.products.somedemo.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/6/006.
 */

public class RetrofitActivity extends AppCompatActivity {
    Button get, post, rxjava_post;
    TextView response_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_activity);
        get = (Button) findViewById(R.id.get);
        post = (Button) findViewById(R.id.post);
        rxjava_post = (Button) findViewById(R.id.rxjava_post);
        response_tv = (TextView) findViewById(R.id.response);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        rxjava_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxJavaPost();
            }
        });
    }

    /**
     * Retrofit实现简单的get请求
     */
    public void get() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetService service = retrofit.create(GetService.class);
        Call<ResponseBody> repos = service.listRepos("DalesDawson");
        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("TAG", response.body().source().toString());
                response_tv.setText(response.body().source().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Retrofit实现简单的post请求
     */
    public void post() {
        /**
         * http://www.kuaidi100.com/query?type=快递公司代号&postid=快递单号
         ps:快递公司编码:申通="shentong" EMS="ems" 顺丰="shunfeng" 圆通="yuantong"
         中通="zhongtong" 韵达="yunda" 天天="tiantian" 汇通="huitongkuaidi"
         全峰="quanfengkuaidi" 德邦="debangwuliu" 宅急送="zhaijisong"
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.kuaidi100.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostService apiService = retrofit.create(PostService.class);
        Call<PostQueryInfo> call = apiService.search("zhongtong", "465568267610");
        call.enqueue(new Callback<PostQueryInfo>() {
            @Override
            public void onResponse(Call<PostQueryInfo> call, Response<PostQueryInfo> response) {
                Log.d("TAG", response.body().getData().toString());
                response_tv.setText(response.body().getMessage() + response.body().getNu() + response.body().getIscheck() + response.body().getCom() + response.body().getStatus() + response.body().getCondition() + response.body().getState() + response.body().getData().toString());
            }

            @Override
            public void onFailure(Call<PostQueryInfo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void RxJavaPost() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.kuaidi100.com/")
                //添加数据解析ConverterFactory
                .addConverterFactory(GsonConverterFactory.create())
                //添加RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        PostService apiService = retrofit.create(PostService.class);
        apiService.searchRx("yuantong", "500379523313")
                //访问网络切换异步线程
                .subscribeOn(Schedulers.io())
                //响应结果处理切换成主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostQueryInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        //错误回调
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostQueryInfo postQueryInfo) {
                        //成功结果返回
                        Log.e("APP", postQueryInfo.getData().toString());
                        response_tv.setText(postQueryInfo.getData().toString());
                    }
                });
    }

    /**
     * Retrofit实现单文件上传携带参数(使用注解@Multipart和@Part)
     */
    public void UploadSingleFile() {
        Retrofit retrofitUpload = new Retrofit.Builder()
                .baseUrl("http://192.168.1.8:8080/UploadFile/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService service = retrofitUpload.create(FileUploadService.class);
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", "xuezhiqian.png");
        //设置Content-Type:application/octet-stream
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        //设置Content-Disposition:form-data; name="photo"; filename="xuezhiqian.png"
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoRequestBody);
        //添加参数用户名和密码，并且是文本类型
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), "abc");
        RequestBody passWord = RequestBody.create(MediaType.parse("text/plain"), "123");
        Call<ResponseBody> loadCall = service.uploadfile(photo, userName, passWord);
        loadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("APP", response.body().source().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Retrofit实现多文件上传携带参数(使用注解@PartMap和@Part)
     */
    public void UploadMutiFile() {
        Retrofit retrofitUpload = new Retrofit.Builder()
                .baseUrl("http://192.168.1.8:8080/UploadFile/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService service = retrofitUpload.create(FileUploadService.class);
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", "xuezhiqian.png");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/Pictures", "xuezhiqian2.png");
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody photoRequestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), "abc");
        RequestBody passWord = RequestBody.create(MediaType.parse("text/plain"), "123");
        Map<String, RequestBody> photos = new HashMap<>();
        //添加文件一
        photos.put("photos\"; filename=\"" + file.getName(), photoRequestBody);
        //添加文件二
        photos.put("photos\"; filename=\"" + file2.getName(), photoRequestBody2);
        //添加用户名参数
        photos.put("username", userName);
        Call<ResponseBody> loadCall = service.uploadfile(photos, passWord);
        loadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("APP", response.body().source().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
