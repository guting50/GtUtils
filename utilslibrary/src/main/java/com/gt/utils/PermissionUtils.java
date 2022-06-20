package com.gt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.Date;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by Administrator on 2017/2/21.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static String permissionsHintHead = "没有此权限，无法开启这个功能，请开启权限：";

    public static int REQUEST_CODE_OPEN_SETTING = 50, REQUEST_CODE_PERMISSION = 100;

    public abstract static class PermissionGrant {
        public abstract void onPermissionGranted(String... requestPermissions);

        public void onRefuseGranted() {

        }

    }

    public interface PermissionGranted {
        void onGranted();
    }

    public interface PermissionDenied {
        void onDenied(String requestPermissions);
    }

    /**
     * 检查或申请权限
     *
     * @param requestPermission request , Manifest.permission.CAMERA
     */
    static void checkOrRequestPermissions(final Activity activity, final String requestPermission, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }

        Log.i(TAG, "requestPermission:" + requestPermission);
        if (TextUtils.isEmpty(requestPermission)) {
            Log.w(TAG, "requestPermission illegal :" + requestPermission);
            return;
        }

        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
        //if (Build.VERSION.SDK_INT < 23) {
        //    return;
        ///

        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (RuntimeException e) {
            Toast.makeText(activity, "please open this permission", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "RuntimeException:" + e.getMessage());
            return;
        }

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");

//          有时候你可能会需要跟用户解释一下权限的用途.
//          注意不是每条权限都需要解释,显而易见的那种可以不解释,太多的解释会降低用户体验.
//          一种方式是,当用户拒绝过这个权限,但是又用到了这个功能, 那么很可能用户不是很明白为什么app需要这个权限, 这时候就可以先向用户解释一下.
//          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                //默认不做解释
                Log.i(TAG, "requestPermission shouldShowRequestPermissionRationale");
//                return;
            }
            Log.d(TAG, "requestCameraPermission else");
            ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, REQUEST_CODE_PERMISSION);

        } else {
            Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            Log.d(TAG, "opened:" + requestPermission);
            permissionGrant.onPermissionGranted(requestPermission);
        }
    }

    private static void showMessageOKCancel(final Activity context, String message, OnClickListener okListener, OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("去开启", okListener)
                .setNegativeButton("取消", cancelListener)
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * @param requestCode Need consistent with requestPermission
     */
    static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults, PermissionGrant permissionGrant) {
        Log.i(TAG, "onRequestPermissionsResult permissions:" + Arrays.toString(permissions)
                + ",grantResults:" + Arrays.toString(grantResults) + ",length:" + grantResults.length);
        if (activity == null) {
            return;
        }
        if (requestCode != REQUEST_CODE_PERMISSION) {
            Log.w(TAG, "requestPermissionsResult illegal:" + Arrays.toString(permissions));
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
            //TODO success, do something, can use onCallback
            permissionGrant.onPermissionGranted(permissions);

        } else {
            //TODO hint user this permission function
            Log.i(TAG, "onRequestPermissionsResult PERMISSION NOT GRANTED");
            //TODO
            openSettingActivity(activity, Arrays.toString(permissions), permissionGrant);
        }
    }

    private static void openSettingActivity(final Activity activity, String message, final PermissionGrant permissionGrant) {
        showMessageOKCancel(activity, permissionsHintHead + message, (dialog, which) -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Log.d(TAG, "getPackageName(): " + activity.getPackageName());
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivityForResult(intent, REQUEST_CODE_OPEN_SETTING);
        }, (dialog, which) -> permissionGrant.onRefuseGranted());
    }

    public static class Builder {
        private final Context mContext;
        private String[] mRequestPermissions;
        private PermissionGranted mPermissionGranted;
        private PermissionDenied mPermissionDenied;

        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        public Builder permission(String... requestPermissions) {
            this.mRequestPermissions = requestPermissions;
            return this;
        }

        public Builder onGranted(PermissionGranted permissionGranted) {
            this.mPermissionGranted = permissionGranted;
            return this;
        }

        public Builder onDenied(PermissionDenied permissionDenied) {
            this.mPermissionDenied = permissionDenied;
            return this;
        }

        public void start() {
            if (mRequestPermissions != null && mRequestPermissions.length > 0)
                request(0);
        }

        private void request(int requestPermissionIndex) {
            long key = new Date().getTime();
            PermissionActivity.permissionGrants.put(key, new PermissionGrant() {
                @Override
                public void onPermissionGranted(String... requestPermissions) {
                    if (requestPermissionIndex < mRequestPermissions.length - 1) {
                        request(requestPermissionIndex + 1);
                    } else {
                        mPermissionGranted.onGranted();
                    }
                }

                @Override
                public void onRefuseGranted() {
                    mPermissionDenied.onDenied(mRequestPermissions[requestPermissionIndex]);
                }
            });
            Intent intent = new Intent(mContext, PermissionActivity.class);
            intent.putExtra("requestPermission", mRequestPermissions[requestPermissionIndex]);
            intent.putExtra("key", key);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            try {//增加一个线程阻塞，确保每次生成的key都不相同
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
