package com.zhengquan.zqlibrary.util;

/**
 * Created by zhengquan on 2017/11/16/016.
 */

public class StringUtils {
    /**
     * 是否为空
     *
     * @param str 字符串
     * @return true 空 false 非空
     */
    public static Boolean isEmpty(String str) {
        if (str == null || str.equals("") || str.length() == 0) {
            return true;
        }
        return false;
    }


}
