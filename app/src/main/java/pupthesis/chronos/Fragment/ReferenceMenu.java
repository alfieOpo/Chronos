package pupthesis.chronos.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.hoang8f.widget.FButton;
import pupthesis.chronos.Activity.Project;
import pupthesis.chronos.Activity.Project_Line;
import pupthesis.chronos.R;
import pupthesis.chronos.Animation.SlideAnimationUtil;
import pupthesis.chronos.Activity.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferenceMenu extends Fragment {
   FButton btn_project;
    FButton btn_line;
    public ReferenceMenu() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reference_menu, container, false);
        SlideAnimationUtil.slideInFromRight(getContext(),view);
        btn_project=(FButton)view.findViewById(R.id.btn_project);
        btn_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startmainactivity = new Intent(getActivity(), Project.class);
                startActivity(startmainactivity);
            }
        });
        btn_line=(FButton)view.findViewById(R.id.btn_line);
        btn_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startmainactivity = new Intent(getActivity(), Project_Line.class);
                startActivity(startmainactivity);
            }
        });

        return view;
    }

}
