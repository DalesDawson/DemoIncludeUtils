package com.zhengquan.zqlibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zhengquan on 2017/11/16/016.
 */

public class NetworkUtils {
    /**
     * 检查网络是否连通
     *
     * @return boolean
     * @since V1.0
     */
    public boolean isNetworkAvailable(Context context) {
        // 创建并初始化连接对象
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 判断初始化是否成功并作出相应处理
        if (connMan != null) {
            // 调用getActiveNetworkInfo方法创建对象,如果不为空则表明网络连通，否则没连通
            NetworkInfo info = connMan.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }
}
