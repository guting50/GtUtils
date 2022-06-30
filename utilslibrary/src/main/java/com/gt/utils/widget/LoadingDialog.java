package com.gt.utils.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gt.utils.R;


/**
 * 自定义加载中动画
 */

public class LoadingDialog {

    public static Dialog mDialog;
    public Activity mContext;
    public Dialog dialog;
    public TextView tipTextView;

    public static LoadingDialog create(Activity context) {
        return create(context, "请稍等", true, false);
    }

    public static LoadingDialog create(Activity context, String msg, boolean isCancelable) {
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
    public static LoadingDialog create(Activity context, String msg, boolean isCancelable, boolean isAlone) {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.mContext = context;
        loadingDialog.dialog = new Dialog(context, R.style.MyDialogStyle);
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
            LinearLayout layout = v.findViewById(R.id.dialog_loading_view);// 加载布局
            loadingDialog.tipTextView = v.findViewById(R.id.tipTextView);// 提示文字
            loadingDialog.tipTextView.setText(msg);// 设置加载信息

            loadingDialog.dialog.setCancelable(isCancelable); // 是否可以按“返回键”消失
            loadingDialog.dialog.setCanceledOnTouchOutside(isCancelable); // 点击加载框以外的区域
            loadingDialog.dialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
            Window window = loadingDialog.dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
            //去掉动画，避免切换页面时闪烁
//            window.setWindowAnimations(R.style.PopWindowAnimStyle);

            if (!isAlone) {
                closeDialog();
                mDialog = loadingDialog.dialog;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingDialog;
    }

    public void show() {
        if (dialog != null && !mContext.isDestroyed())
            dialog.show();
    }

    public void show(String msg) {
        if (dialog != null && !mContext.isDestroyed()) {
            tipTextView.setText(msg);
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * 关闭dialog
     */
    public static void closeDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        Activity context;
        String msg = "请稍后";//          内容
        boolean isCancelable = true;// 是否可取消
        boolean isAlone = false;
        LoadingDialog loadingDialog;
        int count;

        public Builder(Activity context) {
            this.context = context;
        }

        public Builder setIsCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public Builder setIsAlone(boolean isAlone) {
            this.isAlone = isAlone;
            return this;
        }

        public LoadingDialog create() {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog.create(context, msg, isCancelable, isAlone);
            }
            return loadingDialog;
        }

        public void show() {
            show(msg);
        }

        public void show(String msg) {
            count++;
            create().show(msg);
        }

        public void dismiss() {
            if (--count <= 0) {
                loadingDialog.dismiss();
                count = 0;
            }
        }

        public void dismissAll() {
            loadingDialog.dismiss();
            count = 0;
        }
    }
}
