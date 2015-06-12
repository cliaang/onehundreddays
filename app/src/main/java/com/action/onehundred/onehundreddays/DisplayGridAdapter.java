package com.action.onehundred.onehundreddays;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;


public class DisplayGridAdapter extends BaseAdapter {
    private Action currentAction = new Action("");
    private Context mContext;
    int totalWidth;

    public DisplayGridAdapter(Context c, int width) {
        mContext = c;
        totalWidth = width;

        try {
            SharedPreferences sp = mContext.getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
            String jsonString = sp.getString(Config.CurrentActionDetails, "");
            currentAction = Action.parseJSONString(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        return 100;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);

            textView.setLayoutParams(new GridView.LayoutParams(totalWidth / 12, totalWidth / 12));
            textView.setPadding(totalWidth / 120, totalWidth / 120, totalWidth /120, totalWidth /120);
        } else {
            textView = (TextView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        //imageView.setBackgroundColor(Color.BLUE);
        if(currentAction.getRecords()[position].isCompleted()) {
            textView.setBackgroundResource(R.drawable.round_button_completed);
        } else{
            textView.setBackgroundResource(R.drawable.round_button_uncompleted);
        }
        textView.setText((position + 1) + "");
        textView.setTextSize(10);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }




}
