package com.android.baselib.db.operate;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.android.baselib.log.ULog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 数据库表操作类
 *
 * @author PF-NAN
 * @date 2018/2/26.
 */
public class UTableOperate {
    private static UTableOperate mInstance;

    private UTableOperate() {
    }

    /**
     * 单例模式获取实例
     */
    public static UTableOperate getInstance() {
        if (null == mInstance) {
            synchronized (UTableOperate.class) {
                if (null == mInstance) {
                    mInstance = new UTableOperate();
                }
            }
        }
        return mInstance;
    }

    /**
     * 表中添加列
     * ALTER TABLE 表名 ADD COLUMN 列名 数据类型
     *
     * @param db           SQLiteDatabase
     * @param tableName    表名
     * @param columnName   列名
     * @param columnType   列类型
     * @param defaultField 列默认值
     */
    public synchronized void columnAdd(SQLiteDatabase db, String tableName,
                                       String columnName, String columnType,
                                       Object defaultField) {
        try {
            if (null != db) {
                //查询第一条记录
                Cursor c = db.rawQuery(String.format(Locale.CHINESE, "SELECT * FROM %s LIMIT 1 ", tableName), null);
                boolean flag = true;
                if (c != null) {
                    //遍历检索是否已经存在该列
                    for (int i = 0; i < c.getColumnCount(); i++) {
                        if (columnName.equalsIgnoreCase(c.getColumnName(i))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {//插入列
                        db.execSQL(String.format(Locale.CHINESE, "ALTER TABLE %s ADD COLUMN %s %s default %s", tableName, columnName, columnType, defaultField));
                    }
                    c.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "表：%s 中 %s 列添加失败", tableName, columnName), e);
        }
    }


    /**
     * 更新表名称
     * ALTER TABLE 旧表名 RENAME TO 新表名
     *
     * @param db           SQLiteDatabase
     * @param oldTableName 旧表名
     * @param newTableName 新表名
     */
    public synchronized void tableRename(SQLiteDatabase db, String oldTableName, String newTableName) {
        try {
            db.execSQL(String.format(Locale.CHINESE, "ALTER TABLE %s RENAME TO %s", oldTableName, newTableName));
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "表：%s 更新名称为 %s 失败", oldTableName, newTableName), e);
        }
    }

    /**
     * 删除表
     * DROP TABLE 表名;
     *
     * @param db        SQLiteDatabase
     * @param tableName 表名
     */
    public synchronized void deleteTable(SQLiteDatabase db, String tableName) {
        try {
            db.execSQL(String.format(Locale.CHINESE, "DROP TABLE %s", tableName));
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "表：%s 删除失败", tableName), e);
        }
    }

    /**
     * 删除表中指定列(结合ORMLite使用)
     *
     * @param db               SQLiteDatabase
     * @param connectionSource ConnectionSource
     * @param tableName        表名
     * @param classz           表映射实体
     * @param retain           需要保留的列字段
     */
    public synchronized void columnDelete(SQLiteDatabase db, ConnectionSource connectionSource, String tableName, Class classz, String... retain) {
        try {
            String tableNameSuffix = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date(System.currentTimeMillis()));
            String tempTableName = String.format(Locale.CHINESE, "%s_%s", tableName, tableNameSuffix);
            tableRename(db, tableName, tempTableName);
            TableUtils.createTableIfNotExists(connectionSource, classz);
            StringBuffer buffer = new StringBuffer();
            for (String filed : retain) {
                buffer.append(filed + ",");
            }
            String fileds = buffer.substring(0, buffer.length() - 1);
            if (!TextUtils.isEmpty(fileds)) {
                db.execSQL(String.format(Locale.CHINESE, "INSERT INTO %s (%s) SELECT %s FROM %s", tableName, fileds, fileds, tempTableName));
            }
            deleteTable(db, tempTableName);
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e("删除表中指定列失败", e);
        }
    }
}