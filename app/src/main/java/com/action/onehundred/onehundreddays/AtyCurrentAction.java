package com.action.onehundred.onehundreddays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;


public class AtyCurrentAction extends ActionBarActivity {
    public final static String DayIndex = "dayIndex";
    private int dayIndex;
    private boolean hasAction;

    private Action currentAction= new Action("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // judge whether currentAction has been saved in SharedPreference.
        // if not saved, use the add_first layout
        // or use the current_action layout

        hasAction = getSharedPreferences(Config.ActionConfig,Context.MODE_PRIVATE).getBoolean(Config.ActionConfigHasAction,false);
        if (!hasAction) {
            Log.d("TAG", "no action, need to add");
            setContentView(R.layout.activity_add_first);
            findViewById(R.id.add_first).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AtyCurrentAction.this, AtyActionCategory.class);
                    startActivity(i);
                }
            });
        }
        else {
            Log.d("TAG", "action exists");

            setContentView(R.layout.activity_current_action);
            try {
                SharedPreferences sp = getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
                String jsonString = sp.getString(Config.CurrentActionDetails, "");
                currentAction = Action.parseJSONString(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int actionCategroy = currentAction.getActionCategory();
            int actionCategroyIndex = currentAction.getActionCategoryIndex();

            dayIndex = calculateDayIndex();

            TextView tv_actionName = (TextView) findViewById(R.id.action_name);
            tv_actionName.setText(currentAction.getActionName());

            ImageView iv_actionPicture = (ImageView) findViewById(R.id.current_action_action_background);
            try {
                iv_actionPicture.setImageResource(Config.ActionsPictureResource[actionCategroy][actionCategroyIndex]);
            } catch(ArrayIndexOutOfBoundsException e ){
                iv_actionPicture.setImageResource(R.drawable.running);
            }
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            iv_actionPicture.getLayoutParams().height = size.x*5/8;

            TextView tv_totalComplements = (TextView) findViewById(R.id.current_action_total_complements);
            tv_totalComplements.setText(currentAction.getTotalCompletions() + "");

            TextView tv_currentLasting = (TextView) findViewById(R.id.current_action_current_lasting);
            tv_currentLasting.setText(currentAction.getCurrentLasting() + "");

            TextView tv_actionDescription = (TextView) findViewById(R.id.current_action_description);

            try {
                Resources res = getResources();
                InputStream in_s = res.openRawResource(Config.ActionsDescriptionResource[actionCategroy][actionCategroyIndex]);

                byte[] b = new byte[in_s.available()];
                in_s.read(b);
                tv_actionDescription.setText(new String(b));
            } catch (Exception e) {
                // e.printStackTrace();
                tv_actionDescription.setText("");
            }

            Button btn_signature = (Button) findViewById(R.id.current_action_signature);
            btn_signature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(AtyCurrentAction.this, AtyEveryDayRecord.class);
                    i.putExtra(DayIndex, dayIndex);
                    startActivity(i);
                }
            });
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_only_text);
        TextView tv_title = (TextView) findViewById(R.id.actionbar_title);
        tv_title.setText(R.string.app_name);
    }

    /**
     *
     * @return days between current day and start day of the Action
     */
    private int calculateDayIndex(){
        Calendar start = Calendar.getInstance();
        start.setTime(currentAction.getStart());
        Calendar current = Calendar.getInstance();
        current.setTime(new Date());

        int sDay = start.get(Calendar.DAY_OF_YEAR);
        int cDay = current.get(Calendar.DAY_OF_YEAR);

        return cDay-sDay;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!hasAction){
            return true;
        }
        getMenuInflater().inflate(R.menu.menu_aty_current_action, menu);
        menu.findItem(R.id.current_action_record).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(AtyCurrentAction.this, AtyDisplayFmt.class));
                return true;
            }
        });
        menu.findItem(R.id.current_action_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(AtyCurrentAction.this)
                        .setTitle(R.string.give_up_action)
                        .setMessage(R.string.give_up_action_hint)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
                                sp.edit().clear().apply();
                                sp = getSharedPreferences(Config.ActionConfig,Context.MODE_PRIVATE);
                                sp.edit().putBoolean(Config.ActionConfigHasAction,false);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
        return true;
    }

}
