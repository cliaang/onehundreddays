package com.action.onehundred.onehundreddays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class AtyActionList extends ActionBarActivity {
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_action_list);
        final int actionCategory= getIntent().getIntExtra(Config.ActionCategory,0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_with_back);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavUtils.navigateUpFromSameTask(AtyActionList.this);
                finish();
            }
        });
        TextView titleText = (TextView) findViewById(R.id.actionbar_title);
            titleText.setText(Config.ActionCategoryStringResource[actionCategory]);

        lv = (ListView) findViewById(R.id.action_list);
        lv.setAdapter(new ActionAdapter(actionCategory));
        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AtyActionList.this,AtyEditAction.class);
                i.putExtra(Config.ChosenActionCategory,actionCategory);
                i.putExtra(Config.ChosenActionCategoryIndex,position);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                //NavUtils.navigateUpFromSameTask(AtyActionList.this);
                finish();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private class ActionAdapter extends BaseAdapter{
        int categoryIndex;
        public ActionAdapter(int i){
            categoryIndex = i;
        }
        @Override
        public int getCount() {
            return Config.ActionsPictureResource[categoryIndex].length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_action_list,parent,false);
            }
            TextView tv_action = (TextView) convertView.findViewById(R.id.action_name);
            tv_action.setText(Config.ActionsStringResource[categoryIndex][position]);
            ImageView iv_action = (ImageView) convertView.findViewById(R.id.action_image);
            iv_action.setImageResource(Config.ActionsPictureResource[categoryIndex][position]);
            return convertView;
        }
    }
}
