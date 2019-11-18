package com.gt.utils.floatingeditor;

import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * Created by like on 2017/9/18.
 */

public class InputCheckRule implements Serializable {
    int minLength;
    int maxLength;
    String regxRule;
    int regxWarn;

    public InputCheckRule(int maxLength, int minLength) {
        this(maxLength,minLength, null, 0);
    }

    /**
     *
     * @param maxLength  最少输入字符数
     * @param minLength  最多输入字符数
     * @param regxRule  正则表达式校验
     * @param regxWarn  正则表达式失败提示
     */
    public InputCheckRule(int maxLength, int minLength, String regxRule, @StringRes int regxWarn) {
        if (maxLength < minLength || minLength < 0)
            throw new IllegalStateException("maxLength < minLength or minLength < 0");
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.regxRule = regxRule;
        this.regxWarn = regxWarn;
    }
}
