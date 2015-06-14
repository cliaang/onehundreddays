package com.action.onehundred.onehundreddays;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class AtyGuide extends Activity {

    private ViewPager viewPager;
    private MyViewPagerAdapter viewPagerAdapter;
    private ImageView[] dots;
    private List<View> views;
    private Button start_button;
    private LinearLayout linearLayout;
    private int[] picture = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4, R.drawable.guide_5};
    private int currentIndex ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_guide);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        views = new ArrayList<View>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < picture.length ; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setImageResource(picture[i]);
            views.add(imageView);
        }
        viewPagerAdapter = new MyViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new MyOnPagerChangeListener());
        initDots();
    }

    private boolean setCurrentDot(int position) {
        if (position < 0 || position > picture.length - 2 || position == currentIndex){
            currentIndex = position;
            return false;
        }

        dots[position].setImageResource(R.drawable.guide_round_current);
        dots[currentIndex].setImageResource(R.drawable.guide_round_default);
        currentIndex = position;
        return true;
    }

    private void initDots() {
        linearLayout = (LinearLayout) findViewById(R.id.point_image);
        dots = new ImageView[linearLayout.getChildCount()];
        for (int i = 0; i < picture.length -1 ; i++) {
            dots[i]= (ImageView) linearLayout.getChildAt(i);
            dots[i].setTag(i);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (currentIndex == picture.length-1&& positionOffset>0.2){
                startActivity(new Intent(AtyGuide.this,AtyCurrentAction.class));
            }
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentDot(position);
            if (currentIndex == picture.length - 1 ){
                linearLayout.setVisibility(View.GONE);
            }else {
                linearLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
