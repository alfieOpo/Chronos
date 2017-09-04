package pupthesis.chronos.Activity;

import android.os.Bundle;

import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Config.islastpage=true;
    }
    @Override
    public void onResume() {
        Config.islastpage=true;
        super.onResume();
    }
}
