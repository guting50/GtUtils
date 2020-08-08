package com.gt.utils;

import android.content.Context;

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
        return DBClient.insert(context, cacheBean);
    }

    public static <T> T getObj(String key, Class<T> clazz) {
        return getObj(ActivityUtils.getTopActivity(), key, clazz);
    }

    public static <T> T getObj(Context context, String key, Class<T> clazz) {
        return GsonUtils.getGson().fromJson(getVal(context, key), clazz);
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

    public static List<CacheBean> getObjs(Context context, String key) {
        return DBClient.select(context, CacheBean.class, "key", key);
    }

    public static CacheBean getObj(Context context, String key) {
        List<CacheBean> list = getObjs(context, key);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
