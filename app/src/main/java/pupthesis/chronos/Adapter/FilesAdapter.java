package pupthesis.chronos.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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
        CardView card1=(CardView)listViewItem.findViewById(R.id.card1);
        CardView card2=(CardView)listViewItem.findViewById(R.id.card2);
        try{
        switch (Extension[position]){

            case ".xls":
                image.setImageResource(R.drawable.excel);
                break;
            case".png":
                try{
                    card1.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                    card2.setCardBackgroundColor(context.getResources().getColor(R.color.concrete));
                    image.setPadding(0,0,0,0);
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/CHRONOS/"+Name[position]);
                    image.setImageURI(Uri.fromFile(file));}
                catch (Exception xx){ image.setPadding(20,20,20,20); image.setImageResource(R.drawable.picture);}

                break;
        }}catch (Exception xx){}

        return  listViewItem;
    }}