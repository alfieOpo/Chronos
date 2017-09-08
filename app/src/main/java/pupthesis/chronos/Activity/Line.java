package pupthesis.chronos.Activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.util.Calendar;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.LineAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class Line extends BaseActivity {
    DataBaseHandler da;
    boolean islongpress=false;
    ListView linelist;
    String startformatdate="";
      String endformatdate="";
    ArrayAdapter<String>   spinnerArrayAdapter2;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linelist=(ListView)findViewById(R.id.linelist);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        LoadList();
    }
    private  void LoadList(){
        linelist.setAdapter(null);
        da=new DataBaseHandler(Line.this);
        Cursor cursor= da.getLIST("select * from line");
        final String LineName[]=new String[cursor.getCount()];
        final String Status[]=new String[cursor.getCount()];
        final String start_date[]=new String[cursor.getCount()];
        final String end_date[]=new String[cursor.getCount()];
        final String ID[]=new String[cursor.getCount()];
        Config.islastpage=false;
        final String ref_project_id[]=new String[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                start_date[i]=cursor.getString(cursor.getColumnIndex("start_date"));
                end_date[i]=cursor.getString(cursor.getColumnIndex("end_date"));
                LineName[i]=cursor.getString(cursor.getColumnIndex("line_name"));
                Status[i]=cursor.getString(cursor.getColumnIndex("status"));
                ID[i]=cursor.getString(cursor.getColumnIndex("_id"));
                ref_project_id[i]=cursor.getString(cursor.getColumnIndex("ref_project_id"));
                i = cursor.getPosition() + 1;

            } while (cursor.moveToNext());
        }
        LineAdapter adapter=new LineAdapter(Line.this,LineName,Status,ID);
        linelist.setAdapter(adapter);
        linelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!islongpress){
                    Intent startmainactivity = new Intent(Line.this, LineTask.class);
                    startmainactivity.putExtra("project_id", ID[position]);
                    startmainactivity.putExtra("project_name", LineName[position].toUpperCase());
                    startmainactivity.putExtra("ref_project_id", ref_project_id[position]);
                    startmainactivity.putExtra("date_start", start_date[position]);
                    startmainactivity.putExtra("date_end", end_date[position]);
                    startmainactivity.putExtra("status", Status[position]);
                    startActivity(startmainactivity);
                }
            }
        });
        linelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                islongpress=true;
                showAlert(start_date[position],end_date[position],LineName[position], Status[position], ID[position]);

                return islongpress;
            }
        });
    }
    private  void showAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Line.this);
        builder.setTitle("New Line");
        builder.setCancelable(false);
        LinearLayout layout=new LinearLayout(Line.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(10,10,10,10);
        /////
        TextView linename=new TextView(Line.this);
        linename.setText("Line name");

        String ProjectName[];
        final String ref_ProjectID[];

        da=new DataBaseHandler(Line.this);
        Cursor cursor =da.getLIST("select * from projectsline ");
        ProjectName=new String[cursor.getCount()];
        ref_ProjectID=new String[cursor.getCount()];
        final ArrayAdapter<String> spinnerArrayAdapter;
        try {
            int i = 1;
            if (cursor != null) {
                cursor.moveToFirst();
                ProjectName[0] = cursor.getString(1);
                ref_ProjectID[0] = cursor.getString(0);
                while (cursor.moveToNext()) {
                    ProjectName[i] = cursor.getString(1);
                    ref_ProjectID[i] = cursor.getString(0);
                    i++;
                }
            }
            spinnerArrayAdapter  = new ArrayAdapter<String>(Line.this,R.layout.dropdownadapter,ProjectName);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdownadapter);
        }catch (Exception xx){
            TastyToast.makeText(Line.this, "No Project Found.", Toast.LENGTH_SHORT,TastyToast.WARNING);
            return;
        }
        final MaterialBetterSpinner LINEPROJECT=new MaterialBetterSpinner(Line.this);

        LINEPROJECT.setAdapter(spinnerArrayAdapter);

        layout.addView(linename);
        layout.addView(LINEPROJECT);
        /////
        TextView status=new TextView(Line.this);
        status.setText("Status");
        final MaterialBetterSpinner STATUS=new MaterialBetterSpinner(Line.this);
        spinnerArrayAdapter2  = new ArrayAdapter<String>(Line.this, R.layout.dropdownadapter,getResources().getStringArray(R.array.LINESTATUS)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.dropdownadapter);
        STATUS.setAdapter(spinnerArrayAdapter2);
        layout.addView(status);
        layout.addView(STATUS);
        TextView lbl_start_date=new TextView(this);
        lbl_start_date.setText("Date Start");
        TextView lbl_end_date=new TextView(this);
        lbl_end_date.setText("Date End");

     final   EditText txt_start=new EditText(this);
        txt_start.setFocusableInTouchMode(false);
        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Line.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                  startformatdate = _year + "," + _month + "," + _day;
                                txt_start.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        final   EditText txt_end=new EditText(this);
        txt_end.setFocusableInTouchMode(false);
        txt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Line.this,
                        new DatePickerDialog.OnDateSetListener() {



                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                  endformatdate = _year + "," + _month + "," + _day;
                                txt_end.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        layout.addView(lbl_start_date);
        layout.addView(txt_start);
layout.addView(lbl_end_date);
        layout.addView(txt_end);
        ////
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(!Config.ValidDate(txt_start.getText().toString(),txt_end.getText().toString())){

                        AlertWronInput(txt_end);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(STATUS.getText().toString().equals("")){
                    AlertWronInput(STATUS);
                }
                if(LINEPROJECT.getText().toString().equals("")){
                    AlertWronInput(LINEPROJECT);
                }
                ContentValues cv=new ContentValues();

                cv.put("start_date",txt_start.getText().toString());
                cv.put("end_date",txt_end.getText().toString());
                cv.put("line_name",LINEPROJECT.getText().toString());
                cv.put("status",STATUS.getText().toString());
                int position=spinnerArrayAdapter.getPosition(LINEPROJECT.getText().toString());
                cv.put("ref_project_id",ref_ProjectID[position]);
                da=new DataBaseHandler(Line.this);
                if(da.createNewLINE(cv)){
                    TastyToast.makeText(Line.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    LoadList();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final    AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
    }

    private  void showAlert(String _start_date, String _end_date, String _name, String _status, final String _id){
        AlertDialog.Builder builder=new AlertDialog.Builder(Line.this);
        builder.setTitle(_name);
        builder.setCancelable(false);
        LinearLayout layout=new LinearLayout(Line.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(10,10,10,10);

        TextView status=new TextView(Line.this);
        status.setText("Status");
        final MaterialBetterSpinner STATUS=new MaterialBetterSpinner(Line.this);
        spinnerArrayAdapter2  = new ArrayAdapter<String>(Line.this, R.layout.dropdownadapter,getResources().getStringArray(R.array.LINESTATUS)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.dropdownadapter);
        STATUS.setText(_status);
        STATUS.setAdapter(spinnerArrayAdapter2);
        layout.addView(status);
        layout.addView(STATUS);
        TextView lbl_start_date=new TextView(this);
        lbl_start_date.setText("Date Start");
        TextView lbl_end_date=new TextView(this);
        lbl_end_date.setText("Date End");

        final   EditText txt_start=new EditText(this);

        txt_start.setFocusableInTouchMode(false);
        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Line.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                startformatdate = _year + "," + _month + "," + _day;
                                txt_start.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        txt_start.setText(_start_date);
        final   EditText txt_end=new EditText(this);
        txt_end.setFocusableInTouchMode(false);
        txt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Line.this,
                        new DatePickerDialog.OnDateSetListener() {



                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int _year=year;
                                int _month=(monthOfYear + 1);
                                int _day=dayOfMonth;
                                endformatdate = _year + "," + _month + "," + _day;
                                txt_end.setText(_year+"/"+_month+"/"+_day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        txt_start.setEnabled(false);
        txt_end.setText(_end_date);
        layout.addView(lbl_start_date);
        layout.addView(txt_start);
        layout.addView(lbl_end_date);
        layout.addView(txt_end);
        ////
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final    AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          try {
              if(!Config.ValidDate(txt_start.getText().toString(),txt_end.getText().toString())){

                  AlertWronInput(txt_end);

              }
          } catch (ParseException e) {
              e.printStackTrace();
          }
          if(STATUS.getText().toString().equals("")){
              AlertWronInput(STATUS);
          }
          if(counter==0){
              da=new DataBaseHandler(Line.this);
              da.ExecuteSql("update line set end_date='"+txt_end.getText().toString()+"',status='"+STATUS.getText().toString()+"' where _id ="+_id);
dialog.cancel();
              TastyToast.makeText(Line.this,"Successfully Edited",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
              LoadList();
          }
          counter=0;
      }
  });

    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadList();
    }
    private  void AlertWronInput(EditText txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Line.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
        counter++;
    }
    private  void AlertWronInput( String Message ){

        TastyToast.makeText(Line.this,  Message, Toast.LENGTH_SHORT,TastyToast.ERROR).show();
        counter++;
    }
    private  void AlertWronInput(MaterialBetterSpinner txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Line.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();

        counter++;
    }
}
