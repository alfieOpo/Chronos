package pupthesis.chronos.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Adapter.GantAdapter;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class Gantt extends BaseActivity {
    boolean isLongPress=false;
    DataBaseHandler da;
    ListView ganttlist;
    FloatingActionButton fab;

    int counter=0;
    GantAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();

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
        final String ref_ProjectID[];

        da=new DataBaseHandler(Gantt.this);
        Cursor cursor =da.getLIST("select * from projects ");
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
            spinnerArrayAdapter  = new ArrayAdapter<String>(Gantt.this,R.layout.dropdownadapter,ProjectName);
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
        description.setText("Description :");

        chkboxholder.addView(description);
        chkboxholder.addView(DESCRIPTION);


        layout.addView(chkboxholder);

        cardview.addView(layout);

        alert.setView(cardview);
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

                if(DESCRIPTION.getText().toString().equals("")){
                    AlertWronInput(DESCRIPTION);

                }
                if(STATUSNAME.getText().toString().equals("")){
                    AlertWronInput(STATUSNAME);

                }
                if(PROJECTNAME.getText().toString().equals("")){
                    AlertWronInput(PROJECTNAME);
                }
                if(counter==0){
                    da=new DataBaseHandler(Gantt.this);
                    ContentValues cv=new ContentValues();
                    cv.put("project_name",PROJECTNAME.getText().toString());
                    cv.put("status",STATUSNAME.getText().toString());
                    cv.put("description",DESCRIPTION.getText().toString());
                    int position=spinnerArrayAdapter.getPosition(PROJECTNAME.getText().toString());
                    cv.put("ref_project_id",ref_ProjectID[position]);
                    if(da.createNewGANTT(cv)){
                        TastyToast.makeText(Gantt.this,"Successfully Saved",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        Loadlist();
                    }
                    dialog.cancel();
                }
                counter=0;
            }
        });
    }
    private  void Loadlist(){

        Config.islastpage=false;
        da=new DataBaseHandler(getApplicationContext());
        if(da.getCountProjects().equals("0")){
            AlertDialog.Builder createproject=new AlertDialog.Builder(Gantt.this);

            createproject.setMessage("Create new project reference?");
            createproject.setTitle("No project reference found!");
            createproject.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent startmainactivity = new Intent(getApplicationContext(), Project.class);
                    startActivity(startmainactivity);
                    dialog.dismiss();
                }
            });
            createproject.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent startmainactivity = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(startmainactivity);
                    finish();
                    dialog.dismiss();
                }
            });
            createproject.show();
        }
        else if(da.getCountGantt().equals("0")){
            AlertDialog.Builder creategannt=new AlertDialog.Builder(Gantt.this);

            creategannt  .setMessage("Create Project?");
            creategannt   .setTitle("No project found yet");
            creategannt  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showAlert();

                }
            });
            creategannt   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent startmainactivity = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(startmainactivity);
                    finish();

                }
            });
            creategannt.show();
        }
        /////

        ganttlist =(ListView)findViewById(R.id.listView);
        ganttlist.setAdapter(null);
        try{
            final String taskname[];
            final String description[];
            final String status[];
            final String ref_project_id[];
            final String _id[];
            da=new DataBaseHandler(Gantt.this);
            String sql="select * from gantt order by _id desc";
            if(da.gantttaskcount()>0){
                sql="select distinct x.* from (select cur._id ,cur.project_name,cur.[description],cur.[status],cur.ref_project_id,cur.start_date\n" +
                        "from (select g._id,cast(replace(gt.start_date,',','/') as date) as start_date,g.project_name,g.[description],g.[status],g.ref_project_id from gantt g left join  gant_task gt on g._id =gt.project_id) cur\n" +
                        "where not exists (\n" +
                        "    select * \n" +
                        "    from (select g._id,cast(replace(gt.start_date,',','/') as date) as start_date,g.project_name,g.[description],g.[status],g.ref_project_id from gantt g left join  gant_task gt on g._id =gt.project_id) high \n" +
                        "    where high._id = cur._id \n" +
                        "    and high.start_date > cur.start_date\n" +
                        ") )x\n" +
                        "order by start_date desc\n";
            }
            Cursor cursor= da.getLIST(sql);
            status=new String[cursor.getCount()];
            description=new String[cursor.getCount()];
            taskname=new String[cursor.getCount()];
            ref_project_id=new String[cursor.getCount()];
            _id=new String[cursor.getCount()];
            try{
                int i=0;
                if (cursor.moveToFirst()) {
                    do {
                        status[i]=cursor.getString(cursor.getColumnIndex("status"));
                        _id[i]=cursor.getString(cursor.getColumnIndex("_id"));
                        taskname[i]=cursor.getString(cursor.getColumnIndex("project_name"));
                        description[i]=cursor.getString(cursor.getColumnIndex("description"));
                        ref_project_id[i]=cursor.getString(cursor.getColumnIndex("ref_project_id"));
                        i = cursor.getPosition() + 1;

                    } while (cursor.moveToNext());
                }

                adapter=new GantAdapter(this,taskname,status,description,_id);

                ganttlist.setAdapter(adapter);}catch (Exception xx){
              xx.printStackTrace();
            }

            ganttlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!isLongPress){

                        Intent startmainactivity = new Intent(Gantt.this, Gantt_Task.class);
                        startmainactivity.putExtra("project_id", _id[position]);
                        startmainactivity.putExtra("project_name", taskname[position].toUpperCase());
                        startmainactivity.putExtra("ref_project_id", ref_project_id[position]);
                        startmainactivity.putExtra("status", status[position]);
                        startmainactivity.putExtra("description", description[position]);
                        startActivity(startmainactivity);



                    }

                }
            });

            ganttlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    isLongPress=true;

                    showAlert(taskname[position],description[position],status[position],_id[position]);

                    return isLongPress;
                }

            });}catch (Exception xx){

        }
        try{
            ganttlist.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        fab.hide();
                    } else {

                        fab.show();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });}catch (Exception xx){


        }
        if(ganttlist.getAdapter()==null){

        }
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
        final String ref_ProjectID[];
        da=new DataBaseHandler(Gantt.this);
        Cursor cursor =da.getLIST("select * from projects");
        ProjectName=new String[cursor.getCount()];
        ref_ProjectID=new String[cursor.getCount()];
        ArrayAdapter<String> spinnerArrayAdapter;

        int i = 1;
        if (cursor != null) {
            cursor.moveToFirst();
            ProjectName[0] = cursor.getString(1);
            while (cursor.moveToNext()) {
                ProjectName[i] = cursor.getString(1);
                ref_ProjectID[i] = cursor.getString(0);
                i++;
            }
        }
        spinnerArrayAdapter  = new ArrayAdapter<String>(Gantt.this, R.layout.dropdownadapter,ProjectName); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource( R.layout.dropdownadapter);


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
        description.setText("Description :");

        DESCRIPTION.setText(_description);
        chkboxholder.addView(description);
        chkboxholder.addView(DESCRIPTION);


        layout.addView(chkboxholder);
        cardview.addView(layout);
        alert.setView(cardview);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {




            }
        });
        alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final AlertDialog.Builder deletealert = new AlertDialog.Builder(Gantt.this);
                deletealert.setMessage("Are you sure you want to delete "+PROJECTNAME.getText().toString());
                deletealert.setTitle("DELETE CONFIRMATION");

                deletealert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        da=new DataBaseHandler(getApplicationContext());
                        da.ExecuteSql("delete from  gantt where _id="+ID);
                        da.ExecuteSql("delete from  gant_task where project_id="+ID);


                        Loadlist();
                        TastyToast.makeText(getApplicationContext(),"Successfully Deleted.",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        isLongPress=false;
                        dialog.cancel();
                    }
                });
                deletealert.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        final    AlertDialog dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDown;
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(DESCRIPTION.getText().toString().equals("")){
                    AlertWronInput(PROJECTNAME);

                }
                if(STATUSNAME.getText().toString().equals("")){
                    AlertWronInput(STATUSNAME);

                }
                if(PROJECTNAME.getText().toString().equals("")){
                    AlertWronInput(PROJECTNAME);

                }
                if(counter==0){
                    da=new DataBaseHandler(Gantt.this);

                    da.updateGANTT(DESCRIPTION.getText().toString(),PROJECTNAME.getText().toString(),STATUSNAME.getText().toString(),ID);
                    Loadlist();

                    dialog.dismiss();
                }

                isLongPress=false;
                counter=0;
            }
        });

    }
    private  void AlertWronInput(MaterialBetterSpinner txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Gantt.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
        counter++;
    }
    private  void AlertWronInput(EditText txt ){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        txt .startAnimation(shake);
        TastyToast.makeText(Gantt.this,  "Fill-out important data", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
        counter++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Loadlist();
    }
}
