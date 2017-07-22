package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.TextView;

import pupthesis.chronos.R;

public class GantTaskAdapter extends ArrayAdapter<String> {
    private String[] _nameofprojectTV;
    private String[] _end_date;
    private String[] _start_date;
    private Integer[] imageid;
    private String []_percent_complete;
    private Activity context;
int maxposition=0;
    public GantTaskAdapter(Activity context, String[] __nameofprojectTV,  String[] __start_date,String[] __end_date ,String[] __percent_complete,Integer[] imageid) {
        super(context, R.layout.activity_gant_task_adapter, __nameofprojectTV);
        this.context = context;
        this._nameofprojectTV = __nameofprojectTV;
        this._start_date = __start_date;
        this.imageid = imageid;
        this._percent_complete=__percent_complete;
        this._end_date=__end_date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_gant_task_adapter, null, true);
        TextView nameoftask=(TextView)listViewItem.findViewById(R.id.nameoftaskTV);
        TextView date=(TextView)listViewItem.findViewById(R.id.dateTV);
        TextView percent_complete=(TextView)listViewItem.findViewById(R.id.percent_compeleteTV);
        nameoftask.setText(_nameofprojectTV[position]);
        date.setText(_start_date[position].replace(",","/")+" to "+_end_date[position].replace(",","/"));
        percent_complete.setText(_percent_complete[position]+"%");
        CardView cardComplete=(CardView)listViewItem.findViewById(R.id.cardComplete);
        CardView carddate =(CardView)listViewItem.findViewById(R.id.carddate);
        int complete=Integer.valueOf(_percent_complete[position]);
        if(complete<=10){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan10orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan10orequal));
        }
        else if(complete<=20){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan20orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan20orequal));
        }
        else if(complete<=30){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan30orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan30orequal));
        }
        else if(complete<=40){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan40orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan40orequal));
        }
        else if(complete<=50){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan50orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan50orequal));
        }
        else if(complete<=60){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan60orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan60orequal));
        }
        else if(complete<=70){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan70orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan70orequal));
        }
        else if(complete<=80){

            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan80orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan80orequal));
        }
        else if(complete<=90){

            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan90orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan90orequal));
        }
        else if(complete<=100){
            cardComplete.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan100orequal));
            carddate.setCardBackgroundColor(context.getResources().getColor(R.color.lessthan100orequal));
        }/*
if(position%2==0){
    listViewItem.setBackgroundColor( context.getResources().getColor(R.color.AppbarColorwhite));

}*/

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
