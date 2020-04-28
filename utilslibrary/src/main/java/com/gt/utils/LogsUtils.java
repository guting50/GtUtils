package com.gt.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存日志
 * Created by Administrator on 2017/6/12.
 */

public class LogsUtils {

    private static final String KMS_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "gt" + File.separator + "log";

    public static void writeInfoLog(Context ct, String error) {
        writeLog(ct, "", "info", error);
    }

    public static void writeInfoLog(Context ct, String id, String error) {
        writeLog(ct, id, "info", error);
    }

    public static void writeEooroLog(Context ct, String error) {
        writeLog(ct, "", "error", error);
        writeLog(ct, "", "info", error);
    }

    public static void writeEooroLog(Context ct, String id, String error) {
        writeLog(ct, id, "error", error);
        writeLog(ct, id, "info", error);
    }

    private static void writeLog(Context ct, String id, String pathName, String context) {
        String versionName = "";
        int versioncode = 0;
        try {
            PackageManager pm = ct.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ct.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String version = versioncode + "；" + versionName;

        Map<String, String> param = new HashMap<>();
        param.put("device", "手机型号："
                + android.os.Build.MODEL + "；手机厂商：" + android.os.Build.MANUFACTURER + "；系统版本：" + android.os.Build.VERSION.SDK + "："
                + android.os.Build.VERSION.RELEASE);
        param.put("version", version);
        param.put("id", id);
        writeFileToSD(ct, pathName, new Gson().toJson(param) + "[error:" + context + "]");
    }

    /**
     * 写文件到sd卡上
     *
     * @param context
     */
    public static void writeFileToSD(Context ct, final String pathName, final String context) {
        PermissionUtils.requestPermission(ct, PermissionUtils.WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int... requestCode) {
                if (requestCode[0] == -1) {
                    return;
                }
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    Log.e("TestFile", "SD card is not avaiable/writeable icon_xingge_right now.");
                    return;
                }
                writeFile(pathName, context);
            }
        });
    }

    private static void writeFile(String pathName, final String context) {
        try {
            File path = new File(KMS_DIR);
            String str = "gt_" + pathName + "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".log";
            File file = new File(path.getPath() + File.separator + str);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] --->>> ").getBytes());
            fos.write(context.getBytes());
            fos.write("\n\n".getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TestFile", "Error on writeFilToSD.");
        }
    }

    private static void writeLog(String id, String context) {
        String versionName = "";
        int versioncode = 0;
        final String version = versioncode + "；" + versionName;

        Map<String, String> param = new HashMap<>();
        param.put("device", "手机型号："
                + android.os.Build.MODEL + "；手机厂商：" + android.os.Build.MANUFACTURER + "；系统版本：" + android.os.Build.VERSION.SDK + "："
                + android.os.Build.VERSION.RELEASE);
        param.put("version", version);
        param.put("id", id);
        writeFile("error", new Gson().toJson(param) + "[error:" + context + "]");
    }

    public static void main(String[] a) {
        writeLog("gt", "AaAaasfdjasdjf ksdargf oadsjhfg asdjf asjdf afaAaAaasfdjasdjf ksdargf oadsjhfg asdjf asjdf " +
                "afaAaAaasfdjasdjf ksdargf oadsjhfg asdjf asjdf afaAaAaasfdjasdjf ksdargf oadsjhfg asdjf asjdf afaAaAaasfdjasdjf ksdargf oadsjhfg asdjf asjdf afa");
    }
}
