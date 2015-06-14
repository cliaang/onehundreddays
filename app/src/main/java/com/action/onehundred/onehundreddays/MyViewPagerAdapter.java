package com.action.onehundred.onehundreddays;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2015/6/4.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    public List<View> views;

    public MyViewPagerAdapter(List<View> views){
        this.views = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView(views.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        (container).addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public int getCount() {
        if (views != null)
            return views.size();
        else return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view == object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public void finishUpdate(ViewGroup container) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup container) {

    }
}
