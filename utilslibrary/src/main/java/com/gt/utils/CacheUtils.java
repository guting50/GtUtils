package com.gt.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.gt.utils.ormlite.DBClient;

import java.util.List;

public class CacheUtils {

    public static int put(String key, Object obj) {
        return put(ActivityUtils.getTopActivity(), new CacheBean(key, GsonUtils.toJson(obj)));
    }

    public static int put(Context context, String key, Object obj) {
        return put(context, new CacheBean(key, GsonUtils.toJson(obj)));
    }

    public static int put(Context context, CacheBean cacheBean) {
        delete(context, cacheBean.key);
        return DBClient.insert(context, cacheBean);
    }

    public static <T> T getObj(String key, Class<T> clazz) {
        return getObj(ActivityUtils.getTopActivity(), key, clazz);
    }

    public static <T> T getObj(Context context, String key, Class<T> clazz) {
        String val = getVal(context, key);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return GsonUtils.getGson().fromJson(val, clazz);
    }

    public static String getVal(String key) {
        return getObj(ActivityUtils.getTopActivity(), key).value;
    }

    public static String getVal(Context context, String key) {
        return getObj(context, key).value;
    }

    public static CacheBean getCacheBean(String key) {
        return getObj(ActivityUtils.getTopActivity(), key);
    }

    public static List<CacheBean> getObjs(String key) {
        return DBClient.select(ActivityUtils.getTopActivity(), CacheBean.class, "key", key);
    }

    public static List<CacheBean> getObjs(Context context, String key) {
        return DBClient.select(context, CacheBean.class, "key", key);
    }

    public static CacheBean getObj(Context context, String key) {
        List<CacheBean> list = getObjs(context, key);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return new CacheBean();
    }

    public static int delete(String key) {
        return delete(ActivityUtils.getTopActivity(), key);
    }

    public static int delete(Context context, String key) {
        int count = 0;
        List<CacheBean> list = DBClient.select(context, CacheBean.class, "key", key);
        if (list != null)
            for (CacheBean cacheBean : list) {
                count += DBClient.delete(context, cacheBean);
            }
        return count;
    }

    public static int clear() {
        return DBClient.clear(ActivityUtils.getTopActivity(), CacheBean.class);
    }

    public static int clear(Context context) {
        return DBClient.clear(context, CacheBean.class);
    }
}
