package com.wave.mzpad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.wave.mzpad.common.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private String TAG = "SQLiteHelper";
	
    /**
     * SQLiteHelper 实例
     */
    private static SQLiteHelper INSTANCE;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据库名称
     */
    private static String dataBaseName = "wave_mzpad.db";

    /**
     * 数据库版本
     */
    private static int dataBaseVersion= 1 ;
    
    /**
     * 构造函数
     * @param pContext
     */
    private SQLiteHelper(Context pContext,String dbName,CursorFactory cursor, int dbVersion) {
    	super(pContext, dbName, cursor, dbVersion);
    	Log.i(TAG , " SQLiteHelper: version" + dbVersion + "dbName:" +dbName );
        mContext = pContext;
        getWritableDatabase();
    }

    /**
     * 获取SQLiteHelper 实例
     * @param pContext
     * @return
     */
    public static SQLiteHelper getInstance(Context pContext) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteHelper(pContext, dataBaseName, null, dataBaseVersion);
        }
        return INSTANCE;
    }

    /**
     * 创建数据库
     */
    @Override
    public void onCreate(SQLiteDatabase pDataBase) {
    	Log.i(TAG," SQLiteHelper->onCreate");
    	String create_sql = "CREATE TABLE " + MeasureParamDAO.TABLE_MEASUREPARAM
    			        +"(id INTEGER PRIMARY KEY AUTOINCREMENT,"
    					+"line_number VARCHAR(20),"
    					+"line_name  VARCHAR(20),"
    					+"track  INTEGER DEFAULT 0,"
    					+"radius  INTEGER DEFAULT 0,"
    					+"outerrail_high  INTEGER DEFAULT 0,"
    					+"sample_interval  INTEGER DEFAULT 0,"
    					+"inner_side  INTEGER DEFAULT 0,"
    					+"stand_name  VARCHAR(20),"
    					+"stand_id  VARCHAR(20),"
    					+"stand_area  VARCHAR(20),"
    					+"stand_direction  VARCHAR(20),"
    					+"stand_orientation  INTEGER DEFAULT 0,"
    					+"bight_direction  INTEGER DEFAULT 0,"
    					+"rail_high  INTEGER DEFAULT 0,"
    					+"measure_startpos  VARCHAR(50) );";
    	Log.i(TAG, "create_sql:"+create_sql);
    	pDataBase.execSQL(create_sql);
    		create_sql = "CREATE TABLE " + MeasureResultDAO.TABLE_MEASURERESULT
    				+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+"travel_distance INTEGER  DEFAULT 0,"
    				+"platform_high INTEGER  DEFAULT 0,"
    				+"platform_distance INTEGER  DEFAULT 0,"
    				+"dip_angle  INTEGER  DEFAULT 0,"
    				+"rainshed_high  INTEGER  DEFAULT 0,"
    				+"param_id  INTEGER );";
    		Log.i(TAG, "create_sql:"+create_sql);
      pDataBase.execSQL(create_sql);
      		create_sql = "CREATE TABLE " + StandardDataDAO.TABLE_STANDARDDATA 
    		  +"(id  INTEGER PRIMARY KEY AUTOINCREMENT,"
    		  +"track_high  INTEGER  DEFAULT 0,"
    		  +"train_limit  INTEGER  DEFAULT 0,"
    		  +"second_level  INTEGER  DEFAULT 0,"
    		  +"build_right  INTEGER  DEFAULT 0,"
    		  +"build_devious  INTEGER  DEFAULT 0);";
      		Log.i(TAG, "create_sql:"+create_sql);
     pDataBase.execSQL(create_sql);
    }

    /**
     * 升级数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase pDataBase, int paramVersonOld, int paramVersionNew) {
    	Log.e(TAG," SQLiteHelper->onUpgrade old:" + paramVersonOld + " new:" + paramVersionNew);
    }
    
}
