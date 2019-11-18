package com.gt.utils.floatingeditor;

import android.view.ViewGroup;

/**
 * 创建日期：2017/9/13.
 *
 * @author kevin
 */

public interface EditorCallback {
    /**
     * 取消回调
     */
    void onCancel();

    /**
     * 输入完成后回调
     * @param content 内容
     */
    void onSubmit(String content);

    /**
     * 加载完页面后回调
     * @param rootView 页面view
     */
    void onAttached(ViewGroup rootView);
}
