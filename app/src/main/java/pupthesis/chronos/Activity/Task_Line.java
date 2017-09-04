package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class Task_Line extends BaseActivity {

    DataBaseHandler da;
    ListView projectList;
    String ProjectID="0";
    String ProjectNAME="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task__line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProjectID = getIntent().getStringExtra("project_id");
        ProjectNAME=getIntent().getStringExtra("project_name");
        TextView project_nameTV=(TextView)findViewById(R.id.project_nameTV);
        project_nameTV.setText("LIST OF \""+ProjectNAME+"\" ITEM REFERENCES");
        projectList=(ListView)findViewById(R.id.projectList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                PopupEntry();
            }
        });
        Config.islastpage=false;
        LoadList();
    }

    private  void PopupEntry(){

        AlertDialog.Builder alert=new AlertDialog.Builder(Task_Line.this);
        LinearLayout linearLayout=new LinearLayout(Task_Line.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView label=new TextView(Task_Line.this);
        final AutoCompleteTextView txt_project=new AutoCompleteTextView (Task_Line.this);

        ///
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  getResources().getStringArray( R.array.TASK));

        ///
        txt_project.setAdapter(adapter);
        linearLayout.addView(label);
        linearLayout.addView(txt_project);
        alert.setView(linearLayout);
        alert.setTitle("NEW ACTIVITY");
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txt_project.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"No Project Saved.",TastyToast.LENGTH_SHORT,TastyToast.CONFUSING);
                }
                else{
                    da=new DataBaseHandler(Task_Line.this);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("task_name",txt_project.getText().toString());
                    contentValues.put("project_id",ProjectID);


                    if( da.createNewTASKLINE(contentValues)){
                        TastyToast.makeText(getApplicationContext(),"Successfully Saved.",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        LoadList();}
                }

            }
        });
        alert.show();
    }
    private  void LoadList()
    {

        try{

            da=new DataBaseHandler(Task_Line.this);
            Cursor cursor =da.getLIST("select * from tasksline where project_id="+ProjectID);
            final String ProjectName[]=new String[cursor.getCount()];
            final String ID[]=new String[cursor.getCount()];
            int i=1;
            if(cursor!=null){
                cursor.moveToFirst();try{
                    ProjectName[0]=cursor.getString(1);
                    ID[0]=cursor.getString(0);}catch (Exception xx){}
                while (cursor.moveToNext()){
                    ProjectName[i]=cursor.getString(1);
                    ID[i]=cursor.getString(0);
                    i++;
                }
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(Task_Line.this,
                    R.layout.dropdownadapter, ProjectName);
            projectList.setAdapter(adapter);
            projectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final  String DELETEID=ID[position];
                    AlertDialog.Builder deletealert=new AlertDialog.Builder(Task_Line.this);
                    deletealert.setTitle("Delete Confirmation");
                    deletealert.setMessage("Are you sure you want to delete '"+ProjectName[position]+"'");
                    deletealert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            da.ExecuteSql("delete from tasksline where _id ="+DELETEID);
                            TastyToast.makeText(getApplicationContext(),"Successfully Deleted",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            LoadList();
                        }
                    });
                    deletealert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    deletealert.show();
                    return false;
                }
            });
        }catch (Exception xx){
            projectList.setAdapter(null);
            return;
        }

    }
}
