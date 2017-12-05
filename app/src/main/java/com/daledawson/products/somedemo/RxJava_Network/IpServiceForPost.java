package com.daledawson.products.somedemo.RxJava_Network;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public interface IpServiceForPost {
    @FormUrlEncoded
    @POST("getIpInfo.php")
    Observable<IpModel> getIpMsg(@Field("ip") String first);
}
