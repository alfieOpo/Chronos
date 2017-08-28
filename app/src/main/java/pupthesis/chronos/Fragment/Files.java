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
import android.widget.ListView;

import java.io.File;

import pupthesis.chronos.Adapter.FilesAdapter;
import pupthesis.chronos.R;

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
        // Inflate the layout for this fragment
View view=inflater.inflate(R.layout.fragment_files, container, false);
        listoffilees=(ListView)view.findViewById(R.id.listoffilees);
        String path = Environment.getExternalStorageDirectory().toString()+"/CHRONOS";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        final  String[] filesname =new String[files.length];
       final String[] extension =new String[files.length];
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            filesname[i]=files[i].getName();
            extension[i]= files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf("."));
            Log.d("Files", "FileName:" + files[i].getName());
        }
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
        });
        return view;
    }

}
