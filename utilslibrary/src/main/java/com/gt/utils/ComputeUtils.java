package com.gt.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputeUtils {

    /**
     * 提供精确的加法运算。
     *
     * @param v1
     * @param v2
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2));
    }

    public static double add(double... arr) {
        double result = 0;
        for (double v : arr) {
            result = add(result, v);
        }
        return result;
    }

    public static double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    public static double add(String... arr) {
        String result = "0";
        for (String v : arr) {
            result = add(result, v) + "";
        }
        return Double.parseDouble(result);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1
     * @param v2
     * @return 两个参数的数学积，以字符串格式返回
     */
    public static double multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(replaceBlank(v1));
        BigDecimal b2 = new BigDecimal(replaceBlank(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(String v1, String v2, int scale) {
        return div(Double.parseDouble(v1), Double.parseDouble(v2), scale, BigDecimal.ROUND_HALF_UP);
    }

    public static double div(double v1, double v2, int scale) {
        return div(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static double div(double v1, double v2, int scale, int roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (v2 == 0) {
            return 0;
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, roundingMode).doubleValue();
        }
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */

    public static double del(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * 去除空格
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
