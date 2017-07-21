package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.hoang8f.widget.FButton;
import pupthesis.chronos.Gantt;
import pupthesis.chronos.R;
import pupthesis.chronos.BaseActivity;
import pupthesis.chronos.SlideAnimationUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class Create extends Fragment {
    FButton btn_gantt;

    public Create() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_create, container, false);

        SlideAnimationUtil.slideInFromRight(getContext(),view);
        btn_gantt=(FButton)view.findViewById(R.id.btn_gantt);
        btn_gantt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startmainactivity = new Intent(getActivity(), Gantt.class);
                startActivity(startmainactivity);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        return view;
    }



}
