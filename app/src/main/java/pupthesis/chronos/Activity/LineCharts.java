package pupthesis.chronos.Activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Animation.SlideAnimationUtil;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Colors;
import pupthesis.chronos.Util.Config;


public class LineCharts extends AppCompatActivity implements  View.OnClickListener {
    private FloatingActionButton fab ;

    TextView txt_title,description;
    LinearLayout mainlayout;
    LinearLayout legendlayout,lineardays;
    RelativeLayout main;
    private Boolean isFabOpen = false;
    private String _ProjectID = "0";
    private String _RefProjectID = "0";
    private String _ProjectNAME = "N/A";
    List<String> items;
    String [] Names;
    String [][] Values;
    String [][] Dates;
    DataBaseHandler da;



    private ArrayList<String> NamesList;
    private boolean singlerowinitialize=false;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_charts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Config.islastpage=false;
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        _ProjectID = getIntent().getStringExtra("project_id");
        _ProjectNAME = getIntent().getStringExtra("project_name");
        _RefProjectID = getIntent().getStringExtra("ref_project_id");

        fab = (FloatingActionButton)findViewById(R.id.fab);



        fab.setOnClickListener(this);



        txt_title=(TextView)findViewById(R.id.txt_title);
        description=(TextView)findViewById(R.id.description);
        mainlayout=(LinearLayout)findViewById(R.id.mainlayout);
        lineardays=(LinearLayout)findViewById(R.id.lineardays);
        legendlayout=(LinearLayout)findViewById(R.id.legendlayout);
        main=(RelativeLayout)findViewById(R.id.main);
        LoadList();

    }

    private void LoadList() {

        LoadData();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (displayMetrics.heightPixels-150)/Names.length;
        int width = displayMetrics.widthPixels;


        for(int i=0;i<Names.length;i++){
            LinearLayout  Rows=new LinearLayout(LineCharts.this);
            Rows.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            Rows.setOrientation(LinearLayout.HORIZONTAL);
            Rows.setBackgroundColor(Colors.MaterialUIColors(i));

            Rows.setGravity(Gravity.CENTER);

            SlideAnimationUtil.slideInFromLeft(LineCharts.this,Rows);//animation kuno
            SlideAnimationUtil.slideInFromRight(LineCharts.this,legendlayout);//animation kuno
            TextView Divider=new TextView(LineCharts.this);
            Divider.setHeight(2);
            Divider.setBackgroundColor(Color.TRANSPARENT);
            int txtw=width/Values[i].length;
            mainlayout.addView(Rows);
            mainlayout.addView(Divider);
            for(int j=0;j<Values[i].length;j++){//columns

                ///Meter
                final TextView Text=new TextView(LineCharts.this);

                Text.setText(Values[i][j]);
                Text.setTextColor(Color.WHITE);

                ViewTreeObserver vto1 = Text.getViewTreeObserver();
                Text.setGravity(Gravity.CENTER|Gravity.TOP);
                vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int size=(Text.getWidth()+Text.getHeight())/24;
                        if(size>16){
                            size=16;
                        }
                        if(size<10){
                            size=10;
                        }
                        Text.setTextSize(size);

                        if(Text.getText().toString().equals("m.")){
                            Text.setText("N/A");

                            Text.setTextSize(12);
                        }

                    }
                });

                ///Days
                final TextView txtday=new TextView(LineCharts.this);
                txtday.setText(Dates[i][j]);
                txtday.setGravity(Gravity.CENTER);
                txtday.setTextColor(Color.WHITE);
                txtday.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ViewTreeObserver vto = txtday.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        int size=(txtday.getWidth()+txtday.getHeight())/28;
                        if(size>14){
                            size=14;
                        }
                        if(size<10){
                            size=10;
                        }
                        txtday.setTextSize(size);

                    }
                });


                ///DIVIDER
                TextView div=new TextView(LineCharts.this);
                div.setWidth(1);
                div.setHeight(height);
                div.setBackgroundColor(Color.WHITE);
///Dataholder
                LinearLayout data=new LinearLayout(LineCharts.this);
                data.setOrientation(LinearLayout.VERTICAL);
                data.setLayoutParams(new LinearLayout.LayoutParams(txtw,height));
                data.setBackgroundColor(Colors.MaterialUIColors(i));

                if(Text.getText().toString().equals("0") )
                {div.setWidth(1);
                    div.setBackgroundColor(Colors.MaterialUIColors(i));
                    data.setBackgroundColor(this.getResources().getColor(R.color.concrete));
                    Text.setTextColor(Color.LTGRAY);
                    txtday.setTextColor(Color.LTGRAY);


                }
                Text.setText(Text.getText().toString()+"m.");
                if(Text.getText().toString().equals("m.")){
                    div.setWidth(1);
                    div.setBackgroundColor(Colors.MaterialUIColors(i));
                    data.setBackgroundColor(this.getResources().getColor(R.color.fbutton_color_clouds));
                    Text.setTextColor(Color.LTGRAY);
                    txtday.setTextColor(Color.LTGRAY);
                    Text.setText("N/A");
                    txtday.setText("Unknown date.");
                }

                data.addView(txtday);
                data.addView(Text);

                Rows.addView(data);
                Rows.addView(div);




            }
        }

        ///setting the day label
        for(int d=0;d<Values[0].length;d++){
            TextView day=new TextView(LineCharts.this);
            day.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));

            day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            day.setTextColor(Color.BLACK);
            day.setText("Day "+(d+1));
            lineardays.addView(day);

        }


        //setting the legends of the blue sea tadaH!!!: funny no!
        txt_title.setText(_ProjectNAME);
        description.setText("description baby dapat mahaba");
        int counte=0;
        int realcounter=-1;
        int rows=Names.length/4;
        if((rows*4)-Names.length==0){
            rows=Names.length/4;
        }
       else if(Names.length<5)
        {
            rows=1;
        }
         else
             {
                int leftnumber=Names.length-counte;
                if(leftnumber!=0)
                {
                    rows=rows+1;
                }
             }
        LinearLayout []linearLayouts=new LinearLayout[rows];
        for(int c=0;c<Names.length;c++){
            TextView txt=new TextView(LineCharts.this);
            txt.setBackgroundColor(Colors.MaterialUIColors(c));
            txt.setText(Names[c]);
            txt.setTextSize(10);
            txt.setPadding(3,3,3,3);
            txt.setTextColor(Color.WHITE);
            TextView Divider=new TextView(LineCharts.this);
            Divider.setWidth(1);
            Divider.setBackgroundColor(Color.TRANSPARENT);

            if(!singlerowinitialize) {
                if(Names.length<5){

                        if (realcounter == -1) {
                            singlerowinitialize = true;
                            realcounter++;
                        }


                    linearLayouts[realcounter]=new LinearLayout(LineCharts.this);
                    linearLayouts[realcounter].setOrientation(LinearLayout.HORIZONTAL);
                    linearLayouts[realcounter].setGravity(Gravity.CENTER);
                }

                else if(counte==c){
                    counte=counte+4;

                    realcounter++;


                    try{
                        linearLayouts[realcounter]=new LinearLayout(LineCharts.this);
                        linearLayouts[realcounter].setOrientation(LinearLayout.HORIZONTAL);
                        linearLayouts[realcounter].setGravity(Gravity.CENTER);
                    }catch (Exception xx)
                    {

                        break;
                    }
                }
            }


            linearLayouts[realcounter].addView(txt);
            linearLayouts[realcounter].addView(Divider);
        }
        for(int i=0;i<linearLayouts.length;i++){
            legendlayout.addView(linearLayouts[i]);
        }
        singlerowinitialize=false;
    }


    private void LoadData() {
        da = new DataBaseHandler(LineCharts.this);
        Cursor cursor = da.getLIST("select distinct task_name from line_task where project_id=" + _ProjectID+" order by task_name");
        String n[]=new String[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                n[i]=cursor.getString(cursor.getColumnIndex("task_name"));
                i++;
            }
            while (cursor.moveToNext());
        }
        Names=n;
        LoadValues(Names);
        LoadDays(Names);
    }
    private void  LoadValues(String [] names){
        int col=0;
        String val[][]=null;
        for(int rowindex=0;rowindex<names.length;rowindex++){
            da = new DataBaseHandler(LineCharts.this);
            Cursor cursor = da.getLIST("select  measure from line_task where project_id=" + _ProjectID+" and task_name='"+names[rowindex]+"'");
            int columnindex=cursor.getColumnCount();
            try{int w=val[rowindex].length;}
            catch (Exception xx)
            {
                val=new String[names.length][50];
            }

            int i=0;
            if (cursor.moveToFirst()) {
                do {
                    val[rowindex][i]=cursor.getString(cursor.getColumnIndex("measure"));
                    i++;
                }
                while (cursor.moveToNext());
            }
            if(i>col){col=i;}
        }
        Values=new String[Names.length][col];
        for(int i=0;i<Names.length;i++){
            for(int j=0;j<col;j++){
                Values[i][j]=val[i][j];
            }
        }
    }
    private void  LoadDays(String [] names){
        int col=0;
        String val[][]=null;
        for(int rowindex=0;rowindex<names.length;rowindex++){
            da = new DataBaseHandler(LineCharts.this);
            Cursor cursor = da.getLIST("select  start_date from line_task where project_id=" + _ProjectID+" and     task_name='"+names[rowindex]+"'");
            int columnindex=cursor.getColumnCount();
            try{int w=val[rowindex].length;}
            catch (Exception xx)
            {
                val=new String[names.length][50];
            }


            int i=0;
            if (cursor.moveToFirst()) {
                do {
                    val[rowindex][i]=cursor.getString(cursor.getColumnIndex("start_date"));
                    i++;
                }
                while (cursor.moveToNext());
            }
            if(i>col){col=i;}


        }
        Dates=new String[Names.length][col];
        for(int i=0;i<Names.length;i++){
            for(int j=0;j<col;j++){
                Dates[i][j]=val[i][j];
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                SaveImage();
                break;



        }
    }



    private  void SaveImage(){


        OutputStream output;

        // Retrieve the image from the res folder
        main.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(main.getDrawingCache());
        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/CHRONOS/");
        dir.mkdirs();
        File file = new File(dir, _ProjectNAME+"-"+("0000" + _ProjectID).substring( _ProjectID.length())+".png");

        // Show a toast message on successful save
        TastyToast.makeText(getApplicationContext(), "Image Saved", TastyToast.LENGTH_SHORT,TastyToast.SUCCESS) ;
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        try{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }catch (Exception xx){

        }
        finish();
        super.onBackPressed();
    }


}
