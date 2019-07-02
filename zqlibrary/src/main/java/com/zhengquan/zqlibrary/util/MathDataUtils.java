package com.zhengquan.zqlibrary.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/16/016.
 */

public class MathDataUtils {
    /**
     * 去除double类型数字尾巴的0
     *
     * @param d
     * @return
     */
    public static BigDecimal stripTrailingZeros(double d) {//去除double尾巴的0
        return new BigDecimal(d).stripTrailingZeros();
    }
}
