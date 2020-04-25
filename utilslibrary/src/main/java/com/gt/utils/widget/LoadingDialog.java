package com.gt.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.utils.R;


/**
 * 自定义加载中动画
 * 作者：罗咏哲 on 2017/7/17 10:02.
 * 邮箱：137615198@qq.com
 */

public class LoadingDialog {

    public static Dialog dialog;

    public static Dialog createLoadingDialog(Context context) {
        return createLoadingDialog(context, "请稍等", true);
    }

    /**
     * 显示Dialog
     *
     * @param context 上下文
     * @param msg     显示信息
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancelable) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            LinearLayout layout = (LinearLayout) v
                    .findViewById(R.id.dialog_loading_view);// 加载布局
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
            tipTextView.setText(msg);// 设置加载信息

            Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
            loadingDialog.setCancelable(isCancelable); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            /**
             *将显示Dialog的方法封装在这里面
             */
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.PopWindowAnimStyle);
            loadingDialog.show();

            closeDialog();

            dialog = loadingDialog;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * 关闭dialog
     * <p>
     * http://blog.csdn.net/qq_21376985
     */
    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
