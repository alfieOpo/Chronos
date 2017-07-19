package pupthesis.chronos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ALFIE on 7/13/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chronos";
    private static final String TABLE_GANTT= "gantt";
    private static final String TABLE_PROJECT= "projects";
    private static final String TABLE_TASK= "tasks";
    private static final String TABLE_GANT_TASK= "gant_task";
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(GANTTTABLE());
        } catch (Exception xx) {
        }
        try {
            db.execSQL( PROJECTTABLE());
        } catch (Exception xx) {
        }
        try {
            db.execSQL( TASKTABLE());
        } catch (Exception xx) {
        }
        try {
            db.execSQL( GANTTTASKTABLE());
        } catch (Exception xx) {
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GANTT);
        onCreate(db);
    }
    private String GANTTTABLE() {

        String CREATE_MCBMS_TABLE = "CREATE TABLE " + TABLE_GANTT + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "project_name TEXT," +
                "description TEXT," +
                "status TEXT)";
        return CREATE_MCBMS_TABLE;
    }
    private String GANTTTASKTABLE() {

        String CREATE_MCBMS_TABLE = "CREATE TABLE " + TABLE_GANT_TASK + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +

                "task_id TEXT," +
                "task_name TEXT," +
                "percent_complete TEXT," +
                "end_date TEXT," +
                "start_date TEXT," +
                "project_id TEXT)";
        return CREATE_MCBMS_TABLE;
    }
    private String PROJECTTABLE() {

        String CREATE_MCBMS_TABLE = "CREATE TABLE " + TABLE_PROJECT + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "project_name TEXT)";
        return CREATE_MCBMS_TABLE;
    }
    private String TASKTABLE() {

        String CREATE_MCBMS_TABLE = "CREATE TABLE " + TABLE_TASK + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "task_name TEXT)";
        return CREATE_MCBMS_TABLE;
    }
    public void createNewGANTT(ContentValues v) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_GANTT, null, v);
        if (rowInserted != -1) {
            Log.i("1st INSERT", "SUCCESS");
        } else {
            Log.i("1st INSERT", "FAILED");
        }
        db.close();
    }

    public void createNewGANTTTASK(ContentValues v) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_GANT_TASK, null, v);
        if (rowInserted != -1) {
            Log.i("1st INSERT", "SUCCESS");
        } else {
            Log.i("1st INSERT", "FAILED");
        }
        db.close();
    }
    public void createNewPROJECT(ContentValues v) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_PROJECT, null, v);
        if (rowInserted != -1) {
            Log.i("1st INSERT", "SUCCESS");
        } else {
            Log.i("1st INSERT", "FAILED");
        }
        db.close();
    }
    public void createNewTASK(ContentValues v) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_TASK, null, v);
        if (rowInserted != -1) {
            Log.i("1st INSERT", "SUCCESS");
        } else {
            Log.i("1st INSERT", "FAILED");
        }
        db.close();
    }
    public void updateGANTT(String description,String project_name,String status,String _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="update gantt set project_name='"+project_name+"',description='"+description+"',status='"+status+"' where _id="+_id;
        db.execSQL(sql);
        db.close();
    }
    public void ExecuteSql(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public Cursor getLIST(String query) {

        Cursor cursor=null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
              cursor = db.rawQuery(query, null);
        }catch (Exception xx){

            return null;
        }
        return cursor;

    }
    public void DeleteProject(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql="delete from "+TABLE_PROJECT+" where _id="+id;
        db.execSQL(sql);
        db.close();
    }

    public void DeleteTask(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql="delete from "+TABLE_TASK+" where _id="+id;
        db.execSQL(sql);
        db.close();
    }

    public int gantttaskcount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_GANT_TASK, null);
        return cursor.getCount();

    }

}
