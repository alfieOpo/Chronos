package pupthesis.chronos.Adapter;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

public class GantAdapter   extends ArrayAdapter<String> {
    private String[] _nameofprojectTV;
    private String[] _descriptionTV;
    private String[] _statusTV;
    private String[] ID;
    private Activity context;
int maxposition=0;
    public GantAdapter(Activity context, String[] __nameofprojectTV,  String[] __statusTV,String[] __descriptionTV ,String[] ID) {
        super(context, R.layout.activity_gant_adapter, __nameofprojectTV);
        this.context = context;
        this._nameofprojectTV = __nameofprojectTV;
        this._statusTV = __statusTV;
        this.ID = ID;
        this._descriptionTV=__descriptionTV;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_gant_adapter, null, true);
        TextView nameofprojectTV = (TextView) listViewItem.findViewById(R.id.nameoftaskTV);
        TextView statusTV = (TextView) listViewItem.findViewById(R.id.dateTV);
        ImageView taskimage = (ImageView) listViewItem.findViewById(R.id.taskimage);
        TextView DescriptionTV=(TextView) listViewItem.findViewById(R.id.DescriptionTV);

        TextView taskcountTV=(TextView) listViewItem.findViewById(R.id.taskcountTV);
        nameofprojectTV.setText(_nameofprojectTV[position]);
        DescriptionTV.setText(_descriptionTV[position]);
        statusTV.setText(_statusTV[position]);
        CardView cardimage=(CardView)listViewItem.findViewById(R.id.cardimage);
        CardView cardprojectname=(CardView)listViewItem.findViewById(R.id.cardprojectname);
        CardView cardstatus=(CardView)listViewItem.findViewById(R.id.cardstatus);
        CardView   carddescription=(CardView)listViewItem.findViewById(R.id.carddescription);
        CardView cardtasknotif=(CardView)listViewItem.findViewById(R.id.cardtasknotif);
        try{   switch (_statusTV[position]){
            case "Not yet Started":
                taskimage.setImageResource(R.drawable.undone);

                cardimage.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                cardstatus.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                cardprojectname.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                carddescription.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                break;
            case "In progress":
                taskimage.setImageResource(R.drawable.progress);
                cardimage.setCardBackgroundColor(context.getResources().getColor(R.color.fbutton_color_green_sea));
                cardstatus.setCardBackgroundColor(context.getResources().getColor(R.color.fbutton_color_green_sea));
                cardprojectname.setCardBackgroundColor(context.getResources().getColor(R.color.fbutton_color_green_sea));
                carddescription.setCardBackgroundColor(context.getResources().getColor(R.color.fbutton_color_green_sea));
                break;
            case "Complete":
                taskimage.setImageResource(R.drawable.complete);

                cardimage.setCardBackgroundColor(context.getResources().getColor(R.color.AppbarColor));
                cardstatus.setCardBackgroundColor(context.getResources().getColor(R.color.AppbarColor));
                cardprojectname.setCardBackgroundColor(context.getResources().getColor(R.color.AppbarColor));
                carddescription.setCardBackgroundColor(context.getResources().getColor(R.color.AppbarColor));
                break;


        }}catch (Exception xx){}
        DataBaseHandler da=new DataBaseHandler(context);
        String counttask=da.getCountofProjectUnseen(ID[position]);
        if(!counttask.equals("0")){
            cardtasknotif.setVisibility(View.VISIBLE);
            taskcountTV.setVisibility(View.VISIBLE);
            taskcountTV.setText(counttask);
        }
        else{
            cardtasknotif.setVisibility(View.GONE);
            taskcountTV.setVisibility(View.GONE);
            taskcountTV.setText("0");
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

