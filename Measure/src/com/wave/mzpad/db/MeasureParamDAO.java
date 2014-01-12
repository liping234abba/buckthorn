package com.wave.mzpad.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.wave.mzpad.common.Utility;
import com.wave.mzpad.model.AbstractObject;
import com.wave.mzpad.model.MeasureParam;

/**
 * 操作MeasureParam对象对应的表业务类
 */
public class MeasureParamDAO extends DAOBase {

	public static String TABLE_MEASUREPARAM = "measure_param"; //测量输入参数表
	
    /**
     * 通过上下文参数 实例化一个 SQLiteDALDistrict
     * @param pContext：上下文
     */
    public MeasureParamDAO(Context pContext) {
        super(pContext);
    }

    /**
     * 获取表名和主键
     */
    @Override
    protected String[] getTableNameAndPK() {
        return new String[] {TABLE_MEASUREPARAM, "id"};
    }

    /**
     * 根据游标查找对象
     */
    @Override
    protected Object findModel(Cursor pCursor) {
        MeasureParam _measureParam = null;
        if (!Utility.isEmpty(pCursor)) {
            _measureParam = new MeasureParam();
            _measureParam.setId(pCursor.getInt(pCursor.getColumnIndex("id")));
            _measureParam.setLineNumber(pCursor.getInt(pCursor.getColumnIndex("line_number")));
            _measureParam.setTrack(pCursor.getInt(pCursor.getColumnIndex("track")));
            _measureParam.setRadius(pCursor.getInt(pCursor.getColumnIndex("radius")));
            _measureParam.setOuterrailHigh(pCursor.getInt(pCursor.getColumnIndex("outerrail_high")));
            _measureParam.setSampleInterval(pCursor.getInt(pCursor.getColumnIndex("sample_interval")));
            _measureParam.setInnerSide(pCursor.getInt(pCursor.getColumnIndex("inner_side")));
            _measureParam.setMeasureStartposition(pCursor.getInt(pCursor.getColumnIndex("measure_startpos")));
            _measureParam.setLineName(pCursor.getString(pCursor.getColumnIndex("line_name")));
        }
        return _measureParam;
    }

    /**
     * 根据表中字段和MeasureParam创建一个 ContentValues
     * @param pInfo是一个MeasureParam 对象
     * @return ContentValues
     */
    public ContentValues createParms(MeasureParam pInfo) {
        ContentValues _contentValues = new ContentValues();
        _contentValues.put("line_number", pInfo.getLineNumber());
        _contentValues.put("track", pInfo.getTrack());
        _contentValues.put("radius", pInfo.getRadius());
        _contentValues.put("outerrail_high", pInfo.getOuterrailHigh());
        _contentValues.put("sample_interval", pInfo.getSampleInterval());
        _contentValues.put("inner_side", pInfo.getInnerSide());
        _contentValues.put("measure_startpos", pInfo.getMeasureStartposition());
        _contentValues.put("line_name", pInfo.getLineName());
        return _contentValues;
    }

    /**
     * 向MeasureParam数据库中表中插入一条记录
     * @param pMeasureParam
     * @return true ：插入成功， false ：插入失败
     */
    public MeasureParam insertMeasureParam(MeasureParam pMeasureParam) {
        ContentValues _ContentValues = createParms(pMeasureParam);
        long _NewID = getDataBase().insert(getTableNameAndPK()[0], null, _ContentValues);
        pMeasureParam.setId((int)_NewID);
        return pMeasureParam;
    }

    /**
     * 根据条件删除记录
     * @param pCondition
     * @return true 删除成功，false ：删除失败
     */
    public boolean deleteMeasureParam(String pCondition) {
        return delete(TABLE_MEASUREPARAM, pCondition);
    }
    
    /**
     * 根据条件删除记录
     * @param pCondition
     * @return true 删除成功，false ：删除失败
     */
    public boolean deleteMeasureParam(MeasureParam measureParam) {
        return delete(TABLE_MEASUREPARAM, measureParam);
    }

    /**
     * 更新记录
     * @param pCondition
     * @param pMeasureParam
     * @return true :成功：false ：失败
     */
    public boolean updateMeasureParam(MeasureParam pMeasureParam) {
    	String pCondition = pMeasureParam.COLUMN_ID + "=" + pMeasureParam.getId() ;
        ContentValues _ContentValues = createParms(pMeasureParam);
        return getDataBase().update(TABLE_MEASUREPARAM, _ContentValues, pCondition, null) > 0;
    }

    /**
     * 更新记录
     * @param pCondition：条件
     * @param pContentValues ：ContentValues 对象
     * @return boolean ：true：成功； flase ：失败
     */
    public boolean updateMeasureParam(String pCondition, ContentValues pContentValues) {
        return getDataBase().update(TABLE_MEASUREPARAM, pContentValues, pCondition, null) > 0;
    }

    /**
     * 获取一个MeasureParam List
     * @param pCondition
     * @return
     */
    public List<MeasureParam> getMeasureParam(String pCondition) {
        String _SqlText = "SELECT * FROM "+TABLE_MEASUREPARAM ;
        if (!Utility.isEmpty(pCondition)) {
            _SqlText = _SqlText + pCondition;
        }
        return getList(_SqlText);
    }

	@Override
	public boolean isExistObject(AbstractObject abstractObject) {
		if(abstractObject== null){
			return true;
		}
		MeasureParam measureParam = (MeasureParam)abstractObject;
		String sql = " where " + measureParam.COLUMN_LINE_NUMBER+"=" + measureParam.getLineNumber() + " AND " + measureParam.COLUMN_LINE_NAME + "='" + measureParam.getLineName() +"'";
		List<MeasureParam> listMP = getMeasureParam(sql);
		if(Utility.isEmpty(listMP)){
			return false;
		}
		if(Utility.isEmpty(listMP.get(0))){
			return false;
		}
		return true;
	}
}
