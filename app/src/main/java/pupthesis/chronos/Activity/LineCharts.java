package pupthesis.chronos.Activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.github.mikephil.charting.utils.ColorTemplate;
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

    private HorizontalBarChart   mChart;
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

        //LoadItem();//load all the names
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
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                "def", "ghj", "ikl", "mno" });
        LoadList();
    }

    private void LoadItem() {
        items = new ArrayList<String>();
        da = new DataBaseHandler(LineCharts.this);
        Cursor cursor = da.getLIST("select distinct start_date from line_task where project_id=" + _ProjectID);
        if (cursor.moveToFirst()) {
            do {
                items.add(cursor.getString(cursor.getColumnIndex("start_date")));
            }
            while (cursor.moveToNext());
        }
        getSqlSelect();
    }
    private  void  getSqlSelect(){
        List<String> query=new ArrayList<String>();
        for(String item:items){
            query.add("sum(CASE start_date WHEN '"+item+"' THEN measure ELSE 0 END) AS '"+item+"'");
        }
        String data=TextUtils.join(",",query);
        SelectData(data);

    }
    private  void SelectData(String _data){
        da=new DataBaseHandler(LineCharts.this);
        String sql="select task_name,"+_data+" from line_task where project_id=" + _ProjectID+" group by task_name";
        Cursor cursor=da.getLIST(sql);
        String data[][]=new String[cursor.getCount()][cursor.getColumnCount()];
        int i=0;
        if(cursor.moveToFirst()){
            do {
                for(int j =0; j<cursor.getColumnCount(); j++){
                    data[i][j]=cursor.getString(j);
                }
                i++;
            }while (cursor.moveToNext());
        }
        setMainData(data,cursor.getCount(),cursor.getColumnCount());
    }
    private  void setMainData(String[][] _data,int rowindex,int columnindex){
        List<String> data=new ArrayList<String>();
        List<String> LabelList=new ArrayList<String>();
        for(int c=0;c<items.size();c++){
            LabelList.add("'"+items.get(c)+"'");
        }
        String label="['Item',"+TextUtils.join(",",LabelList)+"]";
        data.add(label);
        for(int i=0;i<rowindex;i++){
            String v="";
            String v2="";
            for(int j=0;j<columnindex;j++){
                String x=_data[i][j];
                if(j==0){
                    x= "'"+_data[i][j]+"'";
                }
                v+=x+",";
            }
            v2="["+v+"]";
            data.add(v2.replace(",]","]"));
        }
        MASTERDATA=TextUtils.join(",",data);


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

    private void LoadList() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        float val1 =5f ;
        float val2 =100f  ;
        float val3 = 50f ;
        float val4 = 20f ;
        float val31 =5f ;
        float val32 =2f  ;
        float val33 = 5f ;
        float val34 = 3f ;
        yVals1.add(new BarEntry(0, new float[]{val1, val2, val3,val4},"2017/11/21"));
        yVals1.add(new BarEntry(1, new float[]{val31, val32, val33,val34},"description 2"));


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Statistics Vienna 2014");
            set1.setDrawIcons(false);
            set1.setColors(Colors.getColors());
            set1.setStackLabels(new String[]{"Sunday", "Monday", "Tuesday","Wed"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
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
    public static Bitmap Image(WebView webView) {
        webView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        webView.draw(canvas);
        return bitmap;
    }

    private  void SaveImage(){

        Bitmap bitmap;
        OutputStream output;

        // Retrieve the image from the res folder
        bitmap =  Image(webView);

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
