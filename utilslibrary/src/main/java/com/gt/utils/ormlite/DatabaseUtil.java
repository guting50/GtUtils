package com.gt.utils.ormlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.util.Arrays;

public class DatabaseUtil {

    private static final String TAG = "DatabaseUtil";

    /**
     * 升级表，增加\删除表字段
     *
     * @param db
     * @param clazz
     */
    public static <T> void upgradeTable(SQLiteDatabase db, ConnectionSource cs, Class<T> clazz) {
        String tableName = extractTableName(clazz);

        db.beginTransaction();
        try {

            //Rename table
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            db.execSQL(sql);
            Log.i(TAG, sql);

            //Create table
            try {
                sql = TableUtils.getCreateTableStatements(cs, clazz).get(0);
                db.execSQL(sql);
                Log.i(TAG, sql);
            } catch (Exception e) {
                e.printStackTrace();
                TableUtils.createTable(cs, clazz);
            }

            //Load data
            if (!loadData(db, tableName, tempTableName, tableName)) {
                loadData(db, tableName, tempTableName, tempTableName);
            }

            //Drop temp table
            sql = "DROP TABLE IF EXISTS " + tempTableName;
            db.execSQL(sql);
            Log.i(TAG, sql);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private static boolean loadData(SQLiteDatabase db, String tableName, String tempTableName, String table_name) {
        try {
            String columns = Arrays.toString(getColumnNames(db, table_name)).replace("[", "").replace("]", "");
            String sql = "INSERT INTO " + tableName + " (" + columns + ") " +
                    " SELECT " + columns + " FROM " + tempTableName;
            db.execSQL(sql);
            Log.i(TAG, sql);
            return true;
        } catch (SQLException e) {
            Log.i(TAG, e.getMessage());
        }
        return false;
    }


    /**
     * 获取表名(ormlite DatabaseTableConfig.java)
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name = null;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        } else {
            /*
             * NOTE: to remove javax.persistence usage, comment the following line out
             */
//            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                // if the name isn't specified, it is the class name lowercased
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }

    /**
     * 获取表的列名
     *
     * @param db
     * @param tableName
     * @return
     */
    private static String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("name");
                if (columnIndex == -1) {
                    return null;
                }

                int index = 0;
                columnNames = new String[cursor.getCount()];
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }

}
