package com.action.onehundred.onehundreddays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class AtyActionCategory extends ActionBarActivity implements View.OnClickListener{
    int[] imageIds = new int[]{
            R.id.action_category_image_1,
            R.id.action_category_image_2,
            R.id.action_category_image_3,
            R.id.action_category_image_4,
    };
    int[] textIds = new int[]{
            R.id.action_category_text_1,
            R.id.action_category_text_2,
            R.id.action_category_text_3,
            R.id.action_category_text_4
    };
    int[] layoutIds = new int[]{
            R.id.action_category_1,
            R.id.action_category_2,
            R.id.action_category_3,
            R.id.action_category_4,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_with_back);
        TextView tv_title = (TextView) findViewById(R.id.actionbar_title);
        tv_title.setText(R.string.add_action);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AtyActionCategory.this);
            }
        });

        ImageView iv;
        TextView tv;

        for (int i = 0;i<4;i++){
            iv = (ImageView) findViewById(imageIds[i]);
            iv.setImageResource(Config.ActionCategoryPictureResource[i]);
            tv = (TextView) findViewById(textIds[i]);
            tv.setText(Config.ActionCategoryStringResource[i]);
            findViewById(layoutIds[i]).setOnClickListener(this);
        }
    }

    /**
     * when the BACK key is pressed, return to the parent Activity defined in AndroidManifest.xml.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                NavUtils.navigateUpFromSameTask(AtyActionCategory.this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
       for(int i = 0; i<4;i++) {
           if (v.getId() == layoutIds[i]) {
               intent = new Intent(AtyActionCategory.this, AtyActionList.class);
               intent.putExtra(Config.ActionCategory, i);
               startActivity(intent);
           }
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aty_action_category, menu);
        menu.findItem(R.id.action_category_default).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(AtyActionCategory.this, AtyEditAction.class);
                intent.putExtra(Config.ActionCategory, -1);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }
}
