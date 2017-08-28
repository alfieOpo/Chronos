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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Colors;
import pupthesis.chronos.Util.MyAxisValueFormatter;
import pupthesis.chronos.Util.MyValueFormatter;


public class LineCharts extends AppCompatActivity implements  View.OnClickListener, OnChartValueSelectedListener {
    private FloatingActionButton fab,fab_toimage,fab_refresh;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private WebView webView;
    private Boolean isFabOpen = false;
    private String _ProjectID = "0";
    private String _RefProjectID = "0";
    private String _ProjectNAME = "N/A";
    List<String> items;
    String MASTERDATA="";
    DataBaseHandler da;

    Float data[][];
    private HorizontalBarChart   mChart;
    private ArrayList<String> NamesList;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_charts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        _ProjectID = getIntent().getStringExtra("project_id");
        _ProjectNAME = getIntent().getStringExtra("project_name");
        _RefProjectID = getIntent().getStringExtra("ref_project_id");

         LoadItem();//load all the names
        LoadName();
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab_refresh = (FloatingActionButton)findViewById(R.id.fab_refresh);
        fab_toimage = (FloatingActionButton)findViewById(R.id.fab_toimage);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab_refresh.setOnClickListener(this);
        fab_toimage.setOnClickListener(this);



        mChart = (HorizontalBarChart) findViewById(R.id.mChart);
        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();

        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

        // setting data


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(0f);
        l.setXEntrySpace(6f);

        LoadList();
    }
    private void LoadList() {
        String NAMES[]=null;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for(int i=0;i<items.size();i++){

            String sql="select measure,task_name from line_task where project_id="+_ProjectID+" and start_date='"+items.get(i)+"' order by task_name";
            da=new DataBaseHandler(LineCharts.this);
            Cursor cursor=da.getLIST(sql);
            float val[]=new float[cursor.getCount()];
              NAMES=new String[cursor.getCount()];
            int a=0;
            if(cursor.moveToFirst()){

                do {
                if(NAMES.length!=cursor.getCount()){
                        NAMES[a]=cursor.getString(cursor.getColumnIndex("task_name"));
                    }

                    val[a]=Float.valueOf(cursor.getString(cursor.getColumnIndex("measure")));
                    a++;
                }
                while (cursor.moveToNext());




        }
            yVals1.add(new BarEntry(i, val,items.get(i)));
    }


 /*
        da=new DataBaseHandler(LineCharts.this);
        String sql="select "+getSqlSelect()+" from line_task where project_id=" + _ProjectID+" group by start_date";
        String []Dates=getDate("select "+getSqlSelect()+",start_date from line_task where project_id=" + _ProjectID+" group by start_date");
        Cursor cursor=da.getLIST(sql);
        data =new Float[cursor.getCount()][cursor.getColumnCount()];
        int i=0;
        if(cursor.moveToFirst()){
            do {
                float val[]=new float[cursor.getColumnCount()];
                for(int j =0; j<cursor.getColumnCount(); j++){
                    data[i][j]=Float.valueOf(cursor.getString(j));
                    val[j]=Float.valueOf(cursor.getString(j));
                }

                yVals1.add(new BarEntry(i, val,items.get(i)));
                i++;
            }while (cursor.moveToNext());
        }
*/


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, _ProjectNAME);
            set1.setDrawIcons(false);
            set1.setColors(Colors.getColors(da.countoftask(_RefProjectID)));
                String n[]=new String[NamesList.size()];
             for(int i=0;i<NamesList.size();i++){
                 n[i]=NamesList.get(i);

             }
            set1.setStackLabels(n);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        //mChart.setExtraBottomOffset(5f);
        mChart.setExtraOffsets(10, 10, 10, 10);
        mChart.setFitBars(true);
        mChart.setDragEnabled(true);
        mChart.getLegend().setWordWrapEnabled(true);
        mChart.invalidate();
    }


    private void LoadItem() {

        items = new ArrayList<String>();

        da = new DataBaseHandler(LineCharts.this);
        Cursor cursor = da.getLIST("select distinct start_date from line_task where project_id=" + _ProjectID+" order  by _id desc");
        if (cursor.moveToFirst()) {
            do {
                items.add(cursor.getString(cursor.getColumnIndex("start_date")));

            }
            while (cursor.moveToNext());
        }
        //getSqlSelect();
    } private void LoadName() {

        NamesList = new ArrayList<String>();

        da = new DataBaseHandler(LineCharts.this);
        Cursor cursor = da.getLIST("select distinct task_name from line_task where project_id=" + _ProjectID+" ");
        if (cursor.moveToFirst()) {
            do {
                NamesList.add(cursor.getString(cursor.getColumnIndex("task_name")));

            }
            while (cursor.moveToNext());
        }
        //getSqlSelect();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab_refresh:
                LoadList();
                animateFAB();

                break;
            case R.id.fab_toimage:
                SaveImage();
                animateFAB();
                break;

        }
    }



    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab_refresh.startAnimation(fab_close);
            fab_toimage.startAnimation(fab_close);
            fab_refresh.setClickable(false);
            fab_toimage.setClickable(false);
            isFabOpen = false;
            Log.d("Alfie", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab_refresh.startAnimation(fab_open);
            fab_toimage.startAnimation(fab_open);
            fab_refresh.setClickable(true);
            fab_toimage.setClickable(true);
            isFabOpen = true;
            Log.d("Alfie","open");
        }
    }

    private  void SaveImage(){


        OutputStream output;

        // Retrieve the image from the res folder
        mChart.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(mChart.getDrawingCache());
        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath()
                + "/CHRONOS/");
        dir.mkdirs();
        File file = new File(dir, _ProjectNAME+"-"+("0000" + _ProjectID).substring( _ProjectID.length())+".png");

        // Show a toast message on successful save
        TastyToast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT,TastyToast.SUCCESS) ;
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
    protected void onStop() {

        try{
            webView.stopLoading();
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onStop();
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
