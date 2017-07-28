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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.LineAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;

public class Line extends BaseActivity {
    DataBaseHandler da;
    boolean islongpress=false;
    ListView linelist;
    ArrayAdapter<String>   spinnerArrayAdapter2;
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
        final String ID[]=new String[cursor.getCount()];
        final String ref_project_id[]=new String[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()) {
            do {

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
                    startActivity(startmainactivity);
                }
            }
        });
        linelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                islongpress=true;

                return islongpress;
            }
        });
    }
    private  void showAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Line.this);
        builder.setTitle("New Line");
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
        ////
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues cv=new ContentValues();
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
        final    AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadList();
    }
}
