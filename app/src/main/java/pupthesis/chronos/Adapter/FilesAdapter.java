package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pupthesis.chronos.R;

public class FilesAdapter extends ArrayAdapter<String> {
    private String[] Extension;
    private String[] Name;
    private Activity context;


    public FilesAdapter(Activity context, String[] Name,String[] Extension) {
        super(context, R.layout.activity_files_adapter, Name);
        this.context = context;
        this.Name=Name;
        this.Extension=Extension;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_files_adapter, null, true);
        TextView name=(TextView)listViewItem.findViewById(R.id.name);
        name.setText(Name[position]);
        ImageView image=(ImageView)listViewItem.findViewById(R.id.image);
        switch (Extension[position]){

            case ".xls":
                image.setImageResource(R.drawable.excel);
                break;
            case".png":
                image.setImageResource(R.drawable.picture);
                break;
        }
        return  listViewItem;
    }}