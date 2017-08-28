package pupthesis.chronos.Access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pupthesis.chronos.Util.Config;

/**
 * Created by ALFIE on 7/13/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chronos";
    private static final String TABLE_GANTT= "gantt";
    private static final String TABLE_PROJECT= "projects";
    private static final String TABLE_PROJECTLINE= "projectsline";
    private static final String TABLE_TASK= "tasks";
    private static final String TABLE_GANT_TASK= "gant_task";
    private static final String TABLE_LINE_TASK= "line_task";
    private static final String TABLE_LINE= "line";
    private static final String TABLE_TASKLINE= "tasksline";

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
            db.execSQL( PROJECTTABLELINE());
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
        try {
            db.execSQL( LINETABLE());
        } catch (Exception xx) {
        }
        try {
            db.execSQL( TASKTABLELINE());
        } catch (Exception xx) {
        }
        try {
            db.execSQL( LINETASKTABLE());
        } catch (Exception xx) {
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GANTT);
        onCreate(db);
    }
    private String GANTTTABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_GANTT + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "project_name TEXT," +
                "description TEXT," +
                "status TEXT," +
                "ref_project_id INTEGER)";
        return CREATE_CHRONOS_TABLE;
    }
    private String GANTTTASKTABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_GANT_TASK + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "task_id TEXT," +
                "task_name TEXT," +
                "percent_complete TEXT," +
                "end_date TEXT," +
                "start_date TEXT," +
                "project_id TEXT," +
                "isseen INTEGER  DEFAULT 0)";
        return CREATE_CHRONOS_TABLE;
    }

    private String LINETASKTABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_LINE_TASK + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "task_id varchar(255)," +
                "task_name varchar(255)," +
                "measure decimal (18,2)," +
                "start_date date," +
                "project_id int," +
                "isseen INTEGER  DEFAULT 0)";
        return CREATE_CHRONOS_TABLE;
    }

    private String PROJECTTABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_PROJECT + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "project_name TEXT)";
        return CREATE_CHRONOS_TABLE;
    }

    private String PROJECTTABLELINE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_PROJECTLINE + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +

                "project_name TEXT)";
        return CREATE_CHRONOS_TABLE;
    }
    private String TASKTABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_TASK + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "task_name TEXT," +
                "project_id TEXT)";
        return CREATE_CHRONOS_TABLE;
    }
    private String TASKTABLELINE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_TASKLINE + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "task_name TEXT," +
                "project_id TEXT)";
        return CREATE_CHRONOS_TABLE;
    }
    private String LINETABLE() {

        String CREATE_CHRONOS_TABLE = "CREATE TABLE " + TABLE_LINE + "( " +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT," +
                "line_name TEXT," +
                "status TEXT," +
                "start_date date," +
                "end_date date," +
                "ref_project_id INTEGER)";
        return CREATE_CHRONOS_TABLE;
    }

    public boolean createNewLINE(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_LINE, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewGANTT(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_GANTT, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewLINETASK(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_LINE_TASK, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewGANTTTASK(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_GANT_TASK, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewPROJECT(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_PROJECT, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewPROJECTLINE(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_PROJECTLINE, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewTASK(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_TASK, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
    }
    public boolean createNewTASKLINE(ContentValues v) {
        boolean retval=false;
        SQLiteDatabase db = this.getWritableDatabase();
        long rowInserted = db.insert(TABLE_TASKLINE, null, v);
        if (rowInserted != -1) {
            retval= true;
        } else {
            retval= false;
        }
        db.close();
        return retval;
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
String xa=xx.getMessage();
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
    public String getCountofProjectUnseen(String Project_id) {
        String countTask="0";
        try {
            String sql =" select * from gant_task where project_id = "+Project_id+" and start_date ='"+ Config.Date().replace("/",",")+"' and isseen =0";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countTask=String.valueOf(cursor.getCount());
            return countTask;

        }catch (Exception dd){

            return countTask;
        }
    }

    public String getCountTaskforProject(String Project_id) {
        String countTask="0";
        try {
            String sql =" select * from tasks where project_id = "+Project_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countTask=String.valueOf(cursor.getCount());
            return countTask;

        }catch (Exception dd){
            return countTask;
        }
    }
    public String getCountGantt() {
        String countProject="0";
        try {
            String sql =" select * from gantt";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countProject=String.valueOf(cursor.getCount());
            return countProject;

        }catch (Exception dd){
            return countProject;
        }
    }

    public String getCountProjects() {
        String countProject="0";
        try {
            String sql =" select * from projects";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countProject=String.valueOf(cursor.getCount());
            return countProject;

        }catch (Exception dd){
            return countProject;
        }
    }


    public String getCountTaskforProjectLine(String Project_id) {
        String countTask="0";
        try {
            String sql =" select * from tasksline where project_id = "+Project_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countTask=String.valueOf(cursor.getCount());
            return countTask;

        }catch (Exception dd){
            return countTask;
        }
    }

    public int getCountlineTask(String Project_id) {
        int countTask=0;
        try {
            String sql =" select distinct start_date from line_task where project_id = "+Project_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countTask=cursor.getCount();
            return countTask;

        }catch (Exception dd){
            return countTask;
        }
    }
    public void CheckData(String _ProjectID,String refproject_id,String _ProjectNAME){

        try {
            String sql =" select count(_id) from line_task where project_id = "+_ProjectID+" and start_date='"+Config.Date()+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor.moveToFirst()){
String val=cursor.getString(0);
                    if(val.equals("0")){
                        String sql2 =" select project_id,task_name from tasksline where project_id = "+refproject_id+"";

                        Cursor cursor1 = db.rawQuery(sql2, null);
                        if(cursor1.moveToFirst()){
                            do{
                                ContentValues cv=new ContentValues();
                                cv.put("task_id",_ProjectNAME+_ProjectID);
                                cv.put("task_name",cursor1.getString(1));
                                cv.put("measure",0);
                                cv.put("start_date", Config.Date());
                                cv.put("project_id",_ProjectID);

                                if(createNewLINETASK(cv)){

                                }

                            }while (cursor1.moveToNext());
                        }

                    }
                }


        }catch (Exception dd){
            String a=dd.getMessage();
            String a1=dd.getMessage();
        }

    }
    public int countoftask(String ref_id){//getting the list of task inside the [project it will be use for the series of colors in chart

        int countTask=0;
        try {
            String sql =" select distinct task_name from "+TABLE_TASKLINE+" where project_id = "+ref_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            countTask=cursor.getCount();
            return countTask;

        }catch (Exception dd){
            return countTask;
        }
    }
    public String enddate(String project_id){//getting the end date use for validating insert


        try {
            String sql ="select end_date from  line where _id="+project_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
           if(cursor.moveToFirst()){
               return  cursor.getString(0);
           }
           else {

               return Config.Date();
           }

        }catch (Exception dd){

        }
        return Config.Date();
    }
}
