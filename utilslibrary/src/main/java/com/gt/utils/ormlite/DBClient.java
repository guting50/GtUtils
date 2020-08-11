package com.gt.utils.ormlite;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * eq(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id = 2，columnName为列名，value为匹配键
 * <p>
 * lt(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id < 2
 * <p>
 * gt(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id > 2
 * <p>
 * le(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id <=2
 * <p>
 * ge(String columnName, Object value) //此方法相当与SQL语句中的 WHERE id >=2
 * <p>
 * ne(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id <> 2
 * <p>
 * in(String columnName, Object... objects) // 此方法相当于SQL语句中的 WHERE id IN（1，2）
 * <p>
 * notIn(String columnName, Object... objects) //此方法相当于SQL语句中的 WHERE id NOT IN（1，2）
 * <p>
 * like(String columnName, Object value) //此方法相当于SQL语句中的 WHERE id LIKE “2%”
 * <p>
 * between(String columnName, Object low, Object high) //此方法相当于SQL语句中的 WHERE id BETWEEN 1 AND 2
 * <p>
 * and()、or() //用来组合以上这些方法
 */
public class DBClient {

    /**
     * 增
     *
     * @param context
     * @param obj
     * @return
     */
    public static int insert(Context context, Object obj) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            return helper.getDao(obj.getClass()).create(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删
     *
     * @param context
     * @param obj
     * @return
     */
    public static int delete(Context context, Object obj) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            return helper.getDao(obj.getClass()).delete(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 改
     *
     * @param context
     * @param obj
     * @return
     */
    public static int update(Context context, Object obj) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            return helper.getDao(obj.getClass()).update(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 清空表
     *
     * @param context
     * @param clazz
     */
    public static int clear(Context context, Class clazz) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            return helper.getDao(clazz).deleteBuilder().delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> List<T> select(Context context, Class<T> clazz) {
        return select(context, clazz, "", false, null);
    }

    public static <T> List<T> select(Context context, Class<T> clazz, String key, Object value) {
        Map<String, Object> eqMap = new HashMap<>();
        eqMap.put(key, value);
        return select(context, clazz, "", false, eqMap);
    }

    public static <T> List<T> select(Context context, Class<T> clazz, Map<String, Object> eqMap) {
        return select(context, clazz, "", false, eqMap);
    }

    /**
     * 查询
     *
     * @param context
     * @param clazz
     * @param orderColumnName
     * @param ascending
     * @param eqMap
     * @param <T>
     * @return
     */
    public static <T> List<T> select(Context context, Class<T> clazz,
                                     String orderColumnName, boolean ascending,
                                     Map<String, Object> eqMap) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            QueryBuilder queryBuilder = helper.getDao(clazz).queryBuilder();
            if (!TextUtils.isEmpty(orderColumnName)) {
                queryBuilder = queryBuilder.orderBy(orderColumnName, ascending);
            }
            if (eqMap != null && eqMap.size() > 0) {
                Where where = queryBuilder.where();
                for (String key : eqMap.keySet()) {
                    try {
                        where.and();
                    } catch (Exception e) {

                    }
                    where.eq(key, eqMap.get(key));
                }
            }
            System.out.println(queryBuilder.prepareStatementString());
            return queryBuilder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> selectOr(Context context, Class<T> clazz, Map<String, Object> eqMap) {
        return selectOr(context, clazz, "", false, eqMap);
    }

    public static <T> List<T> selectOr(Context context, Class<T> clazz,
                                       String orderColumnName, boolean ascending,
                                       Map<String, Object> eqMap) {
        DbHelper helper = DbHelper.getHelper(context);
        try {
            QueryBuilder queryBuilder = helper.getDao(clazz).queryBuilder();
            if (!TextUtils.isEmpty(orderColumnName)) {
                queryBuilder = queryBuilder.orderBy(orderColumnName, ascending);
            }
            if (eqMap != null && eqMap.size() > 0) {
                Where where = queryBuilder.where();
                for (String key : eqMap.keySet()) {
                    try {
                        where.or();
                    } catch (Exception e) {

                    }
                    where.eq(key, eqMap.get(key));
                }
            }
            System.out.println(queryBuilder.prepareStatementString());
            return queryBuilder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

