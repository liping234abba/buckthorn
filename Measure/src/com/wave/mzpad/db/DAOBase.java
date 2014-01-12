package com.wave.mzpad.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.AbstractObject;

public abstract class DAOBase {

	protected String TAG = "DAOBase";
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * SqLiteDatabase 操作对象
     */
    private SQLiteDatabase mDataBase;

    /**
     * 构造函数
     * @param pContext
     */
    public DAOBase(Context pContext) {
        mContext = pContext;
    }

    /**
     * 得到一个上下文函数
     * @return Context
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * 获取一个SqLiteDatabase 数据库
     * @return SQLiteDatabase 对象
     */
    public SQLiteDatabase getDataBase() {
        if (mDataBase == null) {
            mDataBase = SQLiteHelper.getInstance(mContext).getWritableDatabase();
        }
        return mDataBase;
    }

    /**
     * 事物开始方法
     */
    public void beginTransaction() {
        getDataBase().beginTransaction();
    }

    /**
     * 设置事物成功方法
     */
    public void setTransactionSuccessful() {
        getDataBase().setTransactionSuccessful();
    }

    /**
     * 事物结束方法
     */
    public void endTransaction() {
        getDataBase().endTransaction();
    }

    /**
     * 根据条件获取表中记录数
     * @param pCondition :自定义条件
     * @return int
     */
    public int getCount(String pCondition) {
        String _String[] = getTableNameAndPK();
        String _SqlText = "SELECT " + _String[1] + " FROM " + _String[0] ;
        if (!Utility.isEmpty(pCondition)) {
            _SqlText = _SqlText + pCondition;
        }
        Cursor _Cursor = execSql(_SqlText);
        int _Count = _Cursor.getCount();
        _Cursor.close();
        return _Count;
    }

    /**
     * 根据条件获取表中记录数
     * @param pPK: 主键
     * @param pTableName :表名
     * @param pCondition : 自定义条件
     * @return int
     */
    public int getCount(String pPK, String pTableName, String pCondition) {
        String _SqlText = "SELECT " + pPK + " FROM " + pTableName;
        if (!Utility.isEmpty(pCondition)) {
            _SqlText = _SqlText + pCondition;
        }
        Cursor _Cursor = execSql(_SqlText);
        int _Count = _Cursor.getCount();
        _Cursor.close();
        return _Count;
    }

    /**
     * 根据条件删除表中的记录
     * @param pTableName
     * @param pCondition
     * @return Boolean 对象
     */
    protected Boolean delete(String pTableName, String pCondition) {
        return getDataBase().delete(pTableName, pCondition, null) >= 0;
    }
    
    /**
     * 根据条件删除表中的记录
     * @param pTableName
     * @param pCondition
     * @return Boolean 对象
     */
    protected Boolean delete(String pTableName,AbstractObject abstractObject) {
    	String delete_sql = abstractObject.COLUMN_ID + "-" + abstractObject.getId();
        return getDataBase().delete(pTableName, delete_sql, null) >= 0;
    }

    /**
     * 通过SQL语句获取一个返回List
     * @param pSqlText
     * @return
     */
    public List getList(String pSqlText) {
        Cursor _Cursor = null;
        if (!Utility.isEmpty(pSqlText)) {
            _Cursor = execSql(pSqlText);
        }
        return cursorToList(_Cursor);
    }

    /**
     * 从游标中获取n条记录
     * @param pCursor
     * @return List
     */
    protected List cursorToList(Cursor pCursor) {
        List _List = new ArrayList();
        while (pCursor.moveToNext()) {
            Object _Object = findModel(pCursor);
            _List.add(_Object);
        }
        pCursor.close();
        return _List;
    }

    /**
     * 根据Sql查询记录
     * @param pSqlText
     * @return
     */
    public Cursor execSql(String pSqlText) {
        return getDataBase().rawQuery(pSqlText, null);
    }

    /**
     * 根据Sql操作数据
     * @param pSqlText
     * @operate 新增，修改，删除等操作
     */
    public void execSql(String pSqlText, String operate) {
        getDataBase().execSQL(pSqlText);
    }

    /**
     * 得到表中的主键
     * @return String[] 对象数组
     */
    protected abstract String[] getTableNameAndPK();

    /**
     * 根据游标查找对象
     * @param pCursor
     * @return Object
     */
    protected abstract Object findModel(Cursor pCursor);
    
    /**
     * 判断对象是否存在
     */
    protected abstract boolean isExistObject(AbstractObject abstractObject);
}
