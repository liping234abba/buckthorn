package com.wave.mzpad.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.wave.mzpad.common.Log;

import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.AbstractObject;
import com.wave.mzpad.model.MeasureResult;
import com.wave.mzpad.model.StandardData;

/**
 * 操作StandardData对象对应的表业务类
 */
public class StandardDataDAO extends DAOBase {

  
	public static String TABLE_STANDARDDATA = "standard_data";//测量获取结果表
	
    /**
     * 通过上下文参数 实例化一个 SQLiteDALDistrict
     * @param pContext：上下文
     */
    public StandardDataDAO(Context pContext) {
        super(pContext);
    }

    /**
     * 获取表名和主键
     */
    @Override
    protected String[] getTableNameAndPK() {
        return new String[] {TABLE_STANDARDDATA, "id"};
    }

    /**
     * 根据游标查找对象
     */
    @Override
    protected Object findModel(Cursor pCursor) {
        StandardData _StandardData = null;
        if (!Utility.isEmpty(pCursor)) {
            _StandardData = new StandardData();
            _StandardData.setId(pCursor.getInt(pCursor.getColumnIndex("id")));
            _StandardData.setTrackHigh(pCursor.getInt(pCursor.getColumnIndex("track_high")));
            _StandardData.setTrainLimit(pCursor.getInt(pCursor.getColumnIndex("train_limit")));
            _StandardData.setSecondLevel(pCursor.getInt(pCursor.getColumnIndex("second_level")));
            _StandardData.setBuildRight(pCursor.getInt(pCursor.getColumnIndex("build_right")));
            _StandardData.setBuildDevious(pCursor.getInt(pCursor.getColumnIndex("build_devious")));
        }
        return _StandardData;
    }

    /**
     * 根据表中字段和StandardData创建一个 ContentValues
     * @param pInfo是一个StandardData 对象
     * @return ContentValues
     */
    public ContentValues createParms(StandardData pInfo) {
        ContentValues _contentValues = new ContentValues();
        _contentValues.put("track_high", pInfo.getTrackHigh());
        _contentValues.put("train_limit", pInfo.getTrainLimit());
        _contentValues.put("second_level", pInfo.getSecondLevel());
        _contentValues.put("build_right", pInfo.getBuildRight());
        _contentValues.put("build_devious", pInfo.getBuildDevious());
        return _contentValues;
    }

    /**
     * 向StandardData数据库中表中插入一条记录
     * @param standardData
     * @return true ：插入成功， false ：插入失败
     */
    public boolean insertStandardData(StandardData standardData) {
        ContentValues _ContentValues = createParms(standardData);
        long _NewID = getDataBase().insert(getTableNameAndPK()[0], null, _ContentValues);
        return _NewID > 0;
    }

    /**
     * 根据条件删除记录
     * @param pCondition
     * @return true 删除成功，false ：删除失败
     */
    public boolean deleteStandardData(String pCondition) {
        return delete(getTableNameAndPK()[0], pCondition);
    }

    /**
     * 更新记录
     * @param pCondition
     * @param standardData
     * @return true :成功：false ：失败
     */
    public boolean updateStandardData(String pCondition, StandardData standardData) {
        ContentValues _ContentValues = createParms(standardData);
        return getDataBase().update(getTableNameAndPK()[0], _ContentValues, pCondition, null) > 0;
    }

    /**
     * 更新记录
     * @param pCondition：条件
     * @param pContentValues ：ContentValues 对象
     * @return boolean ：true：成功； flase ：失败
     */
    public boolean updateStandardData(String pCondition, ContentValues pContentValues) {
        return getDataBase().update(TABLE_STANDARDDATA, pContentValues, pCondition, null) > 0;
    }

    /**
     * 获取一个StandardData List
     * @param pCondition
     * @return
     */
    public List<StandardData> getStandardData(String pCondition) {
        String _SqlText = "SELECT * FROM "+TABLE_STANDARDDATA ;
        if (!Utility.isEmpty(pCondition)) {
            _SqlText = _SqlText + pCondition;
        }
        Log.i(TAG, "SQL:"+_SqlText);
        return getList(_SqlText);
    }
   
	@Override
	protected boolean isExistObject(AbstractObject abstractObject) {
		if(abstractObject == null){
			return true ;
		}
		MeasureResult mr = (MeasureResult) abstractObject ;
		String sql = " where " + mr.COLUMN_ID +"="+ mr.getId()  ;
		List<StandardData> listMR = getStandardData(sql);
		if(Utility.isEmpty(listMR)){
			return false;
		}
		if(Utility.isEmpty(listMR.get(0))){
			return false;
		}
		return true;
	}
}
