package com.daledawson.products.somedemo.RxJava_Network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daledawson.products.somedemo.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Administrator on 2017/12/5/005.
 */

public class RxJavaNetwork extends AppCompatActivity {
    Button okhttp, retrofit;
    TextView tv;
    private final String KEY = "89bba53faccef25b774f2307e398e842";
    private final String url = "https://way.jd.com/he/freeweather?";
    OkHttpClient okHttpClient;
    private static final String TAG = "NetWork";

    private Subscription subscription;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava_network);
        okhttp = (Button) findViewById(R.id.Okhttp);
        retrofit = (Button) findViewById(R.id.Retrofit);
        tv = (TextView) findViewById(R.id.content);
        okhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAsynHttp("chengdu");

            }
        });
        retrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postIpInformation("59.108.54.37");
            }
        });
    }

    private void postIpInformation(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        IpServiceForPost ipService = retrofit.create(IpServiceForPost.class);
//        subscription = ipService.getIpMsg(ip).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HttpResult<IpData>>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(HttpResult<IpData> value) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

    private void postAsynHttp(String size) {
        getObservable(size).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
                tv.setText(s);
                Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Observable<String> getObservable(final String city) {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter exxx) throws Exception {
                okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("city", city)
                        .add("appkey", KEY)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        exxx.onError(new Exception("error"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        exxx.onNext(str);
                        exxx.onComplete();
                    }

                });
            }

        });
        return observable;
    }

}
