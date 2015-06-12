package com.action.onehundred.onehundreddays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;


public class AtyEveryDayRecord extends ActionBarActivity {
    private Action currentAction= new Action("");
    private int dayIndex;
    Switch sw_completed;
    EditText et_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_every_day_record);
        dayIndex = getIntent().getExtras().getInt(AtyCurrentAction.DayIndex);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_with_back);
        TextView tv_title = (TextView) findViewById(R.id.actionbar_title);
        tv_title.setText(R.string.every_record);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to the parent activity defined in AndroidManifest.xml
                // by android:parentActivity label.
                NavUtils.navigateUpFromSameTask(AtyEveryDayRecord.this);
            }
        });
        try {
            SharedPreferences sp = getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
            String jsonString = sp.getString(Config.CurrentActionDetails, "");
            currentAction = Action.parseJSONString(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         sw_completed = (Switch) findViewById(R.id.every_record_switch);
          et_record = (EditText) findViewById(R.id.every_record_editText);

        sw_completed.setChecked(false);
        sw_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_record.setHint("so good");
                } else {
                    et_record.setHint("come on, do better tomorrow");
                }
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                NavUtils.navigateUpFromSameTask(AtyEveryDayRecord.this);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aty_everyday_record, menu);
        menu.findItem(R.id.every_record_save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DailyRecord[] records=  currentAction.getRecords();
                records[dayIndex].setCompleted(sw_completed.isChecked());
                records[dayIndex].setRecord(et_record.getText().toString());
                SharedPreferences sp =getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                try {
                    editor.putString(Config.CurrentActionDetails, currentAction.toJSON().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
                startActivity(new Intent(AtyEveryDayRecord.this, AtyDisplayFmt.class));
                finish();
                return true;
            }
        });
        return true;
    }
}
