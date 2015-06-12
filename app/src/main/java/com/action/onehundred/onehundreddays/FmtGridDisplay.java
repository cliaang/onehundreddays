package com.action.onehundred.onehundreddays;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

import org.json.JSONException;


public class FmtGridDisplay extends Fragment {

    private Action currentAction = new Action("");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            SharedPreferences sp = getActivity().getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
            String jsonString = sp.getString(Config.CurrentActionDetails, "");
            currentAction = Action.parseJSONString(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_aty_display_grid,container,false);

        GridView gridview = (GridView) v.findViewById(R.id.gridview);

        // 取得屏幕的宽度和高度
        WindowManager windowManager =getActivity(). getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point p=new Point();
        display.getSize(p);
        gridview.setAdapter(new DisplayGridAdapter(getActivity(),p.x));

        return v;
    }
}
