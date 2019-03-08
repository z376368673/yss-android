package com.android.baselib.db.dao;

import com.android.baselib.db.help.UOpenHelper;
import com.android.baselib.log.ULog;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * 表数据操作基类(仅提供简单的增删改查),
 * 复杂查询需要在子类中自行扩展实现;
 *
 * @author PF-NAN
 * @date 2018/2/26.
 */
public class UTableDao<T, V> {
    protected Class<T> mClassz;
    protected Dao<T, V> mDao;

    /**
     * 构造函数,子类必须处理
     *
     * @param openHelper 数据库操作基类
     */
    public UTableDao(UOpenHelper openHelper) {
        if (null == openHelper) {
            throw new RuntimeException(this.getClass().getSimpleName() + "中OrmLiteSqliteOpenHelper没有找到!");
        }
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.mClassz = (Class<T>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
        try {
            mDao = openHelper.getDao(this.mClassz);
        } catch (Exception ep) {
            ep.printStackTrace();
        }
    }

    /**
     * 插入单条数据,在没有ID字段的情况下使用最好
     * 有ID字段,使用insert(T tableBean, V idValue)
     *
     * @param tableBean 表单条映射数据实体
     * @return int      操作记录
     */
    public int insert(T tableBean) {
        int result = -1;
        try {
            result = mDao.create(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "数据添加失败,原因：%s", e.getMessage()));
        }
        return result;
    }

    /**
     * 插入单条数据
     *
     * @param tableBean 表单条映射数据实体
     * @param idValue   该条数据的ID字段值
     * @return int      操作记录
     */
    public int insert(T tableBean, V idValue) {
        int result = -1;
        try {
            T queryTableBean = mDao.queryForId(idValue);
            if (null == queryTableBean) {
                result = mDao.create(tableBean);
            } else {
                ULog.e(String.format(Locale.CHINESE, "ID为 %s 的数据已经存在,不可重复添加!", String.valueOf(idValue)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "数据添加失败,原因：%s", e.getMessage()));
        }
        return result;
    }


    /**
     * 插入多条数据
     *
     * @param mapTableBeans 表映射数据实体Map集合,key:该表的ID值,value:表单条映射的数据实体
     * @return int          操作记录
     */
    public int insert(Map<V, T> mapTableBeans) {
        int result = -1;
        List<T> tableBeans = new ArrayList<>();
        try {
            for (V idValue : mapTableBeans.keySet()) {
                T queryTableBean = mDao.queryForId(idValue);
                if (null == queryTableBean) {
                    tableBeans.add(mapTableBeans.get(idValue));
                } else {
                    ULog.e(String.format(Locale.CHINESE, "ID为 %s 的数据已经存在,不可重复添加!", String.valueOf(idValue)));
                }
            }
            result = mDao.create(tableBeans);
        } catch (SQLException e) {
            e.printStackTrace();
            tableBeans = null;
            ULog.e(String.format(Locale.CHINESE, "数据批量添加失败,原因：%s", e.getMessage()));
        }
        return result;
    }

    /**
     * 批量插入数据
     *
     * @param tableBeans
     * @return
     */
    public int insert(List<T> tableBeans) {
        int result = -1;
        try {
            if (null != tableBeans && !tableBeans.isEmpty()) {
                result = mDao.create(tableBeans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "数据批量添加失败,原因：%s", e.getMessage()));
        }
        return result;
    }


    /**
     * 通过id进行数据删除
     *
     * @param idValue 该表指定为ID的字段值
     * @return int    操作记录
     */
    public int deleteById(V idValue) {
        int result = -1;
        try {
            result = mDao.deleteById(idValue);

        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "通过ID %s 删除数据失败,原因：%s", String.valueOf(idValue), e.getMessage()));
        }
        return result;
    }

    /**
     * 指定列及该列值,删除该条数据
     *
     * @param columnName  列名
     * @param columnValue 列值
     * @return int        操作记录
     */
    public int deleteByColumn(String columnName, Object columnValue) {
        int result = -1;
        try {
            DeleteBuilder builder = mDao.deleteBuilder();
            builder.where().eq(columnName, columnValue);
            result = builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "自定义删除指定列数据失败,原因：%s", e.getMessage()));
        }
        return result;
    }

    /**
     * 删除 列columnName1 = columnValue1 并且 columnName2 = columnValue2
     *
     * @param columnName1  列名1
     * @param columnValue1 列值1
     * @param columnName2  列名2
     * @param columnValue2 列值2
     * @return int          操作记录
     */
    public int deleteByColumnAnd(String columnName1, Object columnValue1, String columnName2, Object columnValue2) {
        int result = -1;
        try {
            DeleteBuilder<T, V> builder = mDao.deleteBuilder();
            Where<T, V> where = builder.where();
            where.and(where.eq(columnName1, columnValue1), where.eq(columnName2, columnValue2));
            result = builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "自定义删除指定列数据失败 And,原因：%s", e.getMessage()));
        }
        return result;

    }

    /**
     * 删除 列columnName1 = columnValue1 或 columnName2 = columnValue2
     *
     * @param columnName1  列名1
     * @param columnValue1 列值1
     * @param columnName2  列名2
     * @param columnValue2 列值2
     * @return int          操作记录
     */
    public int deleteByColumnOr(String columnName1, Object columnValue1, String columnName2, Object columnValue2) {
        int result = -1;
        try {
            DeleteBuilder<T, V> builder = mDao.deleteBuilder();
            Where<T, V> where = builder.where();
            where.or(where.eq(columnName1, columnValue1), where.eq(columnName2, columnValue2));
            result = builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "自定义删除指定列数据失败 Or,原因：%s", e.getMessage()));
        }
        return result;
    }


    /**
     * 删除表中指定数据(可用于无ID 参数情况下删除)
     *
     * @param tableBean 数据映射实体
     * @return int      操作记录
     */
    public int delete(T tableBean) {
        int result = -1;
        try {
            result = mDao.delete(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "删除表中 %s 数据失败,原因：%s", tableBean.getClass().getSimpleName(), e.getMessage()));
        }
        return result;
    }

    /**
     * 删除表中所有数据
     *
     * @return int      操作记录
     */
    public int deleteAll() {
        int result = -1;
        try {
            result = mDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "删除所有数据失败,原因：%s", e.getMessage()));
        }
        return result;
    }

    /**
     * 更新表中数据
     *
     * @param tableBean 数据映射实体
     * @return int      操作记录
     */
    public int update(T tableBean) {
        int result = -1;
        try {
            result = mDao.update(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }

    /**
     * 更新该条数据的ID值
     *
     * @param tableBean 该条数据映射实体
     * @param newId     新的ID值
     * @return int      操作记录
     */
    public int update(T tableBean, V newId) {
        int result = -1;
        try {
            result = mDao.update(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 添加或者更新数据,
     * 可在返回的Dao.CreateOrUpdateStatus中通过isCreated()、isUpdated()判断类型，、,
     * 并且可以通过getNumLinesChanged()获取改变行数
     *
     * @param tableBean 单条数据映射实体
     * @return Dao.CreateOrUpdateStatus
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(T tableBean) {
        Dao.CreateOrUpdateStatus status = null;
        try {
            status = mDao.createOrUpdate(tableBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 查询表中所有数据
     *
     * @return List<T>
     */
    public List<T> queryAll() {
        List<T> tableBeans = new ArrayList<>();
        try {
            tableBeans.addAll(mDao.queryForAll());
        } catch (Exception e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "查询所有数据失败,原因：%s", e.getMessage()));
        }
        return tableBeans;
    }

    /**
     * 通过ID查询指定数据
     *
     * @param idValue 该表指定为ID的字段值
     * @return T(表映射实体)
     */
    public T queryById(V idValue) {
        T tableBean = null;
        try {
            if (null != idValue) {
                tableBean = mDao.queryForId(idValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "通过ID %s 查询数据失败,原因：%s", String.valueOf(idValue), e.getMessage()));
        }
        return tableBean;
    }

    /**
     * 指定列、列值查询该条数据
     *
     * @param columnName  列名
     * @param columnValue 列值
     * @return List<T>(表映射实体集合)
     */
    public List<T> queryByColumn(String columnName, Object columnValue) {
        List<T> tableBeans = new ArrayList<>();
        try {
            QueryBuilder<T, V> builder = mDao.queryBuilder();
            builder.where().eq(columnName, columnValue);
            tableBeans = builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "查询所有数据失败,原因：%s", e.getMessage()));
        }
        return tableBeans;
    }

    /**
     * 查询columnName1 = columnValue1 并且 columnName2 = columnValue2的数据
     *
     * @param columnName1  列名1
     * @param columnValue1 列值1
     * @param columnName2  列名2
     * @param columnValue2 列值2
     * @return List<T>  (表映射实体集合)
     */
    public List<T> queryByColumnAnd(String columnName1, Object columnValue1, String columnName2, Object columnValue2) {
        List<T> tableBeans = new ArrayList<>();
        try {
            QueryBuilder<T, V> builder = mDao.queryBuilder();
            Where<T, V> where = builder.where();
            where.and(where.eq(columnName1, columnValue1), where.eq(columnName2, columnValue2));
            tableBeans = builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "自定义查询数据失败 And ,原因：%s", e.getMessage()));

        }
        return tableBeans;
    }

    /**
     * 查询columnName1 = columnValue1 或 columnName2 = columnValue2的数据
     *
     * @param columnName1  列名1
     * @param columnValue1 列值1
     * @param columnName2  列名2
     * @param columnValue2 列值2
     * @return List<T>  (表映射实体集合)
     */
    public List<T> queryByColumnOr(String columnName1, Object columnValue1, String columnName2, Object columnValue2) {
        List<T> tableBeans = new ArrayList<>();
        try {
            QueryBuilder<T, V> builder = mDao.queryBuilder();
            Where<T, V> where = builder.where();
            where.or(where.eq(columnName1, columnValue1), where.eq(columnName2, columnValue2));
            tableBeans = builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "自定义查询数据失败 Or ,原因：%s", e.getMessage()));
        }
        return tableBeans;
    }

    /**
     * 查询表中数据总条数
     *
     * @return 总条数
     */
    public long count() {
        long result = 0;
        try {
            result = mDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
            ULog.e(String.format(Locale.CHINESE, "查询数据总条数失败,原因：%s", e.getMessage()));
        }
        return result;
    }
}