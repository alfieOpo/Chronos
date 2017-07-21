package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.hoang8f.widget.FButton;
import pupthesis.chronos.Gantt;
import pupthesis.chronos.Project;
import pupthesis.chronos.R;
import pupthesis.chronos.SlideAnimationUtil;
import pupthesis.chronos.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferenceMenu extends Fragment {
   FButton btn_project;
    FButton btn_task;
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
        btn_task=(FButton)view.findViewById(R.id.btn_task);
        btn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startmainactivity = new Intent(getActivity(), Task.class);
                startActivity(startmainactivity);
            }
        });

        return view;
    }

}
