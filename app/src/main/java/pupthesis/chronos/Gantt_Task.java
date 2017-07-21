package pupthesis.chronos;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import pupthesis.chronos.Adapter.GantTaskAdapter;

public class Gantt_Task extends BaseActivity implements  View.OnClickListener {

    ListView ganttlist;
    DataBaseHandler da;
    boolean isLongPress=false;


    String startformatdate="";
    String endformatdate="";
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2,fab_charts;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt__task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*  fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });*/

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

        ganttlist =(ListView)findViewById(R.id.listView);

        Loadlist();
    }
    private void showAlert() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(Gantt_Task.this);
        LinearLayout layout = new LinearLayout(Gantt_Task.this);
        LinearLayout chkboxholder = new LinearLayout(Gantt_Task.this);

        alert.setTitle("CREATE NEW TASK");
        layout.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setGravity(Gravity.LEFT);
        layout.setPadding(10,10,10,10);

        TextView taskname=new TextView(Gantt_Task.this);
        taskname.setPadding(0,10,0,0);
        taskname.setText("TASK NAME :");
        String ProjectName[];
        da=new DataBaseHandler(Gantt_Task.this);
        Cursor cursor =da.getLIST("select * from tasks");
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
            spinnerArrayAdapter = new ArrayAdapter<String>(Gantt_Task.this, R.layout.dropdownadapter, ProjectName); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdownadapter);
        }catch (Exception xx){

            TastyToast.makeText(Gantt_Task.this, "No Task Found.", Toast.LENGTH_SHORT,TastyToast.WARNING);

            return;
        }
        //PROJECTNAME.setAdapter(spinnerArrayAdapter);
        final MaterialBetterSpinner PROJECTNAME=new MaterialBetterSpinner(Gantt_Task.this);
        PROJECTNAME.setAdapter(spinnerArrayAdapter);

        chkboxholder.addView(taskname);
        chkboxholder.addView(PROJECTNAME);

        TextView starttime=new TextView(Gantt_Task.this);
        starttime.setText("START :");
        final EditText START=new EditText(Gantt_Task.this);
        START.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Gantt_Task.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                startformatdate=_year+","+_month+","+_day;
                                START.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        chkboxholder.addView(starttime);
        chkboxholder.addView(START);


        TextView endtime=new TextView(Gantt_Task.this);
        endtime.setText("END :");
        final   EditText END=new EditText(Gantt_Task.this);
        END.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Gantt_Task.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                END.setText(_year+","+_month+","+_day);
                                endformatdate=_year+","+_month+","+_day;
                                END.setText(_year+"/"+_month+"/"+_day);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        chkboxholder.addView(endtime);
        chkboxholder.addView(END);


        TextView percent=new TextView(Gantt_Task.this);

        percent.setText("PERCENT COMPLETE :");



        final   EditText  PERCENTCOMPLETE=new EditText(Gantt_Task.this);
        PERCENTCOMPLETE.setInputType(InputType.TYPE_CLASS_NUMBER);
        PERCENTCOMPLETE.setText("0");
        PERCENTCOMPLETE.setSelectAllOnFocus(true);
        chkboxholder.addView(percent);
        chkboxholder.addView(PERCENTCOMPLETE);

        final LinearLayout layout2 = chkboxholder;
        layout.addView(chkboxholder);
        alert.setView(layout);
        alert.setCancelable(false);


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

      final  AlertDialog dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(PROJECTNAME.getText().toString().equals("")){
                    AlertWronInput(PROJECTNAME);

                }
                  if(START.getText().toString().equals("")){
                    AlertWronInput(START);

                }
                  if(END.getText().toString().equals("")){
                    AlertWronInput(END);

                }
                  if(PERCENTCOMPLETE.getText().toString().equals("")){
                    AlertWronInput(PERCENTCOMPLETE);
                }
               if(counter==0){
                   da=new DataBaseHandler(Gantt_Task.this);
                   ContentValues cv=new ContentValues();
                   cv.put("task_name",PROJECTNAME.getText().toString());
                   cv.put("task_id",PROJECTNAME.getText().toString()+da.gantttaskcount());
                   cv.put("start_date",startformatdate);
                   cv.put("end_date",endformatdate);
                   cv.put("percent_complete",PERCENTCOMPLETE.getText().toString());
                   cv.put("project_id",Config.PROJECTID);
                   da.createNewGANTTTASK(cv);
                   Loadlist();
                   dialog.dismiss();
               }

                counter=0;
            }
        });
    }
    private  void AlertWronInput(EditText txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Gantt_Task.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
counter++;
    }
    private  void AlertWronInput(MaterialBetterSpinner txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Gantt_Task.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();

    }
    private  void Loadlist( ){

        final String taskname[];
        final String endtime[];
        final String starttime[];
        final String _percentcompelete[];
        final String _id[];
        da=new DataBaseHandler(Gantt_Task.this);
        Cursor cursor= da.getLIST("select * from gant_task where project_id="+Config.PROJECTID+" order by _id desc");
        endtime=new String[cursor.getCount()];
        starttime=new String[cursor.getCount()];
        _percentcompelete=new String[cursor.getCount()];
        taskname=new String[cursor.getCount()];
        _id=new String[cursor.getCount()];

        int i=1;
        if(cursor!=null){
            cursor.moveToFirst();
            try{
                endtime[0]=cursor.getString(4);
                taskname[0]=cursor.getString(2);
                starttime[0]=cursor.getString(5);
                _percentcompelete[0]=cursor.getString(3);
                _id[0]=cursor.getString(0);}catch (Exception xx){}
            while (cursor.moveToNext()){
                _id[i]=cursor.getString(0);
                endtime[i]=cursor.getString(4);
                taskname[i]=cursor.getString(2);
                starttime[i]=cursor.getString(5);
                _percentcompelete[i]=cursor.getString(3);
                i++;
            }

        }
        GantTaskAdapter adapter=new GantTaskAdapter(Gantt_Task.this,taskname,starttime,endtime,_percentcompelete,null);
        ganttlist.setAdapter(adapter);
        ganttlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongPress=true;
                String idid =_id[position];


                showAlert(_id[position],taskname[position],starttime[position].replace(",","/"),endtime[position].replace(",","/"),_percentcompelete[position]);
                return isLongPress;
            }
        });


    }
    private  void ToExcel(){
        da=new DataBaseHandler(Gantt_Task.this);

        final Cursor cursor = da.getLIST("select task_name,percent_complete,end_date,start_date,project_id from gant_task where project_id="+Config.PROJECTID);
        File filepath = Environment.getExternalStorageDirectory();
        File sd = new File(filepath.getAbsolutePath()
                + "/CHRONOS/");
        String csvFile = Config.PROJECTNAME+"-"+("0000" + Config.PROJECTID).substring(Config.PROJECTID.length())+".xls";

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
            WritableSheet sheet = workbook.createSheet(Config.PROJECTNAME, 0);
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
            TastyToast.makeText(Gantt_Task.this,"Data Exported in a Excel Sheet", Toast.LENGTH_SHORT,TastyToast.SUCCESS);
        }   catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(final String id,String task_name,String start_date,String end_date,String percent_complete) {
        final   AlertDialog.Builder alert = new AlertDialog.Builder(Gantt_Task.this);
        LinearLayout layout = new LinearLayout(Gantt_Task.this);
        LinearLayout chkboxholder = new LinearLayout(Gantt_Task.this);

        alert.setTitle("CREATE NEW TASK");
        alert.setCancelable(false);
        layout.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setGravity(Gravity.LEFT);
        layout.setPadding(10,10,10,10);
        TextView taskname=new TextView(Gantt_Task.this);
        taskname.setPadding(0,10,0,0);
        taskname.setText("TASK NAME :");
        String ProjectName[];
        da=new DataBaseHandler(Gantt_Task.this);
        Cursor cursor =da.getLIST("select * from tasks ");
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
            spinnerArrayAdapter = new ArrayAdapter<String>(Gantt_Task.this, R.layout.dropdownadapter, ProjectName); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdownadapter);
        }catch (Exception xx){

            TastyToast.makeText(Gantt_Task.this, "No Task Found.", Toast.LENGTH_SHORT,TastyToast.WARNING);
            return;
        }
        //PROJECTNAME.setAdapter(spinnerArrayAdapter);
        final MaterialBetterSpinner PROJECTNAME=new MaterialBetterSpinner(Gantt_Task.this);
        PROJECTNAME.setAdapter(spinnerArrayAdapter);
        PROJECTNAME.setText(task_name);
        chkboxholder.addView(taskname);
        chkboxholder.addView(PROJECTNAME);

        TextView starttime=new TextView(Gantt_Task.this);
        starttime.setText("START :");
        final EditText START=new EditText(Gantt_Task.this);
        START.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Gantt_Task.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                startformatdate=_year+","+_month+","+_day;
                                START.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        START.setText(start_date);

        chkboxholder.addView(starttime);
        chkboxholder.addView(START);


        TextView endtime=new TextView(Gantt_Task.this);
        endtime.setText("END :");
        final   EditText END=new EditText(Gantt_Task.this);
        END.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Gantt_Task.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                END.setText(_year+","+_month+","+_day);
                                endformatdate=_year+","+_month+","+_day;
                                END.setText(_year+"/"+_month+"/"+_day);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        chkboxholder.addView(endtime);
        chkboxholder.addView(END);

        END.setText(end_date);
        TextView percent=new TextView(Gantt_Task.this);

        percent.setText("PERCENT COMPLETE :");



        final   EditText  PERCENTCOMPLETE=new EditText(Gantt_Task.this);
        PERCENTCOMPLETE.setInputType(InputType.TYPE_CLASS_NUMBER);
        PERCENTCOMPLETE.setText(percent_complete);
        chkboxholder.addView(percent);
        chkboxholder.addView(PERCENTCOMPLETE);

        final LinearLayout layout2 = chkboxholder;
        layout.addView(chkboxholder);
        alert.setView(layout);
        alert.setCancelable(false);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

              //walang gagawin kase laaht ng event dito sa baba mang yayare
            }
        });
        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                da=new DataBaseHandler(Gantt_Task.this);
                da.ExecuteSql("delete from gant_task where _id ="+id);
                TastyToast.makeText(Gantt_Task.this,"Successfully Deleted.",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                Loadlist( );
            }
        });
        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
      final  AlertDialog dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(PROJECTNAME.getText().toString().equals("")){
                    AlertWronInput(PROJECTNAME);
                }
                  if(START.getText().toString().equals("")){
                    AlertWronInput(START);
                }
                  if(END.getText().toString().equals("")){
                    AlertWronInput(END);
                }
                  if(PERCENTCOMPLETE.getText().toString().equals("")){
                    AlertWronInput(PERCENTCOMPLETE);
                }
                if(counter==0){
                    da=new DataBaseHandler(Gantt_Task.this);
                    da.ExecuteSql("update gant_task set task_name ='"+PROJECTNAME.getText().toString()+
                            "',task_id='"+PROJECTNAME.getText().toString()+da.gantttaskcount()+
                            "',start_date='"+START.getText().toString().replace("/",",")+
                            "',end_date='"+END.getText().toString().replace("/",",")+
                            "',percent_complete='"+PERCENTCOMPLETE.getText().toString()+"' where _id ="+id);
                    Loadlist();
                    dialog.dismiss();}
                counter=0;
            }
        });
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
                showAlert();
                animateFAB();
                break;
            case R.id.fab_charts:
                Intent startmainactivity = new Intent(getApplicationContext(), Charts.class);
                startActivity(startmainactivity);

                animateFAB();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
