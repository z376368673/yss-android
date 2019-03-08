package com.android.baselib.db.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.android.baselib.log.ULog;

import java.sql.SQLException;


/**
 * 数据库创建、更新基类
 *
 * @author PF-NAN
 * @date 2018/2/27.
 */
public abstract class UOpenHelper extends OrmLiteSqliteOpenHelper {

    /**
     * 所有数据表的映射实体类的字节码对象
     */
    private Class[] mTableBeanClass;

    /**
     * 构造进行数据库信息初始化
     *
     * @param dbName         数据库名称
     * @param dbVersion      数据库版本
     * @param tableBeanClass 需要创建的表的映射实体字节码
     */
    public UOpenHelper(@NonNull Context context, @NonNull String dbName, int dbVersion, Class... tableBeanClass) {
        super(context, dbName, null, dbVersion);
        mTableBeanClass = tableBeanClass;
    }


    /**
     * 数据库表创建
     *
     * @param database         SQLiteDatabase
     * @param connectionSource ConnectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            if (null != mTableBeanClass && mTableBeanClass.length > 0) {
                for (Class tableBeanClass : mTableBeanClass) {
                    if (null != tableBeanClass) {
                        int tableIfNotExists = TableUtils.createTableIfNotExists(connectionSource, tableBeanClass);
                        ULog.e("创建表====" + tableIfNotExists + "===" + tableBeanClass);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库更新
     *
     * @param database         SQLiteDatabase
     * @param connectionSource ConnectionSource
     * @param oldVersion       数据库旧版本
     * @param newVersion       数据库新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
        update(database, connectionSource, oldVersion, newVersion);
    }

    /**
     * 子类实现,主要编写在数据库进行更新时需要进行的操作,可以借助DBTableOperate操作类进行处理
     *
     * @param database         SQLiteDatabase
     * @param connectionSource ConnectionSource
     * @param oldVersion       数据库旧版本U
     * @param newVersion       数据库新版本
     */
    protected abstract void update(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion);
}