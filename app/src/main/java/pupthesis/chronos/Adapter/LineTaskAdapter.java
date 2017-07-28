package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.R;

public class LineTaskAdapter extends ArrayAdapter<String> {
    TextView measureTV,projectnameTV,dateTV;
    CardView cardmeasure,cardnameoftask,carddate;
    int maxposition=0;
    Activity context;
    String[] taskname;String[] date; String[] measure;
    public LineTaskAdapter(Activity context, String[] taskname, String[] date, String[] measure) {
        super(context, R.layout.activity_line_task_adapter, taskname);
        this.context = context;
        this.taskname=taskname;
        this.date=date;
        this.measure=measure;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_line_task_adapter, null, true);

        measureTV=(TextView)listViewItem.findViewById(R.id.measureTV);
        projectnameTV=(TextView)listViewItem.findViewById(R.id.projectnameTV);
        dateTV=(TextView)listViewItem.findViewById(R.id.dateTV);

        measureTV.setText(measure[position]);
        projectnameTV.setText(taskname[position]);
        dateTV.setText(date[position]);


        if (position != maxposition) {
            AnimationSet set = new AnimationSet(true);

            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(400);
            set.addAnimation(animation);

            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            animation.setDuration(500);
            set.addAnimation(animation);

            listViewItem.startAnimation(set);
            maxposition = position;
        }

        return  listViewItem;
    }
}
