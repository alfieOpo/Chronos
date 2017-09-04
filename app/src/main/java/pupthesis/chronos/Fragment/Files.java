package pupthesis.chronos.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import java.io.File;

import pupthesis.chronos.Adapter.FilesAdapter;
import pupthesis.chronos.R;
import pupthesis.chronos.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class Files extends Fragment {


    public Files() {
        // Required empty public constructor
    }
    ListView listoffilees;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Config.islastpage=true;
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_files, container, false);
        RadioButton all=(RadioButton)view.findViewById(R.id.all);
        RadioButton image=(RadioButton)view.findViewById(R.id.image);
        RadioButton excel=(RadioButton)view.findViewById(R.id.excel);
        all.setChecked(true);
        LoadList(view,"all");
        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LoadList(view,"all");
                }

            }
        });
        image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LoadList(view,"image");
                }}
        });
        excel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LoadList(view,"excel");}
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        Config.islastpage=true;
        super.onResume();
    }
    private  void LoadList(View view,String Filter){
        try{
            listoffilees=(ListView)view.findViewById(R.id.listoffilees);
            String path = Environment.getExternalStorageDirectory().toString()+"/CHRONOS";
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            int length=0;
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                if(Filter.equals("all")){
                    length++;
                }
                if(Filter.equals("excel")){
                    String ex=files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
                    if(ex.equals(".xls")){
                        length++;
                    }

                }
                if(Filter.equals("image")){
                    String ex=files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
                    if(ex.equals(".png")){
                        length++;
                    }
                }
            }
            final  String[] filesname =new String[length];
            final String[] extension =new String[length];
            Log.d("Files", "Size: "+ files.length);
            int a=0;
            for (int i = 0; i < files.length; i++)
            {
                if(Filter.equals("all")){
                    filesname[a]=files[a].getName();
                    extension[a]= files[a].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
                    Log.d("Files", "FileName:" + files[i].getName());
                    a++;
                }
                if(Filter.equals("excel")){
                    String ex=files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
                    if(ex.equals(".xls")){
                        filesname[a]=files[i].getName();
                        extension[a]= ex;
                        Log.d("Files", "FileName:" + files[i].getName());
                        a++;
                    }

                }
                if(Filter.equals("image")){
                    String ex=files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
                    if(ex.equals(".png")){
                        filesname[a]=files[i].getName();
                        extension[a]= ex;
                        Log.d("Files", "FileName:" + files[i].getName());
                        a++;
                    }

                }
            }listoffilees.setAdapter(null);
            FilesAdapter adapter=new FilesAdapter(getActivity(),filesname,extension);
            listoffilees.setAdapter(adapter);
            listoffilees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (extension[position]){

                        case".png":
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/CHRONOS/"+filesname[position]);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "image/*");
                            startActivity(Intent.createChooser(intent, filesname[position]));
                            break;
                        case".xls":
                            File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/CHRONOS/"+filesname[position]);
                            Intent intent2 = new Intent(Intent.ACTION_VIEW);
                            intent2.setDataAndType(Uri.fromFile(file1), "application/vnd.ms-excel");
                            startActivity(Intent.createChooser(intent2, filesname[position]));
                            break;
                    }

                }
            });}catch (Exception xx){ }
    }
}
