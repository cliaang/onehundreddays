package com.action.onehundred.onehundreddays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/6/4 0004.
 */
public class ScreenSlidePageFragment extends Fragment {
    private int[] imageResource={
            R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3,
            R.drawable.guide_4
    };
    private int position;
    public  ScreenSlidePageFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int b = bundle.getInt("position");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ImageView iv= (ImageView) rootView.findViewById(R.id.fragment_image_container);
        iv.setImageResource(imageResource[b]);
        return rootView;
    }
}