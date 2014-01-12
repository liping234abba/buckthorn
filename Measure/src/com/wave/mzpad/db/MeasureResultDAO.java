package com.wave.mzpad.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.AbstractObject;
import com.wave.mzpad.model.MeasureResult;

/**
 * 操作MeasureResult对象对应的表业务类
 */
public class MeasureResultDAO extends DAOBase {

  
	public static String TABLE_MEASURERESULT = "measure_result";//测量获取结果表
	
    /**
     * 通过上下文参数 实例化一个 SQLiteDALDistrict
     * @param pContext：上下文
     */
    public MeasureResultDAO(Context pContext) {
        super(pContext);
    }

    /**
     * 获取表名和主键
     */
    @Override
    protected String[] getTableNameAndPK() {
        return new String[] {TABLE_MEASURERESULT, "id"};
    }

    /**
     * 根据游标查找对象
     */
    @Override
    protected Object findModel(Cursor pCursor) {
        MeasureResult _measureResult = null;
        if (!Utility.isEmpty(pCursor)) {
            _measureResult = new MeasureResult();
            _measureResult.setId(pCursor.getInt(pCursor.getColumnIndex("id")));
            _measureResult.setTravelDistance(pCursor.getInt(pCursor.getColumnIndex("travel_distance")));
            _measureResult.setPlatformHigh(pCursor.getInt(pCursor.getColumnIndex("platform_high")));
            _measureResult.setPlatformDistance(pCursor.getInt(pCursor.getColumnIndex("platform_distance")));
            _measureResult.setDipAngle(pCursor.getInt(pCursor.getColumnIndex("dip_angle")));
            _measureResult.setRainshedHigh(pCursor.getInt(pCursor.getColumnIndex("rainshed_high")));
            _measureResult.setParamId(pCursor.getInt(pCursor.getColumnIndex("param_id")));
        }
        return _measureResult;
    }

    /**
     * 根据表中字段和MeasureResult创建一个 ContentValues
     * @param pInfo是一个MeasureResult 对象
     * @return ContentValues
     */
    public ContentValues createParms(MeasureResult pInfo) {
        ContentValues _contentValues = new ContentValues();
        _contentValues.put("travel_distance", pInfo.getTravelDistance());
        _contentValues.put("platform_high", pInfo.getPlatformHigh());
        _contentValues.put("platform_distance", pInfo.getPlatformDistance());
        _contentValues.put("dip_angle", pInfo.getDipAngle());
        _contentValues.put("rainshed_high", pInfo.getRainshedHigh());
        _contentValues.put("param_id", pInfo.getParamId());
        return _contentValues;
    }

    /**
     * 向MeasureResult数据库中表中插入一条记录
     * @param pMeasureResult
     * @return true ：插入成功， false ：插入失败
     */
    public MeasureResult insertMeasureResult(MeasureResult pMeasureResult) {
        ContentValues _ContentValues = createParms(pMeasureResult);
        long _NewID = getDataBase().insert(getTableNameAndPK()[0], null, _ContentValues);
        if(_NewID > 0){
        	pMeasureResult.setId((int)_NewID);
        }else{
        	return null ;
        }
        return pMeasureResult ;
    }

    /**
     * 根据条件删除记录
     * @param pCondition
     * @return true 删除成功，false ：删除失败
     */
    public boolean deleteMeasureResult(String pCondition) {
        return delete(getTableNameAndPK()[0], pCondition);
    }

    /**
     * 更新记录
     * @param pCondition
     * @param pMeasureResult
     * @return true :成功：false ：失败
     */
    public boolean updateMeasureResult(String pCondition, MeasureResult pMeasureResult) {
        ContentValues _ContentValues = createParms(pMeasureResult);
        return getDataBase().update(getTableNameAndPK()[0], _ContentValues, pCondition, null) > 0;
    }

    /**
     * 更新记录
     * @param pCondition：条件
     * @param pContentValues ：ContentValues 对象
     * @return boolean ：true：成功； flase ：失败
     */
    public boolean updateMeasureResult(String pCondition, ContentValues pContentValues) {
        return getDataBase().update(TABLE_MEASURERESULT, pContentValues, pCondition, null) > 0;
    }

    /**
     * 获取一个MeasureResult List
     * @param pCondition
     * @return
     */
    public List<MeasureResult> getMeasureResult(String pCondition) {
        String _SqlText = "SELECT * FROM "+TABLE_MEASURERESULT ;
        if (!Utility.isEmpty(pCondition)) {
            _SqlText = _SqlText + pCondition;
        }
        return getList(_SqlText);
    }

	@Override
	protected boolean isExistObject(AbstractObject abstractObject) {
		if(abstractObject == null){
			return true ;
		}
		MeasureResult mr = (MeasureResult) abstractObject ;
		String sql = " where " + mr.COLUMN_ID +"="+ mr.getId() + " AND " + mr.COLUMN_PARAMID + "="+ mr.getParamId() ;
		List<MeasureResult> listMR = getMeasureResult(sql);
		if(Utility.isEmpty(listMR)){
			return false;
		}
		if(Utility.isEmpty(listMR.get(0))){
			return false;
		}
		return true;
	}
}
