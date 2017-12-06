package com.daledawson.products.somedemo.Retrofit;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/12/6/006.
 */

/**
 * @GET和@POST分别是get和post请求。括号里面的value值与上面.baseUrl组成完整的路径
 * @Path动态的URL访问。像上面get请求中的{user}可以把它当做一个占位符，通过@Path("user")标注的参数进行替换
 * @Query请求参数。无论是GET或POST的参数都可以用它来实现
 * @QueryMap请求参数使用Map集合。可以传递一个map集合对象
 * @Body实体请求参数。顾名思义可以传递一个实体对象来作为请求的参数，不过实体属性要与参数名一一致
 * @FormUrlEncoded和@Field简单的表单键值对。两个需要结合使用
 * @Multipart和@PartPOST表单的方式上传文件可以携带参数。两个需要结合使用，使用方式查看下面文件上传中介绍。
 * @PartMap和@PartPOST表单上传多个文件携带参数。
 */
public interface PostService {
    @POST("query")
    Call<PostQueryInfo> search(
            @Query("type") String type,
            @Query("postid") String postid);

    @POST("query")
    Observable<PostQueryInfo> searchRx(@Query("type") String type, @Query("postid") String postid);
}
