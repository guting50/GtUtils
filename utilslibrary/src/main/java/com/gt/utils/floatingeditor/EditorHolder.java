package com.gt.utils.floatingeditor;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import java.io.Serializable;

/**
 * 创建日期：2017/9/14.
 *
 * @author kevin
 */

public class EditorHolder implements Serializable{
    int layoutResId;
    int cancelViewId;
    int submitViewId;
    int editTextId;

    /**
     *
     * @param layoutResId  布局的id
     * @param cancelViewId  取消view的id 可以为0
     * @param submitViewId  完成view的id
     * @param editTextId  输入框view的id
     */
    public EditorHolder(@LayoutRes int layoutResId, @IdRes int cancelViewId,
                        @IdRes int submitViewId, @IdRes int editTextId){
        this.layoutResId = layoutResId;
        this.cancelViewId = cancelViewId;
        this.submitViewId = submitViewId;
        this.editTextId = editTextId;
    }
}
