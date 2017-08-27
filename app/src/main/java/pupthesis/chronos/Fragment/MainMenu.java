package pupthesis.chronos.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.hoang8f.widget.FButton;
import pupthesis.chronos.Activity.About;
import pupthesis.chronos.R;
import pupthesis.chronos.Animation.SlideAnimationUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenu extends Fragment  {

    FButton btn_create;
    FButton btn_refrence;
    FButton btn_about;
    public MainMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main_menu, container, false);
        SlideAnimationUtil.slideInFromLeft(getContext(),view);
        btn_create=(FButton)view.findViewById(R.id.btn_create);
        btn_refrence=(FButton)view.findViewById(R.id.btn_reference);
        btn_about=(FButton)view.findViewById(R.id.btn_about);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new Create()).commit();
            }
        });

        btn_refrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new ReferenceMenu()).commit();
            }
        });

         btn_about.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent startmainactivity = new Intent(getActivity(), About.class);
                 startActivity(startmainactivity);
             }
         });

        return view;
    }

}
