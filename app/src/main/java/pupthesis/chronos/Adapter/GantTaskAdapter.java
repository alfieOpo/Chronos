package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if(position%2==0){
            listViewItem.setBackgroundColor(Color.LTGRAY);

        }
        return  listViewItem;
    }
}
