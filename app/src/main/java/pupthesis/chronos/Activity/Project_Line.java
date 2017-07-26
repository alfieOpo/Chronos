package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.ProjectAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;

public class Project_Line extends BaseActivity {

    DataBaseHandler da;
    ListView projectList;
    boolean isLongPress=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        projectList=(ListView)findViewById(R.id.projectList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupEntry();
            }
        });
        LoadList();
    }
    private  void PopupEntry(){

        AlertDialog.Builder alert=new AlertDialog.Builder(Project_Line.this);
        LinearLayout linearLayout=new LinearLayout(Project_Line.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView label=new TextView(Project_Line.this);
        final EditText txt_project=new EditText(Project_Line.this);
        txt_project.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        linearLayout.addView(label);
        linearLayout.addView(txt_project);
        alert.setView(linearLayout);
        alert.setTitle("NEW PROJECT");
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(txt_project.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"No Project Saved.",TastyToast.LENGTH_SHORT,TastyToast.CONFUSING);
                }
                else{
                    da=new DataBaseHandler(Project_Line.this);
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("project_name",txt_project.getText().toString());

                    if( da.createNewPROJECTLINE(contentValues)){
                        TastyToast.makeText(Project_Line.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        LoadList();
                    }

                }

            }
        });
        alert.show();
    }

    private  void LoadList()
    {
        projectList.setAdapter(null);
        try{
            da=new DataBaseHandler(Project_Line.this);
            Cursor cursor =da.getLIST("select * from projectsline");
            final String ProjectName[]=new String[cursor.getCount()];
            final String ID[]=new String[cursor.getCount()];
            int i=0;
            if (cursor.moveToFirst()) {
                do {

                    ProjectName[i]=cursor.getString(cursor.getColumnIndex("project_name"));
                    ID[i]=cursor.getString(cursor.getColumnIndex("_id"));

                    i = cursor.getPosition() + 1;

                } while (cursor.moveToNext());
            }
            ProjectAdapter adapter = new ProjectAdapter(Project_Line.this,ProjectName,ID,false);
            projectList.setAdapter(adapter);
            projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isLongPress){
                        Intent intent = new Intent(Project_Line.this, Task_Line.class);
                        intent.putExtra("project_id", ID[position]);
                        intent.putExtra("project_name", ProjectName[position].toUpperCase());
                        startActivity(intent);
                    }
                }
            });
            projectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    isLongPress=true;
                    final  String DELETEID=ID[position];
                    AlertDialog.Builder deletealert=new AlertDialog.Builder(Project_Line.this);
                    deletealert.setTitle("Delete Confirmation");
                    deletealert.setMessage("Are you sure you want to delete '"+ProjectName[position]+"'");
                    deletealert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            da.ExecuteSql("delete from projectsline where _id="+DELETEID);
                            TastyToast.makeText(Project_Line.this,"Successfully Deleted",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                            LoadList();
                            isLongPress=false;
                        }
                    });
                    deletealert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            isLongPress=false;
                        }
                    });
                    deletealert.show();
                    return isLongPress;
                }
            });
        }catch (Exception xx){
            projectList.setAdapter(null);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        LoadList();
    }
}
