package com.action.onehundred.onehundreddays;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


public class AtyDisplayFmt extends ActionBarActivity implements View.OnClickListener{
    boolean isGrid = true;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_display_fmt);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_with_back);
        TextView tv_title = (TextView) findViewById(R.id.actionbar_title);
        tv_title.setText(R.string.record);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AtyDisplayFmt.this);
            }
        });

         fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.display_fragment_container);
        if (fragment == null){
            fragment = new FmtGridDisplay();
            fm.beginTransaction().add(R.id.display_fragment_container,fragment).commit();
        }
        findViewById(R.id.display_fragment_grid).setOnClickListener(this);
        findViewById(R.id.display_fragment_list).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.display_fragment_grid:
                if(!isGrid){
                    isGrid = true;
                    fm.beginTransaction().replace(R.id.display_fragment_container,new FmtGridDisplay()).commit();
                }
                break;
            case R.id.display_fragment_list:
                if(isGrid){
                    isGrid=false;
                    fm.beginTransaction().replace(R.id.display_fragment_container, new FmtListDisplay()).commit();
                }
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                NavUtils.navigateUpFromSameTask(AtyDisplayFmt.this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
