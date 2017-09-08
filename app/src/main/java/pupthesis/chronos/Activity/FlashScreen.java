package pupthesis.chronos.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.sdsmdg.tastytoast.TastyToast;

import io.saeid.fabloading.LoadingView;
import pupthesis.chronos.Animation.BaseActivity;
import pupthesis.chronos.R;

public class FlashScreen extends BaseActivity {
    LoadingView mLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        TastyToast.makeText(getApplicationContext(), "Welcome to CHRONOS", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);

        mLoadingView.addAnimation(R.color.red,R.drawable.labor_man,LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(R.color.fbutton_color_midnight_blue,R.drawable.building,LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(R.color.fbutton_color_concrete,R.drawable.road,LoadingView.FROM_BOTTOM);
        mLoadingView.addAnimation(R.color.green_sea,R.drawable.graphmo,LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(R.color.AppbarColor,R.drawable.circlelogo,LoadingView.FROM_BOTTOM);

        mLoadingView.startAnimation();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent startmainactivity = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(startmainactivity);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
