package com.gt.utils.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gt.utils.R;


/**
 * 自定义加载中动画
 * 作者：罗咏哲 on 2017/7/17 10:02.
 * 邮箱：137615198@qq.com
 */

public class LoadingDialog {

    public static Dialog dialog;

    public static Dialog create(Context context) {
        return create(context, "请稍等", true, false);
    }

    public static Dialog create(Context context, String msg, boolean isCancelable) {
        return create(context, msg, isCancelable, false);
    }

    /**
     * 创建 LoadingDialog
     *
     * @param context      上下文
     * @param msg          内容
     * @param isCancelable 是否可取消
     * @param isAlone      是否单独使用
     * @return 返回Dialog
     */
    public static Dialog create(Context context, String msg, boolean isCancelable, boolean isAlone) {
        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            FrameLayout layout = v.findViewById(R.id.dialog_loading_view);// 加载布局
            TextView tipTextView = v.findViewById(R.id.tipTextView);// 提示文字
            tipTextView.setText(msg);// 设置加载信息

            loadingDialog.setCancelable(isCancelable); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.setContentView(layout, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));// 设置布局
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
            //去掉动画，避免切换页面时闪烁
//            window.setWindowAnimations(R.style.PopWindowAnimStyle);

            if (!isAlone) {
                closeDialog();
                dialog = loadingDialog;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingDialog;
    }

    /**
     * 关闭dialog
     */
    public static void closeDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
