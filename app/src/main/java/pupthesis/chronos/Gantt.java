package pupthesis.chronos;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import pupthesis.chronos.Adapter.GantAdapter;

public class Gantt extends AppCompatActivity {
    boolean isLongPress=false;
    DataBaseHandler da;
    ListView ganttlist;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        Loadlist();
    }
    private void showAlert() {


        AlertDialog.Builder alert = new AlertDialog.Builder(Gantt.this);
        LinearLayout layout = new LinearLayout(Gantt.this);
        LinearLayout chkboxholder = new LinearLayout(Gantt.this);
        CardView cardview=new CardView(Gantt.this);
        cardview.setRadius(15);
        cardview.setCardElevation(5);
        alert.setTitle("CREATE NEW PROJECT");
        layout.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setGravity(Gravity.LEFT);
        layout.setPadding(10,10,10,10);

        TextView taskname=new TextView(Gantt.this);
        taskname.setPadding(0,10,0,0);
        taskname.setText("PROJECT NAME :");
        String ProjectName[];


        da=new DataBaseHandler(Gantt.this);
        Cursor cursor =da.getLIST("select * from projects");
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
            spinnerArrayAdapter  = new ArrayAdapter<String>(Gantt.this,R.layout.dropdownadapter,ProjectName); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(R.layout.dropdownadapter);
        }catch (Exception xx){
            TastyToast.makeText(Gantt.this, "No Project Found.", Toast.LENGTH_SHORT,TastyToast.WARNING);
            return;
        }



        final MaterialBetterSpinner PROJECTNAME=new MaterialBetterSpinner(Gantt.this);
        PROJECTNAME.setAdapter(spinnerArrayAdapter);

        TextView status=new TextView(Gantt.this);
        status.setPadding(0,5,0,0);
        status.setText("STATUS :");


        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(Gantt.this, R.layout.dropdownadapter,getResources().getStringArray( R.array.STATUS)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.dropdownadapter);

        final MaterialBetterSpinner STATUSNAME=new MaterialBetterSpinner(Gantt.this);
        STATUSNAME.setAdapter(spinnerArrayAdapter2);


        chkboxholder.addView(taskname);
        chkboxholder.addView(PROJECTNAME);
        chkboxholder.addView(status);
        chkboxholder.addView(STATUSNAME);

        TextView description=new TextView(Gantt.this);
        final EditText DESCRIPTION=new EditText(Gantt.this);
        description.setText("Descrition :");

        chkboxholder.addView(description);
        chkboxholder.addView(DESCRIPTION);


        layout.addView(chkboxholder);

        cardview.addView(layout);

        alert.setView(cardview);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                da=new DataBaseHandler(Gantt.this);
                ContentValues cv=new ContentValues();
                cv.put("project_name",PROJECTNAME.getText().toString());
                cv.put("status",STATUSNAME.getText().toString());
                cv.put("description",DESCRIPTION.getText().toString());
                da.createNewGANTT(cv);
                Loadlist();

                dialog.cancel();
            }
        });

        alert.show();
    }
    private  void Loadlist(){
        try{
            final String taskname[];
            final String description[];
            final String status[];
            final String _id[];
            da=new DataBaseHandler(Gantt.this);
            Cursor cursor= da.getLIST("select * from gantt order by _id desc");
            status=new String[cursor.getCount()];
            description=new String[cursor.getCount()];
            taskname=new String[cursor.getCount()];
            _id=new String[cursor.getCount()];
            try{
                int i=1;
                if(cursor!=null){
                    cursor.moveToFirst();
                    status[0]=cursor.getString(3);
                    taskname[0]=cursor.getString(1);
                    description[0]=cursor.getString(2);
                    _id[0]=cursor.getString(0);
                    while (cursor.moveToNext()){
                        status[i]=cursor.getString(3);
                        _id[i]=cursor.getString(0);
                        taskname[i]=cursor.getString(1);
                        description[i]=cursor.getString(2);
                        i++;
                    }

                }

                ganttlist =(ListView)findViewById(R.id.listView);
                GantAdapter adapter=new GantAdapter(this,taskname,status,description,null);
                ganttlist.setAdapter(adapter);}catch (Exception xx){}

            ganttlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isLongPress){

                        Intent startmainactivity = new Intent(Gantt.this, Gantt_Task.class);
                        startActivity(startmainactivity);

                    }
                }
            });

            ganttlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    isLongPress=true;
                    String idid =_id[position];
                    showAlert(taskname[position],description[position],status[position],_id[position]);

                    return isLongPress;
                }

            });}catch (Exception xx){}
    }
    private void showAlert(String _project_name,String _description,String _status,final String  ID) {

        AlertDialog.Builder alert = new AlertDialog.Builder(Gantt.this);

        LinearLayout layout = new LinearLayout(Gantt.this);
        LinearLayout chkboxholder = new LinearLayout(Gantt.this);
        CardView cardview=new CardView(Gantt.this);
        cardview.setRadius(15);
        alert.setCancelable(false);
        alert.setTitle("UPDATE PROJECT");
        layout.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setOrientation(LinearLayout.VERTICAL);
        chkboxholder.setGravity(Gravity.LEFT);
        layout.setPadding(10,10,10,10);
        TextView taskname=new TextView(Gantt.this);
        taskname.setPadding(0,10,0,0);
        taskname.setText("PROJECT NAME :");
        String ProjectName[];
        da=new DataBaseHandler(Gantt.this);
        Cursor cursor =da.getLIST("select * from projects");
        ProjectName=new String[cursor.getCount()];
        ArrayAdapter<String> spinnerArrayAdapter;

        int i = 1;
        if (cursor != null) {
            cursor.moveToFirst();
            ProjectName[0] = cursor.getString(1);
            while (cursor.moveToNext()) {
                ProjectName[i] = cursor.getString(1);
                i++;
            }
        }
        spinnerArrayAdapter  = new ArrayAdapter<String>(Gantt.this, android.R.layout.simple_spinner_item,ProjectName); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        final  MaterialBetterSpinner PROJECTNAME=new MaterialBetterSpinner(Gantt.this);
        PROJECTNAME.setAdapter(spinnerArrayAdapter);

        PROJECTNAME.setText(_project_name);

        TextView status=new TextView(Gantt.this);
        status.setPadding(0,5,0,0);
        status.setText("STATUS :");


        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(Gantt.this, android.R.layout.simple_spinner_item,getResources().getStringArray( R.array.STATUS)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final MaterialBetterSpinner STATUSNAME=new MaterialBetterSpinner(Gantt.this);
        STATUSNAME.setAdapter(spinnerArrayAdapter2);

        STATUSNAME.setText(_status);
        chkboxholder.addView(taskname);
        chkboxholder.addView(PROJECTNAME);
        chkboxholder.addView(status);
        chkboxholder.addView(STATUSNAME);

        TextView description=new TextView(Gantt.this);
        final EditText DESCRIPTION=new EditText(Gantt.this);
        description.setText("Descrition :");

        DESCRIPTION.setText(_description);
        chkboxholder.addView(description);
        chkboxholder.addView(DESCRIPTION);


        layout.addView(chkboxholder);
        cardview.addView(layout);
        alert.setView(cardview);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                da=new DataBaseHandler(Gantt.this);

                da.updateGANTT(DESCRIPTION.getText().toString(),PROJECTNAME.getText().toString(),STATUSNAME.getText().toString(),ID);
                Loadlist();

                isLongPress=false;
                dialog.cancel();

            }
        });
        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder deletealert = new AlertDialog.Builder(Gantt.this);
                deletealert.setMessage("Are you sure you want to delete "+PROJECTNAME.getText().toString());
                deletealert.setTitle("DELETE CONFIRMATION");
                deletealert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLongPress=false;
                        dialog.cancel();
                    }
                });
                deletealert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLongPress=false;
                        dialog.cancel();
                    }
                });
                deletealert.show();
            }
        });
        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLongPress=false;
                dialog.cancel();
            }
        });

        alert.show();
    }
}
