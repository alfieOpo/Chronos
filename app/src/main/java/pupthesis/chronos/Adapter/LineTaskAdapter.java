package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import pupthesis.chronos.R;

public class LineTaskAdapter extends ArrayAdapter<String> {
    TextView measureTV,projectnameTV,dateTV;
    CardView cardmeasure,cardComplete,carddate;
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
        cardComplete=(CardView)listViewItem.findViewById(R.id.cardmeasure);
        carddate=(CardView)listViewItem.findViewById(R.id. carddate);
        int complete=Integer.parseInt(measure[position].replace("m.","").replace(" ",""));
        if(complete<=10){
            cardComplete.setCardBackgroundColor(Color.rgb(116, 98, 156));
            carddate.setCardBackgroundColor(Color.rgb(116, 98, 156));
        }
        else if(complete<=20){
            cardComplete.setCardBackgroundColor(Color.rgb(106, 108, 156));
            carddate.setCardBackgroundColor(Color.rgb(106, 108, 156));
        }
        else if(complete<=30){
            cardComplete.setCardBackgroundColor(Color.rgb(96, 118, 156));
            carddate.setCardBackgroundColor(Color.rgb(96, 118, 156));
        }
        else if(complete<=40){
            cardComplete.setCardBackgroundColor(Color.rgb(86, 128, 156));
            carddate.setCardBackgroundColor(Color.rgb(86, 128, 156));
        }
        else if(complete<=50){
            cardComplete.setCardBackgroundColor(Color.rgb(76, 138, 156));
            carddate.setCardBackgroundColor(Color.rgb(76, 138, 156));
        }
        else if(complete<=60){
            cardComplete.setCardBackgroundColor(Color.rgb(66, 148, 156));
            carddate.setCardBackgroundColor(Color.rgb(66, 148, 156));
        }
        else if(complete<=70){
            cardComplete.setCardBackgroundColor(Color.rgb(56, 158, 156));
            carddate.setCardBackgroundColor(Color.rgb(56, 158, 156));
        }
        else if(complete<=80){

            cardComplete.setCardBackgroundColor(Color.rgb(46, 168, 156));
            carddate.setCardBackgroundColor(Color.rgb(46, 168, 156));
        }
        else if(complete<=90){

            cardComplete.setCardBackgroundColor(Color.rgb(36, 178, 156));
            carddate.setCardBackgroundColor(Color.rgb(36, 178, 156));
        }
        else if(complete<=100){
            cardComplete.setCardBackgroundColor(Color.rgb(26, 188, 156));
            carddate.setCardBackgroundColor(Color.rgb(26, 188, 156));
        }

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
