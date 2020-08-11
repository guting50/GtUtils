package com.gt.utils.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.gt.utils.ClassesReader;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DbHelper";
    /**
     * data base name
     */
    private static String DATABASE_NAME = "gt_data.db";
    /**
     * increase the database version
     */
    private static int DATABASE_VERSION;

    private static DbHelper helper = null;

    // 存储APP中所有的DAO对象的Map集合
    private Map<String, Dao> daos = new HashMap<>();

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG + "--oldVersion-->", getReadableDatabase().getVersion() + "");
        Log.e(TAG + "--newVersion-->", DATABASE_VERSION + "");
    }

    public static synchronized DbHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DbHelper(context);
        }
        return helper;
    }

    public static void updateDB(int databaseVersion) {
        updateDB(DATABASE_NAME, databaseVersion);
    }

    public static void updateDB(String databaseName, int databaseVersion) {
        DATABASE_NAME = databaseName;
        DATABASE_VERSION = databaseVersion;
    }

    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            for (Class obj : getTables()) {
                TableUtils.createTable(connectionSource, obj);
                Log.i(TAG, TableUtils.getCreateTableStatements(connectionSource, obj).get(0).toString());
            }
            long millis = System.currentTimeMillis();
            Log.i(TAG, "created new entries in onCreate: " + millis);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade");
            for (Class obj : getTables()) {
                DatabaseUtil.upgradeTable(db, connectionSource, obj);
            }
            // after we drop the old databases, we create the new ones
            Log.e(TAG, "-------DbHelper  onUpgrade-------");
        } catch (Exception e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public List<Class> getTables() {
        List<Class> tables = new ArrayList<>();
        List<Class<?>> reader = ClassesReader.reader(AppUtils.getAppPath());
        reader = ClassesReader.deleteNotAnnotationClass(reader, DatabaseTable.class);
        for (Class<?> c : reader) {
            if (!tables.contains(c)) {
                tables.add(c);
            }
        }
        return tables;
    }

    // 根据传入的DAO的路径获取到这个DAO的单例对象（要么从daos这个Map中获取，要么新创建一个并存入daos）
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
        helper = null;
    }

}

