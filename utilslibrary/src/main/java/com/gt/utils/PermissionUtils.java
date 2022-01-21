package com.gt.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

/**
 * Created by Administrator on 2017/2/21.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int RECORD_AUDIO = 0;
    public static final int GET_ACCOUNTS = 1;
    public static final int READ_PHONE_STATE = 2;
    public static final int CALL_PHONE = 3;
    public static final int CAMERA = 4;
    public static final int ACCESS_FINE_LOCATION = 5;
    public static final int ACCESS_COARSE_LOCATION = 6;
    public static final int READ_EXTERNAL_STORAGE = 7;
    public static final int WRITE_EXTERNAL_STORAGE = 8;
    public static final int READ_CONTACTS = 9;
    public static final int READ_SMS = 10;
    public static final int REQUEST_INSTALL_PACKAGES = 11;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    public static final String PERMISSION_REQUEST_INSTALL_PACKAGES = Manifest.permission.REQUEST_INSTALL_PACKAGES;
    private static final String[] mRequestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            PERMISSION_READ_CONTACTS,
            PERMISSION_READ_SMS,
            PERMISSION_REQUEST_INSTALL_PACKAGES
    };
    public static String permissionsHintHead = "没有此权限，无法开启这个功能，请开启权限：";
    private static final String[] permissionsHint = {
            permissionsHintHead + "麦克风",
            permissionsHintHead + "访问GMail账户",
            permissionsHintHead + "读取电话状态",
            permissionsHintHead + "电话呼叫",
            permissionsHintHead + "摄像头",
            permissionsHintHead + "定位",
            permissionsHintHead + "定位",
            permissionsHintHead + "存储",
            permissionsHintHead + "存储",
            permissionsHintHead + "读写联系人",
            permissionsHintHead + "读写短信",
            permissionsHintHead + "安装未知来源应用"
    };

    public static int REQUEST_CODE = 50;

    public abstract static class PermissionGrant {
        public abstract void onPermissionGranted(int... requestCode);

        public void onRefuseGranted() {

        }

    }

    public interface PermissionGranted {
        void onGranted(int... requestCode);

    }

    public interface PermissionDenied {
        void onDenied(int... requestCode);
    }

    /**
     * 申请权限
     *
     * @param context
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    public static synchronized void requestPermission(Context context, int requestCode, PermissionGrant permissionGrant) {
        long key = new Date().getTime();
        PermissionActivity.permissionGrants.put(key, permissionGrant);
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("key", key);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        try {//增加一个线程阻塞，确保每次生成的key都不相同
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查或申请权限
     *
     * @param activity
     * @param requestCode request code, e.g. if you need request CAMERA permission,parameters is PermissionUtils.CODE_CAMERA
     */
    static void checkOrRequestPermissions(final Activity activity, final int requestCode, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }

        Log.i(TAG, "requestPermission requestCode:" + requestCode);
        if (requestCode < 0 || requestCode >= mRequestPermissions.length) {
            Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            return;
        }

        final String requestPermission = mRequestPermissions[requestCode];

        //如果是6.0以下的手机，ActivityCompat.checkSelfPermission()会始终等于PERMISSION_GRANTED，
        // 但是，如果用户关闭了你申请的权限，ActivityCompat.checkSelfPermission(),会导致程序崩溃(java.lang.RuntimeException: Unknown exception code: 1 msg null)，
        // 你可以使用try{}catch(){},处理异常，也可以在这个地方，低于23就什么都不做，
        // 个人建议try{}catch(){}单独处理，提示用户开启权限。
        /*if (Build.VERSION.SDK_INT < 23) {
            return;
        }*/

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
            ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);

        } else {
            Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
            Log.d(TAG, "opened:" + mRequestPermissions[requestCode]);
            permissionGrant.onPermissionGranted(requestCode);
        }
    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("去开启", okListener)
                .setNegativeButton("取消", cancelListener)
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }
        Log.d(TAG, "requestPermissionsResult requestCode:" + requestCode);

        if (requestCode < 0 || requestCode >= mRequestPermissions.length) {
            Log.w(TAG, "requestPermissionsResult illegal requestCode:" + requestCode);
            Toast.makeText(activity, "illegal requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "onRequestPermissionsResult requestCode:" + requestCode + ",permissions:" + permissions.toString()
                + ",grantResults:" + grantResults.toString() + ",length:" + grantResults.length);

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult PERMISSION_GRANTED");
            //TODO success, do something, can use onCallback
            permissionGrant.onPermissionGranted(requestCode);

        } else {
            //TODO hint user this permission function
            Log.i(TAG, "onRequestPermissionsResult PERMISSION NOT GRANTED");
            //TODO
            openSettingActivity(activity, permissionsHint[requestCode], permissionGrant);
        }
    }

    private static void openSettingActivity(final Activity activity, String message, final PermissionGrant permissionGrant) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Log.d(TAG, "getPackageName(): " + activity.getPackageName());
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionGrant.onRefuseGranted();
            }
        });
    }

    public static class Builder {
        private final Context mContext;
        private int mRequestCode;
        private PermissionGranted mPermissionGranted;
        private PermissionDenied mPermissionDenied;

        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        public Builder permission(int requestCode) {
            this.mRequestCode = requestCode;
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
            long key = new Date().getTime();
            PermissionActivity.permissionGrants.put(key, new PermissionGrant() {
                @Override
                public void onPermissionGranted(int... requestCode) {
                    mPermissionGranted.onGranted(requestCode);
                }

                @Override
                public void onRefuseGranted() {
                    mPermissionDenied.onDenied(mRequestCode);
                }
            });
            Intent intent = new Intent(mContext, PermissionActivity.class);
            intent.putExtra("requestCode", mRequestCode);
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
