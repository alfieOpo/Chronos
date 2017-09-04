package pupthesis.chronos.Adapter;

import android.app.Activity;
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

import pupthesis.chronos.Access.DataBaseHandler;
import pupthesis.chronos.R;

public class ProjectAdapter extends ArrayAdapter<String> {
    private String[] ProjectName;
    private String[] ProjectID;
    private Activity context;
    private  int maxposition=0;
    private  boolean gantt=false;
    public ProjectAdapter(Activity context, String[] ProjectName,String[] ProjectID,boolean gantt) {
        super(context, R.layout.activity_project_adapter, ProjectName);
        this.context = context;
        this.ProjectName = ProjectName;
        this.ProjectID=ProjectID;
        this.gantt=gantt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_project_adapter, null, true);
        CardView cardprojecttaskCount;
        TextView projecttaskCountTV,projectnameTV;
        projecttaskCountTV=(TextView)listViewItem.findViewById(R.id.projecttaskCountTV);
        projectnameTV=(TextView)listViewItem.findViewById(R.id.projectnameTV);
        cardprojecttaskCount=(CardView)listViewItem.findViewById(R.id.cardprojecttaskCount);
        ///


        DataBaseHandler da=new DataBaseHandler(context);

        String item=" Item";

        if(gantt){
            int count=Integer.parseInt(da.getCountTaskforProject(ProjectID[position]));
            if(count>1){
                item=" Items";
            }
                projecttaskCountTV.setText(da.getCountTaskforProject(ProjectID[position])+item);
        }
        else
        {int count=Integer.parseInt(da.getCountTaskforProjectLine(ProjectID[position]));
            if(count>1){
                item=" Items";
            }
            projecttaskCountTV.setText(da.getCountTaskforProjectLine(ProjectID[position])+item);
        }

        projectnameTV.setText(ProjectName[position]);
        ///
        //
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

