package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;

public class Project extends BaseActivity {
    ListView projectList;
    DataBaseHandler da;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        projectList=(ListView)findViewById(R.id.projectList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupEntry();
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        LoadList();
    }
    private  void PopupEntry(){

        AlertDialog.Builder alert=new AlertDialog.Builder(Project.this);
        LinearLayout linearLayout=new LinearLayout(Project.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView label=new TextView(Project.this);
        final EditText txt_project=new EditText(Project.this);
        txt_project.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        linearLayout.addView(label);
        linearLayout.addView(txt_project);
        alert.setView(linearLayout);
        alert.setTitle("NEW PROJECT");
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                da=new DataBaseHandler(Project.this);
                ContentValues contentValues=new ContentValues();
                contentValues.put("project_name",txt_project.getText().toString());
                da.createNewPROJECT(contentValues);
                TastyToast.makeText(Project.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                LoadList();
            }
        });
        alert.show();
    }
    private  void LoadList()
    {

        try{

            da=new DataBaseHandler(Project.this);
            Cursor cursor =da.getLIST("select * from projects");

            final String ProjectName[]=new String[cursor.getCount()];
            final String ID[]=new String[cursor.getCount()];
            int i=1;
            if(cursor!=null){
                cursor.moveToFirst();
                try{
                    ProjectName[0]=cursor.getString(1);
                    ID[0]=cursor.getString(0);}catch (Exception xx){}
                while (cursor.moveToNext()){
                    ProjectName[i]=cursor.getString(1);
                    ID[i]=cursor.getString(0);
                    i++;
                }
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(Project.this,
                    R.layout.dropdownadapter, ProjectName);
            projectList.setAdapter(adapter);
            projectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final  String DELETEID=ID[position];
                    AlertDialog.Builder deletealert=new AlertDialog.Builder(Project.this);
                    deletealert.setTitle("Delete Confirmation");
                    deletealert.setMessage("Are you sure you want to delete '"+ProjectName[position]+"'");
                    deletealert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            da.DeleteProject(DELETEID);
                            TastyToast.makeText(Project.this,"Successfully Deleted",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
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
        }

    }
}
