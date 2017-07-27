package pupthesis.chronos.Activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.Util.Config;
import pupthesis.chronos.R;

public class Charts extends AppCompatActivity implements  View.OnClickListener{
    private WebView webView;
    private FloatingActionButton fab,fab_toimage,fab_refresh;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    private String _ProjectID="0";
    private String _RefProjectID="0";
    private String _ProjectNAME="N/A";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        _ProjectID = getIntent().getStringExtra("project_id");
        _ProjectNAME=getIntent().getStringExtra("project_name");
        _RefProjectID=getIntent().getStringExtra("ref_project_id");
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

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        LoadList();
    }
    private  void LoadList(){
        String customHtml = "<html>\n" +
                "<head>\n" +
                "  <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "  <script type=\"text/javascript\">\n" +
                "    google.charts.load('current', {'packages':['gantt']});\n" +
                "    google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "    function daysToMilliseconds(days) {\n" +
                "      return days * 24 * 60 * 60 * 1000;\n" +
                "    }\n" +
                "\n" +
                "    function drawChart() {\n" +
                "\n" +
                "      var data = new google.visualization.DataTable();\n" +
                "      data.addColumn('string', 'Task ID');\n" +
                "      data.addColumn('string', 'Task Name');\n" +
                "      data.addColumn('date', 'Start Date');\n" +
                "      data.addColumn('date', 'End Date');\n" +
                "      data.addColumn('number', 'Duration');\n" +
                "      data.addColumn('number', 'Percent Complete');\n" +
                "      data.addColumn('string', 'Dependencies');\n" +
                "\n" +
                "      data.addRows([ "+DataTable()+"]);\n" +
                "\n" +
                "      var options = {\n" +
                "        height: 600\n" +
                "      };\n" +
                "\n" +
                "      var chart = new google.visualization.Gantt(document.getElementById('chart_div'));\n" +
                "\n" +
                "      chart.draw(data, options);\n" +
                "    }\n" +
                "  </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"chart_div\"></div>\n" +
                "</body>\n" +
                "</html>\n";

        // webView.loadUrl(customHtml);
        webView.loadData(customHtml, "text/html", "UTF-8");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {

                TastyToast.makeText(getApplicationContext(), "Please Wait", Toast.LENGTH_SHORT,TastyToast.INFO) ;
                //   progressDialog = ProgressDialog.show( getApplicationContext(), "Please wait ", "Loading...", true);
                // TODO show you progress image
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                TastyToast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT,TastyToast.SUCCESS) ;


            }
        });

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

    private  String DataTable(){
        List<String> list = new ArrayList<String>();
        DataBaseHandler da=new DataBaseHandler(getApplicationContext());

        String sql="select * from gant_task where project_id="+ _ProjectID+" order by _id desc";
        Cursor cursor= da.getLIST(sql);
        final String taskname[];
        final String task_id[];
        final String endtime[];
        final String starttime[];
        final String _percentcompelete[];
        final String _id[];
        endtime=new String[cursor.getCount()];
        task_id=new String[cursor.getCount()];
        starttime=new String[cursor.getCount()];
        _percentcompelete=new String[cursor.getCount()];
        taskname=new String[cursor.getCount()];
        _id=new String[cursor.getCount()];
        int i=1;
        if(cursor!=null){
            cursor.moveToFirst();
            try{
                endtime[0]=cursor.getString(4);
                task_id[0]=cursor.getString(1);
                taskname[0]=cursor.getString(2);
                starttime[0]=cursor.getString(5);
                _percentcompelete[0]=cursor.getString(3);
                _id[0]=cursor.getString(0);}catch (Exception xx){}
            while (cursor.moveToNext()){
                endtime[i]=cursor.getString(4);
                taskname[i]=cursor.getString(2);
                starttime[i]=cursor.getString(5);
                task_id[i]=cursor.getString(1);
                _percentcompelete[i]=cursor.getString(3);

                i++;
            }
            for(int c=0;c<cursor.getCount();c++){

                String Data="['"+task_id[c]+"','"+taskname[c]+"',new Date("+starttime[c]+") "+",new Date("+ endtime[c]+"),null, "+_percentcompelete[c]+",null]";
                list.add(Data);

            }
        }
        return TextUtils.join(",\n",list);
        // ['task_id', 'task_name,', new Date(2015, 0, 1)--start_date, new Date(2015, 0, 5)--enddate, null,  percent,  null]
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
}
