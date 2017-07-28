package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

import jxl.write.Label;
import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.LineTaskAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class LineTask extends BaseActivity {
    String _ProjectID="0";
    String _RefProjectID="0";
    String _ProjectNAME="N/A";
       int counter=0;
    DataBaseHandler da;
    ListView projectList;
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LineTask.this, "sfwef", Toast.LENGTH_SHORT).show();
               EntryEntry();
            }
        });
        LoadList();
    }
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
                    ContentValues cv=new ContentValues();
                    cv.put("task_id",_ProjectNAME+_ProjectID);
                    cv.put("task_name",TASKNAME.getText().toString());
                    cv.put("measure",MEASURE.getText().toString());
                    cv.put("start_date", Config.Date());
                    cv.put("project_id",_ProjectID);
                    da=new DataBaseHandler(LineTask.this);
                    if(da.createNewLINETASK(cv)){
                        TastyToast.makeText(LineTask.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        LoadList();
                    }
                }
            }
        });
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
}
