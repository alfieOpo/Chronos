package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.LineTaskAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class LineTask extends BaseActivity implements  View.OnClickListener {
    String _ProjectID="0";
    String _RefProjectID="0";
    String _ProjectNAME="N/A";
    int counter=0;

    DataBaseHandler da;
    ListView projectList;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2,fab_charts;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _ProjectID = getIntent().getStringExtra("project_id");
        _ProjectNAME=getIntent().getStringExtra("project_name");
        _RefProjectID=getIntent().getStringExtra("ref_project_id");
        projectList=(ListView)findViewById(R.id.projectList);

        fab_charts=(FloatingActionButton)findViewById(R.id.fab_charts);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab_exel);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_create);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab_charts.setOnClickListener(this);
        LoadList();

    }


    private  void ToExcel(){
        da=new DataBaseHandler(LineTask.this);

        final Cursor cursor = da.getLIST("select task_name,percent_complete,end_date,start_date,project_id from gant_task where project_id="+_ProjectID);
        File filepath = Environment.getExternalStorageDirectory();
        File sd = new File(filepath.getAbsolutePath()
                + "/CHRONOS/");
        String csvFile = _ProjectNAME+"-"+("0000" + _ProjectID).substring(_ProjectID.length())+".xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(_ProjectNAME, 0);
            // column and row

            sheet.addCell(new Label(0, 0, "Task Name"));
            sheet.addCell(new Label(1, 0, "Percent Complete"));
            sheet.addCell(new Label(2, 0, "End Date"));
            sheet.addCell(new Label(3, 0, "Start Date"));

            int i=0;
            if (cursor.moveToFirst()) {
                do {
                    String task_name = cursor.getString(cursor.getColumnIndex("task_name"));
                    String percent_complete = cursor.getString(cursor.getColumnIndex("percent_complete"));
                    String end_date = cursor.getString(cursor.getColumnIndex("end_date"));
                    String start_date = cursor.getString(cursor.getColumnIndex("start_date"));
                    i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, task_name));
                    sheet.addCell(new Label(1, i, percent_complete));
                    sheet.addCell(new Label(2, i, end_date.replace(",","/")));
                    sheet.addCell(new Label(3, i, start_date.replace(",","/")));
                } while (cursor.moveToNext());
            }
            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            TastyToast.makeText(LineTask.this,"Data Exported in a Excel Sheet", Toast.LENGTH_SHORT,TastyToast.SUCCESS);
        }   catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private  void EntryEntry(){

        LinearLayout linearLayout=new LinearLayout(LineTask.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        TextView taskname=new TextView(LineTask.this);
        taskname.setText("Task Name");

        String ProjectName[];
        da=new DataBaseHandler(LineTask.this);
        final Cursor cursor =da.getLIST("select * from tasksline where project_id="+_RefProjectID);
        ProjectName=new String[cursor.getCount()];
        ArrayAdapter<String> spinnerArrayAdapter;
        try {
            int i = 1;
            if (cursor != null) {
                cursor.moveToFirst();
                ProjectName[0] = cursor.getString(1);
                while (cursor.moveToNext()) {
                    ProjectName[i] = cursor.getString(1);
                    i++;
                }
            }
            spinnerArrayAdapter = new ArrayAdapter<String>(LineTask.this, R.layout.dropdownadapter, ProjectName); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdownadapter);
        }catch (Exception xx){

            TastyToast.makeText(LineTask.this, "No Task Found.", Toast.LENGTH_SHORT,TastyToast.WARNING);

            return;
        }
        final MaterialBetterSpinner TASKNAME=new MaterialBetterSpinner(LineTask.this);
        TASKNAME.setAdapter(spinnerArrayAdapter);
        TextView measure=new TextView(LineTask.this);
        measure.setText("Measure");
        final EditText MEASURE=new EditText(LineTask.this);
        MEASURE.setInputType(InputType.TYPE_CLASS_NUMBER);

        linearLayout.addView(taskname);
        linearLayout.addView(TASKNAME);

        linearLayout.addView(measure);
        linearLayout.addView(MEASURE);

/////////............................................
        final AlertDialog.Builder builder =new AlertDialog.Builder(LineTask.this);
        builder.setTitle("New Line Task");

        builder.setView(linearLayout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        final  AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter=0;
                WronInput(MEASURE);
                WronInput(TASKNAME);
                if(counter==0){
                    da=new DataBaseHandler(LineTask.this);
                    if(isExist(Config.Date(),TASKNAME.getText().toString())){
                        da.ExecuteSql("update line_task set measure=measure+"+MEASURE.getText().toString()+" where start_date='"+Config.Date()+"' and task_name='"+TASKNAME.getText().toString()+"' and project_id="+_ProjectID);
                        TastyToast.makeText(LineTask.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        LoadList();
                        dialog.cancel();
                    }
                    else {
                        ContentValues cv=new ContentValues();
                        cv.put("task_id",_ProjectNAME+_ProjectID);
                        cv.put("task_name",TASKNAME.getText().toString());
                        cv.put("measure",MEASURE.getText().toString());
                        cv.put("start_date", Config.Date());
                        cv.put("project_id",_ProjectID);

                        if(da.createNewLINETASK(cv)){
                            TastyToast.makeText(LineTask.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            LoadList();
                            dialog.cancel();
                        }}
                }
            }
        });
    }
    private  boolean isExist(String date,String name){
        da=new DataBaseHandler(LineTask.this);
        Cursor cursor=da.getLIST("select * from line_task where start_date='"+date+"' and task_name='"+name+"' and project_id="+_ProjectID);
        try{return cursor.getCount()!=0;}catch
                (Exception xx)
        {return  false;}

    }
    private  void WronInput(MaterialBetterSpinner view){


        if(view.getText().toString().equals("")){
            Animation    shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.setAnimation(shake);
            view.setError("No value found");
            TastyToast.makeText(LineTask.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();

            counter++;

        }

    }
    private  void WronInput(EditText view){


        if(view.getText().toString().equals("")){
            Animation    shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.setAnimation(shake);
            view.setError("No value found");
            TastyToast.makeText(LineTask.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
            counter++;

        }

    }
    private  void LoadList(){
        projectList.setAdapter(null);
        final String _measure[];
        final String _date[];
        final String _taskname[];


        da=new DataBaseHandler(LineTask.this);
        Cursor cursor=da.getLIST("select * from line_task where project_id="+_ProjectID);
        _measure=new String[cursor.getCount()];
        _date=new String[cursor.getCount()];
        _taskname=new String[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                _measure[i]=  cursor.getString(cursor.getColumnIndex("measure"))+" m.";
                _date[i]=  cursor.getString(cursor.getColumnIndex("start_date"));
                _taskname[i]=  cursor.getString(cursor.getColumnIndex("task_name"));
                i = cursor.getPosition() + 1;
            } while (cursor.moveToNext());
        }

        LineTaskAdapter adapter=new LineTaskAdapter(LineTask.this,_taskname,_date,_measure);
        projectList.setAdapter(adapter);
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab_charts.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab_charts.setClickable(false);
            isFabOpen = false;
            Log.d("Alfie", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab_charts.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab_charts.setClickable(true);
            isFabOpen = true;
            Log.d("Alfie","open");
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab_exel:
                ToExcel();
                animateFAB();

                break;
            case R.id.fab_create:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    EntryEntry();
                }
                animateFAB();
                break;
            case R.id.fab_charts:
                Intent startmainactivity = new Intent(getApplicationContext(), LineCharts.class);
                startmainactivity.putExtra("project_id", _ProjectID);
                startmainactivity.putExtra("project_name", _ProjectNAME);
                startmainactivity.putExtra("ref_project_id", _RefProjectID);
                startActivity(startmainactivity);

                animateFAB();
                break;
        }
    }
}
